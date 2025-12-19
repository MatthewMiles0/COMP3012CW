package uk.ac.nott.cs.comp3012.coursework.tam;

import uk.ac.nott.cs.comp3012.coursework.ast.*;
import uk.ac.nott.cs.comp3012.coursework.exceptions.TamGenException;
import uk.ac.nott.cs.comp3012.coursework.semantic.Type;
import uk.ac.nott.cs.comp3012.coursework.symbol.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TamInstructionBuilder extends AstVisitor<Void> {
    private final ArrayList<TamInstruction> instructions = new ArrayList<>();
    private final Map<String, Integer> varMap = new HashMap<>();
    private int stackSize = 0;
    private final SymbolTable table;

    public TamInstructionBuilder(SymbolTable table) {
        this.table = table;
    }

    public TamInstruction[] build(Ast ast) {
        visit(ast);
        return instructions.toArray(new TamInstruction[0]);
    }

    private void addInstruction(TamInstruction instruction) {
        System.out.println(stackSize + " : " + instruction);
        instructions.add(instruction);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var instruction : instructions) {
            sb.append(instruction).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Void visitProgram(Program p) {
        visit(p.block());
        addInstruction(new TamGenericInstruction(TamOp.HALT, null, null, null));
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        int startStackSize = stackSize;
        List<String> vars = new ArrayList<>();
        for (var iv : b.varInits()) {
            if (iv.type().arrayDimensions() == null && iv.type().pointerLevel() == 0) {
                switch (iv.type().baseType()) {
                    case INT:
                    case BOOL:
                        varMap.put(iv.vr().name(), stackSize);
                        vars.add(iv.vr().name());
                        stackSize += 1;
                        break;
                    default:
                        throw new TamGenException("Cannot initialise type " + iv.type());
                }
            } else if (iv.type().arrayDimensions() != null) {
                switch (iv.type().baseType()) {
                    case INT:
                    case BOOL:
                        varMap.put(iv.vr().name(), stackSize);
                        vars.add(iv.vr().name());
                        int size = iv.type().arrayDimensions().getFirst();
                        for (int i = 1; i < iv.type().arrayDimensions().size(); i++) {
                            size *= iv.type().arrayDimensions().get(i);
                        }
                        stackSize += size;
                        break;
                }
            } else {
                throw new TamGenException("Cannot initialise pointer type " + iv.type());
            }
        }
        int blockStackSize = stackSize - startStackSize;
        if (blockStackSize > 0) addInstruction(new TamGenericInstruction(TamOp.PUSH, null, blockStackSize, null));
        for (var s : b.statements()) {
            visit(s);
        }
        if (blockStackSize > 0) addInstruction(new TamGenericInstruction(TamOp.POP, 0, blockStackSize, null));
        stackSize -= blockStackSize;
        for (var v : vars) {
            varMap.remove(v);
        }
        return null;
    }

    @Override
    public Void visitIfSt(IfSt is) {
        // cond
        visit(is.condition());

        // jump to else instructions placeholder
        int jumpIfIndex = instructions.size();
        addInstruction(TamGenericInstruction.getPlaceholder());
        stackSize -= 1;

        // then
        visit(is.thenBlock());

        // jump over else instructions placeholder
        int jumpOverElseIndex = instructions.size();
        addInstruction(TamGenericInstruction.getPlaceholder());

        // else
        int elseStart = instructions.size();
        visit(is.elseBlock());
        int afterElse = instructions.size();

        // patch
        // else placeholder
        instructions.set(jumpIfIndex, new TamGenericInstruction(TamOp.JUMPIF, 0, elseStart, TamRegister.CB));

        // jump over else placeholder
        instructions.set(jumpOverElseIndex, new TamGenericInstruction(TamOp.JUMP, null, afterElse, TamRegister.CB));

        return null;
    }

    @Override
    public Void visitInt(Int i) {
        addInstruction(new TamGenericInstruction(TamOp.LOADL, null, i.value(), null));
        stackSize += 1;
        return null;
    }

    @Override
    public Void visitBool(Bool b) {
        addInstruction(new TamGenericInstruction(TamOp.LOADL, null, b.value() ? 1 : 0, null));
        stackSize += 1;
        return null;
    }

    @Override
    public Void visitReal(Real r) {
//        addInstruction(new TamGenericInstruction(TamOp.LOADL, null, r.value()., null));
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Void visitString(Str s) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Void visitBinOp(BinOp bo) {
        visit(bo.left());
        visit(bo.right());
        if (bo.op() == Op.EQ || bo.op() == Op.NEQ) {
            addInstruction(new TamGenericInstruction(TamOp.LOADL, null, 1, null));
        }
        addInstruction(new TamQuickCallInstruction(bo.op()));
        stackSize -= 1;
        return null;
    }

    @Override
    public Void visitWriteSt(WriteSt ws) {
        for (var e : ws.expr()) {
            visit(e);
            addInstruction(new TamQuickCallInstruction(TamPrimitive.PUTINT));
            stackSize -= 1;
        }
        return null;
    }

    @Override
    public Void visitReadSt(ReadSt rs) {
        Type type;
        for (var vr : rs.var()) {
            visitVarAddress(vr);
            type = table.lookup(vr.name()).type();
            switch (type.baseType()) {
                case INT:
                case BOOL:
                    addInstruction(new TamQuickCallInstruction(TamPrimitive.GETINT));
                    break;
                default:
                    throw new TamGenException("Cannot read type " + type);
            }
        }
        return null;
    }

    @Override
    public Void visitDoWhile(DoWhile dw) {
        int jumpIndex = instructions.size();
        visit(dw.expr());
        int condJumpIfIndex = instructions.size();
        addInstruction(TamInstruction.getPlaceholder());
        visit(dw.block());
        addInstruction(new TamGenericInstruction(TamOp.JUMP, null, jumpIndex, TamRegister.CB));
        int endIndex = instructions.size();
        instructions.set(condJumpIfIndex, new TamGenericInstruction(TamOp.JUMPIF, 0, endIndex, TamRegister.CB));
        return null;
    }

    @Override
    public Void visitDoLoop(DoLoop dl) {
        visit(dl.init());
        int loopStart = instructions.size();
        visit(dl.init().varRef());
        visit(dl.max());
        addInstruction(new TamQuickCallInstruction(TamPrimitive.LT));
        stackSize -= 1;
        int loopJumpIfIndex = instructions.size();
        addInstruction(TamInstruction.getPlaceholder());
        visit(dl.block());
        visit(dl.init().varRef());
        visit(dl.step());
        addInstruction(new TamQuickCallInstruction(TamPrimitive.ADD));
        stackSize -= 1;
        saveStackTopAtVar(dl.init().varRef());
        addInstruction(new TamGenericInstruction(TamOp.JUMP, null, loopStart, TamRegister.CB));
        int endIndex = instructions.size();
        instructions.set(loopJumpIfIndex, new TamGenericInstruction(TamOp.JUMPIF, 0, endIndex, TamRegister.CB));
        return null;
    }


    @Override
    public Void visitVarRef(VarRef vr) {
        Type type = table.lookup(vr.name()).type();
        visitVarAddress(vr);
        switch (type.baseType()) {
            case INT:
            case BOOL:
                addInstruction(new TamGenericInstruction(TamOp.LOADI, 1, null, null));
                break;
            default:
                throw new TamGenException("Cannot load array type " + type);
        }
        return null;
    }

    private void visitVarAddress(VarRef vr) {
        int stackIndex = varMap.getOrDefault(vr.name(), -1);
        Type type = table.lookup(vr.name()).type();
        if (stackIndex == -1) {
            throw new TamGenException("Variable " + vr.name() + " not found on stack");
        }
        if (type.arrayDimensions() == null && type.pointerLevel() == 0) {
            switch (type.baseType()) {
                case INT:
                case BOOL:
                    addInstruction(new TamGenericInstruction(TamOp.LOADA, null, stackIndex, TamRegister.SB));
                    stackSize += 1;
                    break;
                default:
                    throw new TamGenException("Cannot load address of singular type " + type);
            }
        } else if (type.arrayDimensions() != null) {
            switch (type.baseType()) {
                case INT:
                case BOOL:
                    calculateIndex(vr, type);
                    addInstruction(new TamGenericInstruction(TamOp.LOADA, null, stackIndex, TamRegister.SB));
                    stackSize += 1;
                    addInstruction(new TamQuickCallInstruction(TamPrimitive.ADD));
                    stackSize -= 1;
                    break;
                default:
                    throw new TamGenException("Cannot load address of array type " + type);
            }
        } else {
            throw new TamGenException("Cannot load address of pointer type " + type);
        }
    }

    private void calculateIndex(VarRef vr, Type type) {
        for (int i = 0; i < type.arrayDimensions().size(); i++) {
            visit(vr.indices().get(i));
            if (i > 0) {
                addInstruction(new TamGenericInstruction(TamOp.LOADL, null, type.arrayDimensions().get(i), null));
                stackSize += 1;
                addInstruction(new TamQuickCallInstruction(TamPrimitive.MULT));
                stackSize -= 1;
                addInstruction(new TamQuickCallInstruction(TamPrimitive.ADD));
                stackSize -= 1;
            }
        }
    }

    @Override
    public Void visitAssignment(Assignment a) {
        visit(a.expr());
        saveStackTopAtVar(a.varRef());
        return null;
    }

    private void saveStackTopAtVar(VarRef vr) {
        Type type = table.lookup(vr.name()).type();
        int stackIndex = varMap.getOrDefault(vr.name(), -1);

        if (type.arrayDimensions() == null && type.pointerLevel() == 0) {
            switch (type.baseType()) {
                case INT:
                case BOOL:
                    addInstruction(new TamGenericInstruction(TamOp.STORE, 1, stackIndex, TamRegister.SB));
                    stackSize -= 1;
                    break;
                default:
                    throw new TamGenException("Cannot save singular type " + type);
            }
        } else if (type.arrayDimensions() != null) {
            switch (type.baseType()) {
                case INT:
                case BOOL:
                    calculateIndex(vr, type);
                    addInstruction(new TamGenericInstruction(TamOp.LOADA, null, stackIndex, TamRegister.SB));
                    stackSize += 1;
                    addInstruction(new TamQuickCallInstruction(TamPrimitive.ADD));
                    stackSize -= 1;

                    addInstruction(new TamGenericInstruction(TamOp.STOREI, 1, null, null));
                    stackSize -= 1;
                    break;
            }
        } else {
            throw new TamGenException("Cannot save pointer type " + type);
        }


        stackSize -= 1;
    }
}

package uk.ac.nott.cs.comp3012.coursework.tam;

import jdk.jshell.spi.ExecutionControl;
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
            switch (iv.type()) {
                case INT:
                case BOOL:
                    varMap.put(iv.vr().name(), stackSize);
                    vars.add(iv.vr().name());
                    stackSize += 1;
                    break;
                default:
                    throw new TamGenException("Cannot initialise type " + iv.type());
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
        int stackIndex;
        for (var vr : rs.var()) {
            type = table.lookup(vr.name()).type();
            stackIndex = varMap.getOrDefault(vr.name(), -1);
            if (stackIndex == -1) {
                throw new TamGenException("Variable " + vr.name() + " not found on stack");
            }
            switch (type) {
                case INT:
                case BOOL:
                    addInstruction(new TamGenericInstruction(TamOp.LOADA, null, stackIndex, TamRegister.SB));
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
        visit(dl.max());
        visit(dl.step());
        visit(dl.block());
        return null;
    }

    @Override
    public Void visitVarRef(VarRef vr) {
        int stackIndex = varMap.getOrDefault(vr.name(), -1);
        Type type = table.lookup(vr.name()).type();
        if (stackIndex == -1) {
            throw new TamGenException("Variable " + vr.name() + " not found on stack");
        }
        switch (type) {
            case INT:
            case BOOL:
                addInstruction(new TamGenericInstruction(TamOp.LOAD, 1, stackIndex, TamRegister.SB));
                stackSize += 1;
                varMap.put(vr.name(), stackIndex);
                break;
            default:
                throw new TamGenException("Cannot load type " + type);
        }
        return null;
    }

    @Override
    public Void visitAssignment(Assignment a) {
        visit(a.expr());
        Type type = table.lookup(a.varRef().name()).type();
        int stackIndex = varMap.getOrDefault(a.varRef().name(), -1);

        addInstruction(new TamGenericInstruction(TamOp.STORE, 1, stackIndex, TamRegister.SB));
        stackSize -= 1;

        return null;
    }
}

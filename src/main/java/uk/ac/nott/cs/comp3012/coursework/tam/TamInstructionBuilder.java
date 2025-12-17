package uk.ac.nott.cs.comp3012.coursework.tam;

import uk.ac.nott.cs.comp3012.coursework.ast.*;

import java.util.ArrayList;

public class TamInstructionBuilder extends AstVisitor<Void> {
    public ArrayList<TamInstruction> instructions = new ArrayList<>();

    public TamInstruction[] build(Ast ast) {
        visit(ast);
        return instructions.toArray(new TamInstruction[0]);
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
        instructions.add(new TamGenericInstruction(TamOp.HALT, null, null, null));
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        for (var s : b.statements()) {
            visit(s);
        }
        return null;
    }

    @Override
    public Void visitIfSt(IfSt is) {
        // cond
        visit(is.condition());

        // jump to else instructions placeholder
        int jumpIfIndex = instructions.size();
        instructions.add(TamGenericInstruction.getPlaceholder());

        // then
        visit(is.thenBlock());

        // jump over else instructions placeholder
        int jumpOverElseIndex = instructions.size();
        instructions.add(TamGenericInstruction.getPlaceholder());

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
        instructions.add(new TamGenericInstruction(TamOp.LOADL, null, i.value(), null));
        return null;
    }

    @Override
    public Void visitBool(Bool b) {
        instructions.add(new TamGenericInstruction(TamOp.LOADL, null, b.value() ? 1 : 0, null));
        return null;
    }

    @Override
    public Void visitBinOp(BinOp bo) {
        visit(bo.left());
        visit(bo.right());
        instructions.add(new TamQuickCallInstruction(bo.op()));
        return null;
    }

    @Override
    public Void visitWriteSt(WriteSt ws) {
        for (var e : ws.expr()) {
            visit(e);
            instructions.add(new TamQuickCallInstruction(TamPrimitive.PUTINT));
        }
        return null;
    }
}

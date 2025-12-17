package uk.ac.nott.cs.comp3012.coursework.tam;

import uk.ac.nott.cs.comp3012.coursework.ast.Op;

public class TamCallInstruction extends TamInstruction {
    private final TamPrimitive primitive;

    public TamCallInstruction(TamPrimitive primitive) {
        this.op = TamOp.CALL;
        this.primitive = primitive;
    }

    public TamCallInstruction(Op op) {
        TamPrimitive primitive = TamPrimitive.valueOf(op.name());
        this.op = TamOp.CALL;
        this.primitive = primitive;
    }

    @Override
    protected String getInstructionString() {
        return op.toString() + " " + primitive.name().toLowerCase();
    }
}

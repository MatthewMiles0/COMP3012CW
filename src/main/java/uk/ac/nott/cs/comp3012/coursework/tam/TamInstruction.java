package uk.ac.nott.cs.comp3012.coursework.tam;

public abstract class TamInstruction {
    protected TamOp op;

    protected abstract String getInstructionString();

    @Override
    public String toString() {
        if (op == null) return "; Could not generate instruction: null operator";
        if (op == TamOp.UNUSED) return "; Unused instruction";
        return getInstructionString();
    }

    public static TamGenericInstruction getPlaceholder() {
        return new TamGenericInstruction(TamOp.UNUSED, null, null, null);
    }

    public abstract byte[] toByteArray();
}

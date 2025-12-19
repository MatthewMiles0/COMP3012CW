package uk.ac.nott.cs.comp3012.coursework.tam;

import uk.ac.nott.cs.comp3012.coursework.exceptions.TamGenException;

public class TamGenericInstruction extends TamInstruction {
    private final Integer n;
    private final Integer d;
    private final TamRegister r;

    public TamGenericInstruction(TamOp op, Integer n, Integer d, TamRegister r) {
        this.op = op;
        this.n = n;
        this.d = d;
        this.r = r;

        if (!op.allowsNArg() && n != null) {
            throw new TamGenException("Did not expect N arg but got "+n);
        }

        if (op.needsDArg() == (d == null)) {
            throw new TamGenException((op.needsDArg()?"Expected":"Did not expect")+" D arg but got "+d);
        }

        if (op.needsRArg() == (r == null)) {
            throw new TamGenException((op.needsRArg()?"Expected":"Did not expect")+" R arg but got "+r);
        }
    }

    private String getNDR() {
        StringBuilder sb = new StringBuilder();
        if (n != null) sb.append("(").append(n).append(")");
        sb.append(" ");
        if (d != null) sb.append(d);
        if (r != null) sb.append("[").append(r).append("]");
        return sb.toString();
    }

    @Override
    public String getInstructionString() {
        return op.toString() + getNDR();
    }

    @Override
    public byte[] toByteArray() {
        int rI = 0, nI = 0, dI = 0;
        if (r != null) rI = r.getRegisterNumber();
        if (n != null) nI = n;
        if (d != null) dI = d;
        return new byte[]{(byte) ((op.getOpCode() << 4) | rI), (byte) nI,
                (byte) ((dI & 0xff00) >>> 8), (byte) (dI & 0xff),};
    }
}

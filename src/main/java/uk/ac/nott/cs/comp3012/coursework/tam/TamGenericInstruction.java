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

//        if (op.needsNArg() == (n == null)) {
//            throw new TamGenException((op.needsNArg()?"Expected":"Did not expect")+" N arg but got "+n);
//        }

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
        return new byte[]{(byte) ((op.getOpCode() << 4) | r.getRegisterNumber()), n.byteValue(),
                (byte) ((d & 0xff00) >>> 8), (byte) (d & 0xff),};
    }
}

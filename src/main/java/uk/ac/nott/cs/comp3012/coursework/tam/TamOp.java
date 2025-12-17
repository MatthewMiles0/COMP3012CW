package uk.ac.nott.cs.comp3012.coursework.tam;

public enum TamOp {
    LOAD,
    LOADA,
    LOADI,
    LOADL,
    STORE,
    STOREI,
    CALL,
    CALLI,
    RETURN,
    UNUSED, // do not use. placed for ordinal op code
    PUSH,
    POP,
    JUMP,
    JUMPI,
    JUMPIF,
    HALT;

    public int getOpCode() {
        return ordinal();
    }

    public boolean needsNArg() {
        return switch (this) {
            case LOAD, LOADI, STORE, STOREI, CALL, RETURN, POP, JUMPIF -> true;
            default -> false;
        };
    }

    public boolean needsDArg() {
        return switch (this) {
            case LOAD, LOADA, LOADL, STORE, CALL, RETURN, PUSH, POP, JUMP, JUMPIF -> true;
            default -> false;
        };
    }

    public boolean needsRArg() {
        return switch (this) {
            case LOAD, LOADA, STORE, CALL, JUMP, JUMPIF -> true;
            default -> false;
        };
    }
}

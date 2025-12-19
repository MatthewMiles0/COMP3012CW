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
    UNUSED, // for ordinal op code. also used as a placeholder instruction
    PUSH,
    POP,
    JUMP,
    JUMPI,
    JUMPIF,
    HALT;

    public int getOpCode() {
        return ordinal();
    }

    public boolean allowsNArg() {
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

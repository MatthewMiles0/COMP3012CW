package uk.ac.nott.cs.comp3012.coursework.tam;

public enum TamPrimitive {
    ID,
    NOT,
    AND,
    OR,
    SUCC,
    PRED,
    NEG,
    ADD,
    SUB,
    MULT,
    DIV,
    MOD,
    LT,
    LE,
    GE,
    GT,
    EQ,
    NE,
    EOL,
    EOF,
    GET,
    PUT,
    GETEOL,
    PUTEOL,
    GETINT,
    PUTINT,
    NEW,
    DISPOSE;

    public int getDistanceFromPB() {
        return ordinal()+1;
    }
}

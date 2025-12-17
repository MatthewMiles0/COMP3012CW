package uk.ac.nott.cs.comp3012.coursework.tam;

public enum TamRegister {
    CB, // Code Base
    CT, // Code Top
    PB, // Primitives Base
    PT, // Primitives Top
    SB, // Stack Base
    ST, // Stack Top
    HB, // Heap Base
    HT, // Heap Top
    LB, // Local Base
    L1, // Local Base 1
    L2, // Local Base 2
    L3, // Local Base 3
    L4, // Local Base 4
    L5, // Local Base 5
    L6, // Local Base 6
    CP; // Code Pointer

    public int getRegisterNumber() {
        return ordinal();
    }
}

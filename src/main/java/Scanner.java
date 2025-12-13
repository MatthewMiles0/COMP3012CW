public class Scanner {
    enum TokenType {
        KW_ALLOCATE,
        KW_BREAK,
        KW_CALL,
        KW_CHARACTER,
        KW_DEALLOCATE,
        KW_DO,
        KW_ELSE,
        KW_END,
        KW_FUNCTION,
        KW_IF,
        KW_INTEGER,
        KW_LOGICAL,
        KW_POINTER,
        KW_PROGRAM,
        KW_READ,
        KW_REAL,
        KW_RESULT,
        KW_SUBROUTINE,
        KW_THEN,
        KW_TYPE,
        KW_WHILE,
        KW_WRITE,

        NAME,

        L_INT,      // 1        +2      -3      b"0101"     o"760"      z"ab23"
        L_REAL,     // 1.1      +2.2    3.      -.4
        L_LOGICAL,  // .true.   .false.
        L_CHAR,     // "she said ""hello""."

        L_BRACKET,
        R_BRACKET,
        COMMA,

        LT,
        GT,
        LE,
        GE,
        EQ,

        AND,
        OR,
        NOT,

        PERCENT,
        SLASH,
        ASTERISK,
        MINUS,
        PLUS,
        ASSIGN,

        DOUBLE_ASTERISK,
        DOUBLE_SLASH,
        DOUBLE_COLON,

        EOF
    }



    public Scanner(String input) {
        System.out.println(input);
    }


}

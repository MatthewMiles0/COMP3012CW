package uk.ac.nott.cs.comp3012.coursework.ast;

public class AstVisitor<T> {
    public T visit(Ast ast) {
        return switch (ast) {
            case Int i -> visitInt(i);
            case Real r -> visitReal(r);
            case Str s -> visitString(s);
            case Bool b -> visitBool(b);
            case IfSt is -> visitIfSt(is);
            case ReadSt rs -> visitReadSt(rs);
            case WriteSt ws -> visitWriteSt(ws);
        };
    }

    public T visitInt(Int i) {
        return null;
    }

    public 
}

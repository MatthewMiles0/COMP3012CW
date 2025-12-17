package uk.ac.nott.cs.comp3012.coursework.ast;

public class AstVisitor<T> {
    public T visit(Ast ast) {
        return switch (ast) {
            case Program p -> visitProgram(p);
            case Block b -> visitBlock(b);
            case BinOp bo -> visitBinOp(bo);
            case VarRef vr -> visitVarRef(vr);
            case Int i -> visitInt(i);
            case Real r -> visitReal(r);
            case Str s -> visitString(s);
            case Bool b -> visitBool(b);
            case IfSt is -> visitIfSt(is);
            case ReadSt rs -> visitReadSt(rs);
            case WriteSt ws -> visitWriteSt(ws);
            default -> throw new IllegalStateException("Unexpected value: " + ast);
        };
    }

    public T visitProgram(Program p) {
        return null;
    }

    public T visitBlock(Block b) {
        return null;
    }

    public T visitBinOp(BinOp bo) {
        return null;
    }

    public T visitVarRef(VarRef vr) {
        return null;
    }

    public T visitInt(Int i) {
        return null;
    }

    public T visitReal(Real r) {
        return null;
    }

    public T visitString(Str s) {
        return null;
    }

    public T visitBool(Bool b) {
        return null;
    }

    public T visitIfSt(IfSt is) {
        return null;
    }

    public T visitReadSt(ReadSt rs) {
        return null;
    }

    public T visitWriteSt(WriteSt ws) {
        return null;
    }
}

package uk.ac.nott.cs.comp3012.coursework.ast;

public class AstVisitor<T> {
    public T visit(Ast ast) {
//        System.out.println("                             > " + ast);
        T ret = switch (ast) {
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
            case Subroutine s -> visitSubroutine(s);
            case Function f -> visitFunction(f);
            case InitVar iv -> visitInitVar(iv);
            case DoLoop dl -> visitDoLoop(dl);
            case DoWhile dw -> visitDoWhile(dw);
            case Assignment a -> visitAssignment(a);
            default -> throw new IllegalStateException("Unexpected value: " + ast);
        };
//        System.out.println( "                            <  " + ast);
        return ret;
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

    public T visitSubroutine(Subroutine s) {
        return null;
    }

    public T visitFunction(Function f) {
        return null;
    }

    public T visitInitVar(InitVar iv) {
        return null;
    }

    public T visitDoLoop(DoLoop dl) {
        return null;
    }

    public T visitDoWhile(DoWhile dw) {
        return null;
    }

    public T visitAssignment(Assignment a) {
        return null;
    }
}

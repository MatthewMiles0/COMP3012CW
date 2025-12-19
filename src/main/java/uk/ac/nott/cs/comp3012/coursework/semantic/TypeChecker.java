package uk.ac.nott.cs.comp3012.coursework.semantic;

import uk.ac.nott.cs.comp3012.coursework.ast.*;
import uk.ac.nott.cs.comp3012.coursework.exceptions.SemanticException;
import uk.ac.nott.cs.comp3012.coursework.symbol.SymbolTable;

public class TypeChecker extends AstVisitor<Type> {
    private SymbolTable table;

    public TypeChecker(SymbolTable table) {
        this.table = table;
    }

    public void check(Ast ast) {
        visit(ast);
    }

    @Override
    public Type visitProgram(Program p) {
        visit(p.block());
        return null;
    }

    @Override
    public Type visitBlock(Block b) {
//        System.out.println("Visiting block: " + b);
        for (var initVar : b.varInits()) {
            visit(initVar);
        }
        for (var statement : b.statements()) {
//            System.out.println("Visiting statement: " + statement);
            visit(statement);
        }
        return null;
    }

    @Override
    public Type visitIfSt(IfSt is) {
        visit(is.condition());
        visit(is.thenBlock());
        visit(is.elseBlock());
        return null;
    }

    @Override
    public Type visitBinOp(BinOp bo) {
        Type leftType = visit(bo.left());
        Type rightType = visit(bo.right());
//        System.out.println("TC: " + leftType + " " + rightType);
        if (!leftType.equals(rightType)) {
            throw new SemanticException("Incompatible types: " + leftType + " and " + rightType);
        }
        return leftType;
    }

    @Override
    public Type visitDoLoop(DoLoop dl) {
        visit(dl.init());
        visit(dl.max());
        visit(dl.block());
        return null;
    }

    @Override
    public Type visitInt(Int i) {
        return new Type(BaseType.INT, null, 0);
    }

    @Override
    public Type visitBool(Bool b) {
        return new Type(BaseType.BOOL, null, 0);
    }

    @Override
    public Type visitString(Str s) {
        return new Type(BaseType.STR, null, 0);
    }

    @Override
    public Type visitReal(Real r) {
        return new Type(BaseType.REAL, null, 0);
    }

    @Override
    public Type visitVarRef(VarRef vr) {
        return table.lookup(vr.name()).type();
    }
}

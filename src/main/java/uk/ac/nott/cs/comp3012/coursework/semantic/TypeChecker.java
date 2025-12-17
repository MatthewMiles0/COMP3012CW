package uk.ac.nott.cs.comp3012.coursework.semantic;

import uk.ac.nott.cs.comp3012.coursework.ast.*;
import uk.ac.nott.cs.comp3012.coursework.exceptions.SemanticException;

public class TypeChecker extends AstVisitor<Type> {
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
        for (var statement : b.statements()) {
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
        System.out.println("TC: " + leftType + " " + rightType);
        if (leftType != rightType) {
            throw new SemanticException("Incompatible types: " + leftType + " and " + rightType);
        }
        return leftType;
    }

    @Override
    public Type visitInt(Int i) {
        return Type.INT;
    }

    @Override
    public Type visitBool(Bool b) {
        return Type.BOOL;
    }

    @Override
    public Type visitString(Str s) {
        return Type.STR;
    }

    @Override
    public Type visitReal(Real r) {
        return Type.REAL;
    }
}

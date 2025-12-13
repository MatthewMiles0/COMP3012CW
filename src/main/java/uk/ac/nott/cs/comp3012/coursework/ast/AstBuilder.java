package uk.ac.nott.cs.comp3012.coursework.ast;

import uk.ac.nott.cs.comp3012.coursework.NottscriptBaseVisitor;
import uk.ac.nott.cs.comp3012.coursework.NottscriptParser;

import java.util.ArrayList;
import java.util.List;

public class AstBuilder extends NottscriptBaseVisitor<Ast> {
    @Override
    public Program visitProgram(NottscriptParser.ProgramContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (var statement : ctx.statement()) {
            statements.add((Statement) visit(statement));
        }
        return new Program(statements);
    }

    @Override
    public Expr visitBinaryExpression(NottscriptParser.BinaryExpressionContext ctx) {
        Expr left = (Expr) visit(ctx.lexp);
        Op op = toOp(ctx.children.get(1).getText());
        Expr right = (Expr) visit(ctx.rexp);
        return new BinOp(left, op, right);
    }

    @Override
    public Expr visitLiteralExpression(NottscriptParser.LiteralExpressionContext ctx) {
        return (Expr) visit(ctx.literal());
    }

    @Override
    public Expr visitNameExpression(NottscriptParser.NameExpressionContext ctx) {
        return (Expr) new VarRef(ctx.NAME().getText());
    }

    @Override
    public Expr visitLiteral(NottscriptParser.LiteralContext ctx) {
        return switch (ctx.getChild(0).getText()) {
            case "true" -> new Bool(true);
            case "false" -> new Bool(false);
            default -> new Int(Integer.parseInt(ctx.getChild(0).getText()));
        };
    }

    private Op toOp(String op) {
        return switch (op) {
            case "<", ".lt." -> Op.LT;
            case ">", ".gt." -> Op.GT;
            case "<=", ".le." -> Op.LE;
            case ">=", ".ge." -> Op.GE;
            case "==", ".eq." -> Op.EQ;
            case "!=", ".neq." -> Op.NEQ;
            case ".and." -> Op.AND;
            case ".or." -> Op.OR;
            case "+" -> Op.ADD;
            case "-" -> Op.SUB;
            case "*" -> Op.MUL;
            case "/" -> Op.DIV;
            case "**" -> Op.POW;
            default -> throw new IllegalStateException("Unexpected operator value: " + op);
        };
    }
}

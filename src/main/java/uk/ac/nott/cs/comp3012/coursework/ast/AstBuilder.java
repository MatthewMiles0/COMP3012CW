package uk.ac.nott.cs.comp3012.coursework.ast;

import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.nott.cs.comp3012.coursework.NottscriptBaseVisitor;
import uk.ac.nott.cs.comp3012.coursework.NottscriptParser;

import java.util.ArrayList;
import java.util.Arrays;
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
        return new BinOp((Expr) visit(ctx.lexp), toOp(ctx.children.get(1).getText()), (Expr) visit(ctx.rexp));
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
    public Expr visitLiteralInt(NottscriptParser.LiteralIntContext ctx) {
        String text = ctx.L_INT().getText();
        return switch (text.charAt(0)) {
            case 'b' -> new Int(Integer.parseInt(text.substring(2), 2));
            case 'o' -> new Int(Integer.parseInt(text.substring(2), 8));
            case 'z' -> new Int(Integer.parseInt(text.substring(2), 16));
            default -> new Int(Integer.parseInt(text));
        };
    }

    @Override
    public Ast visitLiteralLogical(NottscriptParser.LiteralLogicalContext ctx) {
        String text = ctx.L_LOGICAL().getText();
        return switch (text) {
            case ".true." -> new Bool(true);
            case ".false." -> new Bool(false);
            default -> throw new IllegalStateException("Unexpected bool value: " + text);
        };
    }

    @Override
    public Ast visitLiteralChar(NottscriptParser.LiteralCharContext ctx) {
        String text = ctx.L_CHAR().getText();
        text = text.substring(1, text.length() - 1);
        text = text.replace("\"\"", "\"");
        return new Str(text);
    }

    @Override
    public Ast visitLiteralReal(NottscriptParser.LiteralRealContext ctx) {
        String text = ctx.L_REAL().getText();
        return new Real(Double.parseDouble(text));
    }

    @Override
    public Ast visitIf_statement(NottscriptParser.If_statementContext ctx) {
        ArrayList<Statement> ifBlockStatements = new ArrayList<>();
        ifBlockStatements.add((Statement) visit(ctx.statement()));
        return new IfSt((Expr) visit(ctx.expression()), new Block(ifBlockStatements), new Block(new ArrayList<>()));
    }

    @Override
    public Ast visitIf_then_statement(NottscriptParser.If_then_statementContext ctx) {
        return new IfSt((Expr) visit(ctx.expression()), (Block) visit(ctx.thenBlock), (Block) visit(ctx.elseBlock));
    }

    @Override
    public Ast visitBlock(NottscriptParser.BlockContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (var statement : ctx.children) {
            statements.add((Statement) visit(statement));
        }
        return new Block(statements);
    }

    @Override
    public Ast visitWrite(NottscriptParser.WriteContext ctx) {
        return new WriteSt(fetchExpressionList(ctx.expression_list()));
    }

    private List<Expr> fetchExpressionList(NottscriptParser.Expression_listContext ctx) {
        ArrayList<Expr> expressions = new ArrayList<>();
        for (var expr : ctx.expression()) {
            expressions.add((Expr) visit(expr));
        }
        return expressions;
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

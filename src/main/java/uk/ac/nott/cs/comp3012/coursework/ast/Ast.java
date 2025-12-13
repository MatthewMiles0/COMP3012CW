package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

/**
 * Base interface type for all AST classes. Modify it, delete it, or do whatever you want with it.
 */
public interface Ast {}

record Program(List<Statement> statements) implements Ast {}

interface Expr extends Ast {}
enum Op {
    LT, GT, LE, GE, EQ, NEQ,
    AND, OR,
    ADD, SUB, MUL, DIV, POW
}
record BinOp(Expr left, Op op, Expr right) implements Expr {}
record VarRef(String name) implements Expr {}

interface Atom extends Expr {}
record Int(int value) implements Atom {}
record Real(double value) implements Atom {}
record Str(String value) implements Atom {}
record Bool(boolean value) implements Atom {}

interface Statement extends Ast {}
record Block(List<Statement> statements) implements Statement {}
record IfSt(Expr condition, Block thenBlock, Block elseBlock) implements Statement {}


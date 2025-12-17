package uk.ac.nott.cs.comp3012.coursework.ast;

public record BinOp(Expr left, Op op, Expr right) implements Expr {}

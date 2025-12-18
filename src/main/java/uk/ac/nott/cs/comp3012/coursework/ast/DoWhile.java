package uk.ac.nott.cs.comp3012.coursework.ast;

public record DoWhile(Expr expr, Block block) implements Statement {}

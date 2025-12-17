package uk.ac.nott.cs.comp3012.coursework.ast;

public record IfSt(Expr condition, Block thenBlock, Block elseBlock) implements Statement {}

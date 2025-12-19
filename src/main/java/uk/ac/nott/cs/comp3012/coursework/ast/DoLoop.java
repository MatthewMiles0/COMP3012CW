package uk.ac.nott.cs.comp3012.coursework.ast;

public record DoLoop(Assignment init, Expr max, Expr step, Block block) implements Statement {}

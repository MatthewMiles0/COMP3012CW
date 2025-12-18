package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record CallSubroutine(String name, List<Expr> args) implements Statement {}

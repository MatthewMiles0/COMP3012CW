package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record VarRef(String name, String derType, List<Expr> indices) implements Expr {}

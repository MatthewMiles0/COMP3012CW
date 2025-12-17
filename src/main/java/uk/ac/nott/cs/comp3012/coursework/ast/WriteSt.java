package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record WriteSt(List<Expr> expr) implements Statement {}

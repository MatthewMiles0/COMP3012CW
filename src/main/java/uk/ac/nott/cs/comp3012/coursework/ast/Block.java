package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record Block(List<InitVar> varInits, List<Statement> statements) implements Ast {}

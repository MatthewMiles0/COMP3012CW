package uk.ac.nott.cs.comp3012.coursework.ast;

import uk.ac.nott.cs.comp3012.coursework.semantic.Type;

public record InitVar(VarRef vr, Type type) implements Ast {}

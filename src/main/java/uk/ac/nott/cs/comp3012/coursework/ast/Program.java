package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record Program(String name, Block block) implements Ast {}

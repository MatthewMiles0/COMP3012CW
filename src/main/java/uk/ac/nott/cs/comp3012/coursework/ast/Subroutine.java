package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record Subroutine(String name, List<String> args, Block block) implements Ast {}

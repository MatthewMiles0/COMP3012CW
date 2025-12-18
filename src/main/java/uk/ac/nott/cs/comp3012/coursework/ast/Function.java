package uk.ac.nott.cs.comp3012.coursework.ast;

import java.util.List;

public record Function(String name, List<String> args, String resName, Block block) implements Ast {}

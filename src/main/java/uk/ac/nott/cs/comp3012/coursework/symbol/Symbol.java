package uk.ac.nott.cs.comp3012.coursework.symbol;

import uk.ac.nott.cs.comp3012.coursework.semantic.Type;

public record Symbol (String name, SymbolKind kind, Type type) { }

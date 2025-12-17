package uk.ac.nott.cs.comp3012.coursework.symbol;

import uk.ac.nott.cs.comp3012.coursework.exceptions.SyntaxException;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> table = new HashMap<>();

    public void define(Symbol symbol) {
        if (table.containsKey(symbol.name())) {
            throw new SyntaxException("Duplicate symbol definition: " + symbol.name());
        }

        table.put(symbol.name(), symbol);
    }

    public Symbol lookup(String name) {
        if (!table.containsKey(name)) {
            throw new SyntaxException("Undefined symbol: " + name);
        }

        return table.get(name);
    }

    public boolean exists(String name) {
        return table.containsKey(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var entry : table.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}

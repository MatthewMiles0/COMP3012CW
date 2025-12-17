package uk.ac.nott.cs.comp3012.coursework.symbol;

import uk.ac.nott.cs.comp3012.coursework.ast.Ast;
import uk.ac.nott.cs.comp3012.coursework.ast.AstVisitor;
import uk.ac.nott.cs.comp3012.coursework.ast.Program;

public class SymbolTableBuilder extends AstVisitor<Void> {
    private final SymbolTable table = new SymbolTable();

    public SymbolTable build(Ast ast) {
        visit(ast);
        return table;
    }

    @Override
    public Void visitProgram(Program p) {
        Symbol programSymbol = new Symbol(
            p.name(),
            SymbolType.PROGRAM
        );
        table.define(programSymbol);
        return null;
    }
}

package uk.ac.nott.cs.comp3012.coursework.symbol;

import uk.ac.nott.cs.comp3012.coursework.ast.*;
import uk.ac.nott.cs.comp3012.coursework.semantic.Type;

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
            SymbolKind.PROGRAM,
            null
        );
        table.define(programSymbol);
        visit(p.block());
        return null;
    }

    @Override
    public Void visitBlock(Block b) {
        for (var initVar : b.varInits()) {
            visit(initVar);
        }
        for (var s : b.statements()) {
            visit(s);
        }
        return null;
    }

    @Override
    public Void visitIfSt(IfSt is) {
        visit(is.condition());
        visit(is.thenBlock());
        visit(is.elseBlock());
        return null;
    }

    @Override
    public Void visitDoLoop(DoLoop dl) {
        visit(dl.init());
        visit(dl.max());
        visit(dl.block());
        return null;
    }

    @Override
    public Void visitDoWhile(DoWhile dw) {
        visit(dw.expr());
        visit(dw.block());
        return null;
    }

    @Override
    public Void visitInitVar(InitVar iv) {
        table.define(
            new Symbol(
                iv.vr().name(),
                SymbolKind.VARIABLE,
                iv.type()
            )
        );
        return null;
    }
}

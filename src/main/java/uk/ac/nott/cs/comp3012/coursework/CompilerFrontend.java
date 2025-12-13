package uk.ac.nott.cs.comp3012.coursework;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.nott.cs.comp3012.coursework.ast.Ast;
import uk.ac.nott.cs.comp3012.coursework.ast.AstBuilder;

import java.util.List;

public class CompilerFrontend implements Compiler.Frontend {
    @Override
    public Ast runFrontend(String programText) {
        System.out.println("Running frontend");

        System.out.println("=== Program text ===");
        System.out.println(programText);
        System.out.println("=== End program text ===");

        System.out.println("> Lexing");
        NottscriptLexer lexer = new NottscriptLexer(CharStreams.fromString(programText));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        List<Token> tokensList = tokens.getTokens();

        System.out.println(tokensList.size() + " tokens:");
        for (Token token : tokensList) {
            System.out.println(NottscriptLexer.VOCABULARY.getSymbolicName(token.getType()) + "(" + token.getText() + ")");
        }

        System.out.println("> Parsing");
        NottscriptParser parser = new NottscriptParser(tokens);
        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));

        System.out.println("> AST building");
        AstBuilder builder = new AstBuilder();
        Ast ast = builder.visit(tree);
        System.out.println(ast);

        System.out.println("Frontend done");
        return null;
    }
}

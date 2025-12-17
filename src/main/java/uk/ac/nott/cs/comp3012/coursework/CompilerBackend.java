package uk.ac.nott.cs.comp3012.coursework;

import uk.ac.nott.cs.comp3012.coursework.ast.Ast;

public class CompilerBackend implements Compiler.Backend {
    @Override
    public byte[] runBackend(Ast program) {
        System.out.println(">>> Running backend");

        System.out.println(">>> Backend done");
        return new byte[0];
    }
}

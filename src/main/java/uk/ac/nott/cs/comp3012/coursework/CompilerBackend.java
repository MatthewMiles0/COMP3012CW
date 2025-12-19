package uk.ac.nott.cs.comp3012.coursework;

import uk.ac.nott.cs.comp3012.coursework.ast.Ast;
import uk.ac.nott.cs.comp3012.coursework.semantic.TypeChecker;
import uk.ac.nott.cs.comp3012.coursework.symbol.SymbolTable;
import uk.ac.nott.cs.comp3012.coursework.symbol.SymbolTableBuilder;
import uk.ac.nott.cs.comp3012.coursework.tam.TamInstructionBuilder;

import java.util.ArrayList;
import java.util.List;

public class CompilerBackend implements Compiler.Backend {
    @Override
    public byte[] runBackend(Ast program) {
        System.out.println(">>> Running backend");

        System.out.println("> Creating Symbol Table");
        SymbolTable symbolTable = new SymbolTableBuilder().build(program);
        System.out.println(symbolTable);

        System.out.println("> Checking types");
        new TypeChecker(symbolTable).check(program);

        System.out.println("> Generating TAM Instructions");
        TamInstructionBuilder builder = new TamInstructionBuilder(symbolTable);
        var instructions = builder.build(program);

        StringBuilder sb = new StringBuilder();
        for (var instruction : instructions) {
            sb.append(instruction).append("\n");
        }
        System.out.println("TAM Output:");
        System.out.println(sb);

        System.out.println("> Converting TAM Instructions to byte array");
        List<byte[]> instBytes = new ArrayList<>();
        for (var instruction : instructions) {
            instBytes.add(instruction.toByteArray());
        }
        int size = 0;
        for (var b : instBytes) {
            size += b.length;
        }
        byte[] output = new byte[size];
        int pos = 0;
        for (var b : instBytes) {
            System.arraycopy(b, 0, output, pos, b.length);
            pos += b.length;
        }

        System.out.println(">>> Backend done");
        return output;
    }
}

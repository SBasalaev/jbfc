/*
 * Java brainfuck compiler.
 *  Copyright (C) 2011, Sergey Basalaev <sbasalaev@gmail.com>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 */

package org.sbasalaev.bfc.writers;

import org.sbasalaev.bfc.Options;
import org.sbasalaev.bfc.tree.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Writes BF tree to the Java class.
 * 
 * @author Sergey Basalaev
 */
public class javaWriter implements ExtendedTreeVisitor<Void, Integer> {

	private Options options;
	private PrintStream output;
	private int lastline = 0;

	public javaWriter() { }

	public void setOptions(Options options) {
		this.options = options;
	}

	public Void visitUnit(UnitTree tree) {
		String name = options.getOutputName();
		if (name == null) {
			name = tree.getSource();
			int dot = name.lastIndexOf('.');
			if (dot > 0) name = name.substring(0, dot);
		}
		File file = new File(options.getOutputDir(), name+".java");
		try {
			output = new PrintStream(file);
		} catch (IOException ioe) {
			//cry like a baby
		}
		if (options.needDebugInfo()) {
			output.println("/* Generated from \""+tree.getSource()+"\" */");
		}
		output.println();
		output.println("import java.io.IOException;");
		output.println();
		output.println("public class "+name+" {");
		output.println("\tpublic static void main(String[] args) throws IOException {");
		output.println("\t\tfinal int ARRAY_SIZE = "+options.getRange()+";");
		output.println("\t\tbyte[] array = new byte[ARRAY_SIZE];");
		output.println("\t\tint position = 0;");
		output.println("\t\tint ch;");
		for (Tree t : tree.getChildren()) {
			t.accept(this, 2);
		}
		output.println("\t\tSystem.out.flush();");
		output.println("\t}");
		output.println("}");
		output.flush();
		output.close();
		System.err.println("File "+name+".java written");
		return null;
	}

	public Void visitLoop(LoopTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("while (array[position] != 0) {");
		for (Tree t : tree.getChildren()) {
			t.accept(this, level+1);
		}
		indent(level);
		output.println("}");
		return null;
	}

	public Void visitInc(IncTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		if (tree.getIncrement() > 0) {
			output.println("array[position] += "+tree.getIncrement()+';');
		} else {
			output.println("array[position] -= "+-tree.getIncrement()+';');
		}
		return null;
	}

	public Void visitMove(MoveTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		if (tree.getOffset() > 0) {
			output.println("position += "+tree.getOffset()+';');
		} else {
			output.println("position -= "+-tree.getOffset()+';');
		}
		return null;
	}

	public Void visitGetChar(GetCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("ch = System.in.read();");
		indent(level);
		output.println("array[position] = ch >= 0 ? (byte)ch : 0;");
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("System.out.write(array[position]);");
		return null;
	}

	public Void visitAssign(AssignTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("array[position] = (byte)"+tree.getValue()+';');
		return null;
	}

	public Void visitOther(Tree tree, Integer level) {
		throw new RuntimeException("Unknown tree type: "+tree.getClass());
	}

	private void indent(int level) {
		while (level > 0) {
			output.write('\t');
			level--;
		}
	}

	private void addLineComment(Tree tree, int level) {
		if (lastline < tree.getSourcePosition().lineNumber()) {
			lastline = tree.getSourcePosition().lineNumber();
			indent(level);
			output.println("// line "+lastline);
		}
	}
}

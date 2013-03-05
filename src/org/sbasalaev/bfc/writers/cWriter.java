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
 * Writer to C code.
 * @author Sergey Basalaev
 */
public class cWriter implements ExtendedTreeVisitor<Void, Integer> {


	private Options options;
	private PrintStream output;
	private String source;
	private int lastline = 0;

	public cWriter() { }

	public void setOptions(Options options) {
		this.options = options;
	}

	public Void visitUnit(UnitTree tree) {
		String name = options.getOutputName();
		source = tree.getSource();
		if (name == null) {
			name = source;
			int dot = name.lastIndexOf('.');
			if (dot > 0) name = name.substring(0, dot);
		}
		File file = new File(options.getOutputDir(), name+".c");
		try {
			output = new PrintStream(file);
		} catch (IOException ioe) {
			//cry like a baby
		}
		if (options.needDebugInfo()) {
			output.println("/* Generated from \"" + source + "\" */");
		}
		output.println();
		output.println("#include <stdio.h>");
		output.println();
		output.print("#define ARRAY_SIZE   ");
		output.println(options.getRange());
		output.println();
		output.println("int main(int argc, char* argv[]) {");
		output.println("\tchar array[ARRAY_SIZE];");
		output.println("\tint position = 0;");
		output.println("\tchar ch;");
		for (Tree t : tree.getChildren()) {
			t.accept(this, 1);
		}
		output.println("}");
		output.flush();
		output.close();
		System.err.println("File "+name+".c written");
		return null;
	}

	public Void visitLoop(LoopTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		output.println("while (array[position]) {");
		for (Tree t : tree.getChildren()) {
			t.accept(this, level+1);
		}
		indent(level);
		output.println("}");
		return null;
	}

	public Void visitInc(IncTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		if (tree.getIncrement() > 0) {
			output.println("array[position] += "+tree.getIncrement()+';');
		} else {
			output.println("array[position] -= "+-tree.getIncrement()+';');
		}
		return null;
	}

	public Void visitMove(MoveTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		if (tree.getOffset() > 0) {
			output.println("position += "+tree.getOffset()+';');
		} else {
			output.println("position -= "+-tree.getOffset()+';');
		}
		return null;
	}

	public Void visitGetChar(GetCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		output.println("ch = getchar();");
		indent(level);
		output.println("array[position] = ch >= 0 ? ch : 0;");
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		output.println("putchar(array[position]);");
		return null;
	}

	public Void visitAssign(AssignTree tree, Integer level) {
		if (options.needDebugInfo()) addLine(tree, level);
		indent(level);
		output.println("array[position] = "+tree.getValue()+';');
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

	private void addLine(Tree tree, int level) {
		if (lastline < tree.getSourcePosition().lineNumber()) {
			lastline = tree.getSourcePosition().lineNumber();
			indent(level);
			output.println("#line " + lastline + " \"" + source + '"');
		}
	}
}

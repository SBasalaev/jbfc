package home.bfc.writers;

import home.bfc.Options;
import home.bfc.tree.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Writer to C code.
 * @author Sergey Basalaev
 */
public class cWriter implements TreeVisitor<Void, Integer> {


	private Options options;
	private PrintStream output;
	private int lastline = 0;

	public cWriter() { }

	public void setOptions(Options options) {
		this.options = options;
	}

	public Void visitUnit(UnitTree tree) {
		String name = options.getOutputName();
		if (name == null) name = "main";
		File file = new File(options.getOutputDir(), name+".c");
		try {
			output = new PrintStream(file);
		} catch (IOException ioe) {
			//cry like a baby
		}
		if (options.needDebugInfo()) {
			output.println("/* Compiled from \""+tree.getSource()+"\" */");
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
		for (Tree t : tree.getChildren()) {
			t.accept(this, 1);
		}
		output.println("}");
		return null;
	}

	public Void visitLoop(LoopTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
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
		output.println("ch = getchar();");
		indent(level);
		output.println("array[position] = ch >= 0 ? ch : 0;");
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("putchar(array[position]);");
		return null;
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
			output.println("/* line "+lastline+" */");
		}
	}
}
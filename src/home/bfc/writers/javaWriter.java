package home.bfc.writers;

import home.bfc.Options;
import home.bfc.tree.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Writes BF tree to the Java class.
 * 
 * @author Sergey Basalaev
 */
public class javaWriter implements TreeVisitor<Void, Integer> {

	private Options options;
	private PrintStream output;
	private int lastline = 0;

	public javaWriter() { }

	public void setOptions(Options options) {
		this.options = options;
	}

	public Void visitUnit(UnitTree tree) {
		String name = options.getOutputName();
		if (name == null) name = "Main";
		File file = new File(options.getOutputDir(), name+".java");
		try {
			output = new PrintStream(file);
		} catch (IOException ioe) {
			//cry like a baby
		}
		if (options.needDebugInfo()) {
			output.println("/* Compiled from \""+tree.getSource()+"\" */");
		}
		output.println();
		output.println("import java.io.IOException;");
		output.println();
		output.println("public class "+name+" {");
		output.println("\tpublic static void main(String[] args) throws IOException {");
		output.println("\t\tfinal int ARRAY_SIZE = "+options.getRange()+";");
		output.println("\t\tbyte[] array = new byte[ARRAY_SIZE];");
		output.println("\t\tint position = 0;");
		for (Tree t : tree.getChildren()) {
			t.accept(this, 2);
		}
		output.println("\t\tSystem.out.flush();");
		output.println("\t}");
		output.println("}");
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
		output.println("array[position] = ch >= 0 ? ch : 0;");
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Integer level) {
		if (options.needDebugInfo()) addLineComment(tree, level);
		indent(level);
		output.println("System.out.write(array[position]);");
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
			output.println("// line "+lastline);
		}
	}
}

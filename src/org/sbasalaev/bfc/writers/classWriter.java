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

import org.objectweb.asm.*;
import static org.objectweb.asm.Opcodes.*;
import org.sbasalaev.bfc.Options;
import org.sbasalaev.bfc.tree.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.objectweb.asm.MethodVisitor;

/**
 * Writes BF tree to Java class file.
 * @author Sergey Basalaev
 */
public class classWriter implements ExtendedTreeVisitor<Void, Void> {

	private Options options;
	private MethodVisitor main;
	private int lastline = 0;

	private static final int JAVA_1_5 = 49;

	private final static int varArray = 1;
	private final static int varPosition = 2;

	public classWriter() { }

	public void setOptions(Options options) {
		this.options = options;
	}

	public Void visitUnit(UnitTree tree) {
		ClassWriter clz = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String name = options.getOutputName();
		if (name == null) {
			name = tree.getSource();
			int dot = name.lastIndexOf('.');
			if (dot > 0) name = name.substring(0, dot);
		}
		clz.visit(JAVA_1_5, ACC_PUBLIC | ACC_FINAL, name, null, "java/lang/Object", null);
		if (options.needDebugInfo()) clz.visitSource(tree.getSource(), null);
		main = clz.visitMethod(
				ACC_STATIC | ACC_PUBLIC,
				"main",
				"([Ljava/lang/String;)V",
				null,
				new String[] {"java/io/IOException"});
		visitIntConst(options.getRange());
		main.visitIntInsn(NEWARRAY, T_BYTE);
		main.visitVarInsn(ASTORE, varArray);
		main.visitInsn(ICONST_0);
		main.visitVarInsn(ISTORE, varPosition);
		Label startVars = new Label();
		main.visitLabel(startVars);
		for (Tree t : tree.getChildren()) {
			t.accept(this, null);
		}
		Label endVars = new Label();
		main.visitLabel(endVars);
		if (options.needDebugInfo()) {
			main.visitLocalVariable("array", "[B", null, startVars, endVars, varArray);
			main.visitLocalVariable("position", "I", null, startVars, endVars, varPosition);
		}
		main.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "flush", "()V");
		main.visitInsn(RETURN);
		main.visitMaxs(0, 0);
		main.visitEnd();
		clz.visitEnd();
		//writing
		try {
			File outfile = new File(options.getOutputDir(), name+".class");
			OutputStream out = new FileOutputStream(outfile);
			out.write(clz.toByteArray());
			out.flush();
			out.close();
			System.err.println("File "+name+".class written");
		} catch (IOException ioe) {
			//cry like a baby
		}
		return null;
	}

	public Void visitInc(IncTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		main.visitVarInsn(ALOAD, varArray);
		main.visitVarInsn(ILOAD, varPosition);
		main.visitInsn(DUP2);
		main.visitInsn(BALOAD);
		visitIntConst(tree.getIncrement());
		main.visitInsn(IADD);
		main.visitInsn(I2B);
		main.visitInsn(BASTORE);
		return null;
	}

	public Void visitMove(MoveTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		main.visitIincInsn(varPosition, tree.getOffset());
		return null;
	}

	public Void visitGetChar(GetCharTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		Label notEOF = new Label();
		main.visitVarInsn(ALOAD, varArray);
		main.visitVarInsn(ILOAD, varPosition);
		main.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
		main.visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I");
		main.visitInsn(DUP);
		main.visitJumpInsn(IFGE, notEOF);
		main.visitInsn(POP);
		main.visitInsn(ICONST_0);
		main.visitLabel(notEOF);
		main.visitInsn(I2B);
		main.visitInsn(BASTORE);
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		main.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		main.visitVarInsn(ALOAD, varArray);
		main.visitVarInsn(ILOAD, varPosition);
		main.visitInsn(BALOAD);
		main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "write", "(I)V");
		return null;
	}

	public Void visitLoop(LoopTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		Label loopStart = new Label();
		Label loopEnd = new Label();
		main.visitLabel(loopStart);
		main.visitVarInsn(ALOAD, varArray);
		main.visitVarInsn(ILOAD, varPosition);
		main.visitInsn(BALOAD);
		main.visitJumpInsn(IFEQ, loopEnd);
		for (Tree t : tree.getChildren()) {
			t.accept(this, data);
		}
		main.visitJumpInsn(GOTO, loopStart);
		main.visitLabel(loopEnd);
		return null;
	}
	
	public Void visitAssign(AssignTree tree, Void data) {
		if (options.needDebugInfo()) markLine(tree);
		main.visitVarInsn(ALOAD, varArray);
		main.visitVarInsn(ILOAD, varPosition);
		visitIntConst(tree.getValue());
		main.visitInsn(I2B);
		main.visitInsn(BASTORE);
		return null;
	}
	
	public Void visitOther(Tree tree, Void data) {
		throw new RuntimeException("Unknown tree type: "+tree.getClass());
	}

	private void visitIntConst(int i) {
		if (i >= -1 && i <= 5) {
			main.visitInsn(Opcodes.ICONST_0 + i);
		} else if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			main.visitIntInsn(Opcodes.BIPUSH, i);
		} else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			main.visitIntInsn(Opcodes.SIPUSH, i);
		} else {
			main.visitLdcInsn(i);
		}
	}

	private void markLine(Tree tree) {
		if (lastline < tree.getSourcePosition().lineNumber()) {
			lastline = tree.getSourcePosition().lineNumber();
			Label mark = new Label();
			main.visitLabel(mark);
			main.visitLineNumber(lastline, mark);
		}
	}
}

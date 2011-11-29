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

package org.sbasalaev.bfc;

import org.sbasalaev.bfc.tree.*;
import org.sbasalaev.bfc.opt.SizeCalculator;
import org.sbasalaev.bfc.opt.Simplifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BF compiler.
 * @author Sergey Basalaev
 */
public class Main {

	private Main() { }

	/* Modes when reading options. */
	static private final int NORMAL = 0;
	static private final int EXPECT_NAME = 1;
	static private final int EXPECT_DIR = 2;
	static private final int EXPECT_TARGET = 3;
	static private final int EXPECT_BFCLASS = 4;
	static private final int EXPECT_RANGE = 5;

	static private final String VERSION =
			"BF compiler version 0.2";

	static private final String HELP =
			"Usage: jbfc [-options] source\n" +
			"where possible options are:\n" +
			"  -o <name>                       Use given output name to generate files\n" +
			"  -t, -target <name>              Generate file for given target\n" +
			"  -vc, -visitorclass <classname>  Apply specified visitor to BF tree\n" +
			"  -d <directory>                  Specify where to place generated files\n" +
			"  -r, -range <number>             Specify max array size\n" +
			"  -g                              Generate debugging info\n" +
			"  -h, -help                       Show this help message and exit\n" +
			"  -v, -version                    Show program version and exit";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		//parsing arguments
		Options options = new Options();
		options.debug = false;
		options.outputDir = new File(System.getProperty("user.dir"));
		options.range = 30000;
		int mode = NORMAL;
		List<String> classNames = new ArrayList<String>();
		File inputFile = null;
		for (String arg : args) {
			switch (mode) {
				case EXPECT_NAME:
					mode = NORMAL;
					options.outputName = arg;
					break;
				case EXPECT_DIR:
					mode = NORMAL;
					options.outputDir = new File(arg);
					break;
				case EXPECT_TARGET:
					mode = NORMAL;
					classNames.add("org.sbasalaev.bfc.writers."+arg+"Writer");
					break;
				case EXPECT_BFCLASS:
					mode = NORMAL;
					classNames.add(arg);
					break;
				case EXPECT_RANGE:
					mode = NORMAL;
					int r;
					try {
						r = Integer.parseInt(arg);
						if (r < 0) {
							System.err.println("Option -range expects positive integer");
							System.exit(1);
						}
						options.range = r;
					} catch (NumberFormatException nfe) {
						System.err.println("Option -range expects positive integer");
						System.exit(1);
					}
					break;
				default:
					if (arg.equals("-h") || arg.equals("-help")) {
						System.out.println(HELP);
						System.exit(0);
					} else if (arg.equals("-v") || arg.equals("-version")) {
						System.out.println(VERSION);
						System.exit(0);
					} else if (arg.equals("-d")) {
						mode = EXPECT_DIR;
					} else if (arg.equals("-t") || arg.equals("-target")) {
						mode = EXPECT_TARGET;
					} else if (arg.equals("-vc") || arg.equals("-visitorclass")) {
						mode = EXPECT_BFCLASS;
					} else if (arg.equals("-o")) {
						mode = EXPECT_NAME;
					} else if (arg.equals("-g")) {
						options.debug = true;
					} else if (arg.startsWith("-")) {
						System.err.println("Error: Unknown option: "+arg);
						System.exit(-1);
					} else if (inputFile != null) {
						System.err.println("Error: Multiple sources specified");
					} else {
						inputFile = new File(arg);
					}
			} /* switch */
		}
		//parsing input
		if (inputFile == null) {
			System.err.println("No input given, aborting.");
			System.exit(1);
		}
		UnitTree unit = null;
		try {
			unit = parse(inputFile);
		} catch (IOException ioe) {
			System.err.println("I/O error while reading source");
			System.err.println(ioe);
			System.exit(2);
		} catch (ParseException pe) {
			System.err.println(inputFile.getAbsolutePath()+':'+pe.getSourcePosition().lineNumber()+':'+pe.getMessage());
			System.exit(3);
		}
		// optimizing
		Simplifier sim = new Simplifier();
		sim.setOptions(options);
		sim.visitUnit(unit);
		// calculating array size
		SizeCalculator scalc = new SizeCalculator();
		scalc.setOptions(options);
		int r = scalc.visitUnit(unit);
		if (r >= 0) options.range = r;
		//applying visitors
		options.outputDir.mkdirs();
		if (classNames.isEmpty()) {
			System.err.println("No targets or visitors specified. Exiting.");
			System.exit(0);
		}
		for (String className : classNames) {
			try {
				Class<?> clz = Class.forName(className);
				TreeVisitor<?,?> v = (TreeVisitor<?,?>)clz.newInstance();
				v.setOptions(options);
				v.visitUnit(unit);
			} catch (ClassNotFoundException cnfe) {
				System.err.println("Class not found: "+className);
			} catch (IllegalAccessException iae) {
				System.err.println("Class not accessible: "+className);
			} catch (InstantiationException ie) {
				System.err.println("Class not instantiable: "+className);
			} catch (ClassCastException cce) {
				System.err.println("Not a visitor class: "+className);
			}
		}
    }

	private static UnitTree parse(File f) throws IOException, ParseException {
		UnitTree unit = new UnitTree(f.getName());
		SourceInputStream in = new SourceInputStream(f.getPath(), new FileInputStream(f));
		parseUnit(unit, in);
		in.close();
		return unit;
	}

	private static void parseUnit(UnitTree tree, SourceInputStream in)
			throws IOException, ParseException {
		int ch = in.read();
		List<Tree> children = tree.getChildren();
		while (ch != -1) {
			in.backup();
			switch (ch) {
				case '+':
				case '-':
					children.add(parseInc(in));
					break;
				case '<':
				case '>':
					children.add(parseMove(in));
					break;
				case ',':
					children.add(new GetCharTree(in.position()));
					in.read();
					break;
				case '.':
					children.add(new PutCharTree(in.position()));
					in.read();
					break;
				case '[':
					children.add(parseLoop(in));
					break;
				case ']':
					throw new ParseException(" ] without matching [", in.position());
				default:
					in.read();
			}
			ch = in.read();
		}
	}

	private static LoopTree parseLoop(SourceInputStream in)
			throws IOException, ParseException {
		LoopTree loop = new LoopTree(in.position());
		List<Tree> children = loop.getChildren();
		in.read(); // skip [
		int ch = in.read();
		while (ch != ']') {
			in.backup();
			switch (ch) {
				case '+':
				case '-':
					children.add(parseInc(in));
					break;
				case '<':
				case '>':
					children.add(parseMove(in));
					break;
				case ',':
					children.add(new GetCharTree(in.position()));
					in.read();
					break;
				case '.':
					children.add(new PutCharTree(in.position()));
					in.read();
					break;
				case '[':
					children.add(parseLoop(in));
					break;
				case -1:
					throw new ParseException(" [ without matching ]", loop.getSourcePosition());
				default:
					in.read();
			}
			ch = in.read();
		}
		return loop;
	}

	private static IncTree parseInc(SourceInputStream in) throws IOException {
		SourcePosition pos = in.position();
		int inc = 0;
		while (true) {
			int ch = in.read();
			if (ch == '+') inc++;
			else if (ch == '-') inc--;
			else if (ch == '<' || ch == '>' || ch == '.'
			      || ch == ',' || ch == '[' || ch == ']' || ch == -1) {
				in.backup();
				break;
			}
		}
		return new IncTree(pos, inc);
	}

	private static MoveTree parseMove(SourceInputStream in) throws IOException {
		SourcePosition pos = in.position();
		int offset = 0;
		while (true) {
			int ch = in.read();
			if (ch == '>') offset++;
			else if (ch == '<') offset--;
			else if (ch == '+' || ch == '-' || ch == '.'
			      || ch == ',' || ch == '[' || ch == ']' || ch == -1) {
				in.backup();
				break;
			}
		}
		return new MoveTree(pos, offset);
	}
}

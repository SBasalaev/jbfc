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

import org.sbasalaev.bfc.tree.SourcePosition;
import java.io.File;

/**
 * Compiler options.
 * @author Sergey Basalaev
 */
public final class Options {

	File outputDir;
	String outputName;
	boolean debug;
	int range;

	/** Package constructor. */
	Options() { }

	/**
	 * Returns output name specified by <em>-n</em> option.
	 * If <em>-n</em> option was not given returns <code>null</code>.
	 */
	public String getOutputName() {
		return outputName;
	}

	/**
	 * Returns output directory specified with <em>-d</em> option.
	 * If <em>-d</em> option was not given returns current
	 * working directory.
	 */
	public File getOutputDir() {
		return outputDir;
	}

	/**
	 * Returns <code>true</code> if <em>-g</em> option was given.
	 */
	public boolean needDebugInfo() {
		return debug;
	}

	/**
	 * Returns array size specified with <em>-r</em> option.
	 * If <em>-r</em> option was not given returns default value 30000.
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Print warning on the output
	 * @param pos      position in the source
	 * @param message  warning message
	 */
	public void warn(SourcePosition pos, String message) {
		System.err.println(pos.toString()+": "+message);
	}
}

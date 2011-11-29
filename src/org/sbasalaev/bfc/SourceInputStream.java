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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A stream to read source.
 * Counts lines and has ability to backup
 * single character.
 *
 * @author Sergey Basalaev
 */
class SourceInputStream extends FilterInputStream {

	private static final int NONE = -2;

	private String filename;

	private int nextch = NONE;
	private boolean backup = false;
	private int line = 1;
	private int column = 1;
	private int prevlinesize = 0;

	public SourceInputStream(String filename, InputStream in) {
		super(in);
		this.filename = filename;
	}

	@Override
	public int read() throws IOException {
		if (backup) {
			backup = false;
		} else {
			nextch = in.read();
			if (nextch == '\n') {
				line++;
				prevlinesize = column;
				column = 1;
			} else {
				column++;
			}
		}
		return nextch;
	}

	/**
	 * Returns current position in the source.
	 */
	public SourcePosition position() {
		if (backup) {
			if (column == 1) {
				return new SourcePosition(filename, line-1, prevlinesize);
			} else {
				return new SourcePosition(filename, line, column-1);
			}
		} else {
			return new SourcePosition(filename, line, column);
		}
	}
	
	/**
	 * Causes the last character to be read again.
	 */
	public void backup() {
		backup = true;
	}
}

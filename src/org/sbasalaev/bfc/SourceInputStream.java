/*
 * Java brainfuck compiler.
 *  Copyright (C) 2011, Sergey Basalaev <sbasalaev@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

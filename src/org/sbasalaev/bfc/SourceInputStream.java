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

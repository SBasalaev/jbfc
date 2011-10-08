package home.bfc.tree;

/**
 * Position in the source code.
 * @author Sergey Basalaev
 */
public class SourcePosition {

	private final String filename;
	private final int linenumber;
	private final int colnumber;

	public SourcePosition(String filename, int line, int column) {
		this.filename = filename;
		this.linenumber = line;
		this.colnumber = column;
	}

	public int lineNumber() {
		return linenumber;
	}

	public int columnNumber() {
		return colnumber;
	}

	public String fileName() {
		return filename;
	}

	public String toString() {
		return filename+':'+linenumber+':'+colnumber;
	}
}

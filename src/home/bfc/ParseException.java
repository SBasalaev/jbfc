package home.bfc;

import home.bfc.tree.SourcePosition;

/**
 * Thrown to indicate erroneous code.
 * @author Sergey Basalaev
 */
class ParseException extends Exception {

	private final SourcePosition pos;

	public ParseException(String msg, SourcePosition pos) {
		super(msg);
		this.pos = pos;
	}

	public SourcePosition getSourcePosition() {
		return pos;
	}
}

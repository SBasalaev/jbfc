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

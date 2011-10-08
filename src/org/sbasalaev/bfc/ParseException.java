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

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

package org.sbasalaev.bfc.tree;

/**
 * A tree that moves current position in the array.
 * Corresponding BF instructions are <code>'&lt;'</code>
 * and <code>'&gt;'</code>.
 *
 * @author Sergey Basalaev
 */
public class MoveTree extends Tree {

	private final int offset;

	public MoveTree(SourcePosition pos, int offset) {
		super(pos);
		this.offset = offset;
	}

	/**
	 * Returns value of the offset position moves.
	 */
	public int getOffset() {
		return offset;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitMove(this, data);
	}
}

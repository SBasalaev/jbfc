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
 * BF tree.
 * @author Sergey Basalaev
 */
public abstract class Tree {

	private final SourcePosition pos;

	/**
	 * Constructor for subclasses.
	 *
	 * @param pos the position in the source
	 *   file this tree was constructed from
	 */
	protected Tree(SourcePosition pos) {
		this.pos = pos;
	}

	/**
	 * Returns the position in the source file
	 * this tree was constructed from. May be used by
	 * compilers to generate debugging info.
	 */
	public SourcePosition getSourcePosition() {
		return pos;
	}

	public abstract <R,D> R accept(TreeVisitor<R,D> v, D data);
}

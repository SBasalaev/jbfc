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
 * A tree that increments value at the current position
 * of array. Corresponding BF instructions are
 * <code>'+'</code> and <code>'-'</code>.
 *
 * @author Sergey Basalaev
 */
public class IncTree extends Tree {

	private final int increment;

	/**
	 * Creates new <code>IncTree</code>.
	 * @param pos        position in the source
	 * @param increment  increment value
	 */
	public IncTree(SourcePosition pos, int increment) {
		super(pos);
		this.increment = increment;
	}

	/**
	 * Returns the value of increment.
	 */
	public int getIncrement() {
		return increment;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitInc(this, data);
	}
}

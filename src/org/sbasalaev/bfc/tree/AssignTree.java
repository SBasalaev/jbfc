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
 * A tree that assigns specific value at the current
 * position of array. This kind of tree may appear
 * as result of optimizations.
 *
 * @author Sergey Basalaev
 */
public class AssignTree extends Tree {

	private final int value;

	/**
	 * Creates new <code>AssignTree</code>.
	 * @param pos        position in the source
	 * @param increment  the value to assign
	 */
	public AssignTree(SourcePosition pos, int value) {
		super(pos);
		this.value = value;
	}

	/**
	 * Returns the value to be assigned.
	 */
	public int getValue() {
		return value;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		if (v instanceof ExtendedTreeVisitor) {
			return ((ExtendedTreeVisitor<R,D>)v).visitAssign(this, data);
		} else {
			return v.visitOther(this, data);
		}
	}
}

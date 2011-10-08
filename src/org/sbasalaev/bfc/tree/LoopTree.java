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

import java.util.ArrayList;
import java.util.List;

/**
 * A tree for BF loop.
 * Represents part of BF source enclosed in
 * <code>'['</code> and <code>']'</code>.
 * 
 * @author Sergey Basalaev
 */
public class LoopTree extends Tree {

	private final List<Tree> children = new ArrayList<Tree>();

	/**
	 * Creates new <code>LoopTree</code>.
	 */
	public LoopTree(SourcePosition pos) {
		super(pos);
	}

	/**
	 * Returns a list of children of this tree
	 * which can be freely modified.
	 */
	public List<Tree> getChildren() {
		return children;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitLoop(this, data);
	}
}

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
 * A tree for compilation unit.
 * @author Sergey Basalaev
 */
public class UnitTree {

	private final String source;

	private final List<Tree> children = new ArrayList<Tree>();

	/**
	 * Creates new <code>UnitTree</code>.
	 * @param source  name of the source file
	 */
	public UnitTree(String source) {
		this.source = source;
	}

	/**
	 * Returns name of the source file.
	 * May be used by compilers to generate
	 * debugging info.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Returns a list of children of this tree which then
	 * can be freely modified.
	 */
	public List<Tree> getChildren() {
		return children;
	}

	public <R,D> R accept(TreeVisitor<R,D> v) {
		return v.visitUnit(this);
	}
}

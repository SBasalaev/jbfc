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

package org.sbasalaev.bfc.opt;

import org.sbasalaev.bfc.Options;
import org.sbasalaev.bfc.tree.*;
import java.util.List;

/**
 * Eliminates all from last I/O operation till end.
 * Every visit method returns <code>true</code> if the tree
 * may be removed and <code>false</code> otherwise.
 * Deletion starts from the end and stops on first
 * <code>false</code> result.
 *
 * @author Sergey Basalaev
 */
public class TailCutter implements TreeVisitor<Boolean, Void> {

	public void setOptions(Options options) { }

	public Boolean visitInc(IncTree tree, Void data) {
		return true;
	}

	public Boolean visitMove(MoveTree tree, Void data) {
		return true;
	}

	public Boolean visitGetChar(GetCharTree tree, Void data) {
		return false;
	}

	public Boolean visitPutChar(PutCharTree tree, Void data) {
		return false;
	}

	public Boolean visitLoop(LoopTree tree, Void data) {
		for (Tree child : tree.getChildren()) {
			if (!child.accept(this, data)) return false;
		}
		return true;
	}

	public Boolean visitUnit(UnitTree tree) {
		List<Tree> children = tree.getChildren();
		for (int i = children.size()-1; i >= 0; i--) {
			if (children.get(i).accept(this, null)) {
				children.remove(i);
			} else {
				break;
			}
		}
		return false;
	}
}

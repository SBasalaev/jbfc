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

/**
 * Tries to estimate array size for program.
 * Returns <code>-1</code> if fails to do so.
 * Also does some bound checkings.
 *
 * @author Sergey Basalaev
 */
public class SizeCalculator implements TreeVisitor<Integer, SizeBounds> {

	private Options options;

	public void setOptions(Options options) {
		this.options = options;
	}

	public Integer visitInc(IncTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Changing value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitMove(MoveTree tree, SizeBounds data) {
		data.currentIndex += tree.getOffset();
		// We are not assigning maximum index here.
		// It is assigned if that cell is actually used.
		return null;
	}

	public Integer visitGetChar(GetCharTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Writing value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitPutChar(PutCharTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Reading value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitLoop(LoopTree tree, SizeBounds data) {
		int curIndex = data.currentIndex;
		for (Tree node : tree.getChildren()) {
			node.accept(this, data);
		}
		if (data.currentIndex != curIndex) {
			throw new SizeEstimateException();
		}
		return null;
	}

	public Integer visitUnit(UnitTree tree) {
		SizeBounds bounds = new SizeBounds();
		try {
			for (Tree node : tree.getChildren()) {
				node.accept(this, bounds);
			}
		} catch (SizeEstimateException e) {
			return -1;
		}
		//returned value never exceeds -r
		return Math.min(options.getRange(), bounds.maxIndex+1);
	}
}

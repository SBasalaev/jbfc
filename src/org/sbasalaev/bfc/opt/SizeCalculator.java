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
public class SizeCalculator implements ExtendedTreeVisitor<Integer, SizeBounds> {

	private Options options;

	public void setOptions(Options options) {
		this.options = options;
	}

	public Integer visitInc(IncTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Warning: Changing value out of array bounds");
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
			options.warn(tree.getSourcePosition(), "Warning: Writing value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitPutChar(PutCharTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Warning: Reading value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitAssign(AssignTree tree, SizeBounds data) {
		if (data.currentIndex < 0 || data.currentIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Warning: Writing value out of array bounds");
		data.updateMax();
		return null;
	}

	public Integer visitOther(Tree tree, SizeBounds data) {
		//cannot process unknown tree
		throw new SizeEstimateException();
	}

	public Integer visitLoop(LoopTree tree, SizeBounds data) {
		int curIndex = data.currentIndex;
		if (curIndex < 0 || curIndex >= options.getRange())
			options.warn(tree.getSourcePosition(), "Warning: Reading value out of array bounds");
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

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
import java.util.List;
import java.util.ListIterator;

/**
 * Removes zero increments and shifts.
 * Merges consecutive increments and shifts.
 * Removes trailing shift.
 * @author Sergey Basalaev
 */
public class Simplifier implements TreeVisitor<Void, Void> {

	public void setOptions(Options options) {
		//not needed
	}

	public Void visitInc(IncTree tree, Void data) {
		return null;
	}

	public Void visitMove(MoveTree tree, Void data) {
		return null;
	}

	public Void visitGetChar(GetCharTree tree, Void data) {
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Void data) {
		return null;
	}
	
	public Void visitOther(Tree tree, Void data) {
		return null;
	}

	public Void visitLoop(LoopTree tree, Void data) {
		visitChildren(tree.getChildren());
		return null;
	}

	public Void visitUnit(UnitTree tree) {
		List<Tree> children = tree.getChildren();
		visitChildren(children);
		// remove trailing shift
		if (!children.isEmpty() && children.get(children.size()-1) instanceof MoveTree) {
			children.remove(children.size()-1);
		}
		return null;
	}

	private void visitChildren(List<Tree> children) {
		boolean optimized;
		do {
			optimized = false;
			ListIterator<Tree> iter = children.listIterator();
			while (iter.hasNext()) {
				Tree t1 = iter.next();
				//merge adjacent increments
				if (t1 instanceof IncTree) {
					final IncTree itree = (IncTree)t1;
					if (itree.getIncrement() == 0) {
						iter.remove();
						optimized = true;
						continue;
					}
					if (iter.hasNext()) {
						Tree t2 = iter.next();
						if (t2 instanceof IncTree) {
							int incr = itree.getIncrement() + ((IncTree)t2).getIncrement();
							iter.remove();
							iter.previous();
							iter.set(new IncTree(t1.getSourcePosition(), incr));
							optimized = true;
							continue;
						} else {
							iter.previous();
						}
					}
				//merge adjacent moves
				} else if (t1 instanceof MoveTree) {
					final MoveTree mtree = (MoveTree)t1;
					if (mtree.getOffset() == 0) {
						iter.remove();
						optimized = true;
						continue;
					}
					if (iter.hasNext()) {
						Tree t2 = iter.next();
						if (t2 instanceof MoveTree) {
							int ofs = mtree.getOffset() + ((MoveTree)t2).getOffset();
							iter.remove();
							iter.previous();
							iter.set(new MoveTree(t1.getSourcePosition(), ofs));
							optimized = true;
							continue;
						} else {
							iter.previous();
						}
					}
				//process inside loop
				} else if (t1 instanceof LoopTree) {
					t1.accept(this, null);
					// if loop is [-] or [+] replace it with AssignTree(0)
					List<Tree> nodes = ((LoopTree)t1).getChildren();
					if (nodes.size() == 1) {
						Tree child = nodes.get(0);
						if (child instanceof IncTree && ((IncTree)child).getIncrement() %2 != 0) {
							iter.set(new AssignTree(t1.getSourcePosition(), 0));
							optimized = true;
							continue;
						} else if (child instanceof AssignTree && ((AssignTree)child).getValue() == 0) {
							iter.set(new AssignTree(t1.getSourcePosition(), 0));
							optimized = true;
							continue;
						}
					}
				}
			}
		} while (optimized);
	}
}

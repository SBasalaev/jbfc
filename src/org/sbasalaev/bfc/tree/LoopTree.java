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

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

/**
 * A tree that moves current position in the array.
 * Corresponding BF instructions are <code>'&lt;'</code>
 * and <code>'&gt;'</code>.
 *
 * @author Sergey Basalaev
 */
public class MoveTree extends Tree {

	private final int offset;

	public MoveTree(SourcePosition pos, int offset) {
		super(pos);
		this.offset = offset;
	}

	/**
	 * Returns value of the offset position moves.
	 */
	public int getOffset() {
		return offset;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitMove(this, data);
	}
}

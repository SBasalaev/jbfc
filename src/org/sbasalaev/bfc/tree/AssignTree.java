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

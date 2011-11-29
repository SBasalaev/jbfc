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

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

import org.sbasalaev.bfc.Options;

/**
 * A visitor that visit all standard BF nodes.
 *
 * @param <R> a type that visitor methods return
 * @param <D> a type of additional data passed to visitor methods
 * @author Sergey Basalaev
 */
public interface TreeVisitor<R,D> {
	R visitGetChar(GetCharTree tree, D data);
	R visitPutChar(PutCharTree tree, D data);
	R visitInc(IncTree tree, D data);
	R visitMove(MoveTree tree, D data);
	R visitLoop(LoopTree tree, D data);
	R visitOther(Tree tree, D data);
	R visitUnit(UnitTree tree);
	void setOptions(Options options);
}

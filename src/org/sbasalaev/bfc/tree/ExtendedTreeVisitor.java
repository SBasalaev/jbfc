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

import org.sbasalaev.bfc.Options;

/**
 * An extended version of visitor that also visit additional nodes.
 *
 * @param <R> a type that visitor methods return
 * @param <D> a type of additional data passed to visitor methods
 * @author Sergey Basalaev
 */
public interface ExtendedTreeVisitor<R,D> extends TreeVisitor<R,D> {
	R visitAssign(AssignTree tree, D data);
}

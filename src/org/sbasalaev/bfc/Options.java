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

package org.sbasalaev.bfc;

import org.sbasalaev.bfc.tree.SourcePosition;
import java.io.File;

/**
 * Compiler options.
 * @author Sergey Basalaev
 */
public final class Options {

	File outputDir;
	String outputName;
	boolean debug;
	int range;

	/** Package constructor. */
	Options() { }

	/**
	 * Returns output name specified by <em>-n</em> option.
	 * If <em>-n</em> option was not given returns <code>null</code>.
	 */
	public String getOutputName() {
		return outputName;
	}

	/**
	 * Returns output directory specified with <em>-d</em> option.
	 * If <em>-d</em> option was not given returns current
	 * working directory.
	 */
	public File getOutputDir() {
		return outputDir;
	}

	/**
	 * Returns <code>true</code> if <em>-g</em> option was given.
	 */
	public boolean needDebugInfo() {
		return debug;
	}

	/**
	 * Returns array size specified with <em>-r</em> option.
	 * If <em>-r</em> option was not given returns default value 30000.
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Print warning on the output
	 * @param pos      position in the source
	 * @param message  warning message
	 */
	public void warn(SourcePosition pos, String message) {
		System.err.println(pos.toString()+": "+message);
	}
}

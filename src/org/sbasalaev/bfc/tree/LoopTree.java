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

package org.sbasalaev.bfc.tree;

/**
 * A tree that puts character on the output.
 * Corresponding BF instruction is <code>'.'</code>.
 *
 * @author Sergey Basalaev
 */
public class PutCharTree extends IOTree {

	/** Constructor for subclasses. */
	public PutCharTree(SourcePosition pos) {
		super(pos);
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitPutChar(this, data);
	}
}

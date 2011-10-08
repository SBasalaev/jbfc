package org.sbasalaev.bfc.tree;

/**
 * A tree that gets character from the input.
 * Corresponding BF instruction is <code>','</code>.
 *
 * @author Sergey Basalaev
 */
public class GetCharTree extends IOTree {

	/** Constructor for subclasses. */
	public GetCharTree(SourcePosition pos) {
		super(pos);
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitGetChar(this, data);
	}
}

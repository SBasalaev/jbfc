package home.bfc.tree;

/**
 * BF tree.
 * @author Sergey Basalaev
 */
public abstract class Tree {

	private final SourcePosition pos;

	/**
	 * Constructor for subclasses.
	 *
	 * @param pos the position in the source
	 *   file this tree was constructed from
	 */
	protected Tree(SourcePosition pos) {
		this.pos = pos;
	}

	/**
	 * Returns the position in the source file
	 * this tree was constructed from. May be used by
	 * compilers to generate debugging info.
	 */
	public SourcePosition getSourcePosition() {
		return pos;
	}

	public abstract <R,D> R accept(TreeVisitor<R,D> v, D data);
}

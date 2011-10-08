package home.bfc.tree;

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

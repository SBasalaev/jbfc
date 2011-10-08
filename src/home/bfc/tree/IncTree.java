package home.bfc.tree;

/**
 * A tree that increments value at the current position
 * of array. Corresponding BF instructions are
 * <code>'+'</code> and <code>'-'</code>.
 *
 * @author Sergey Basalaev
 */
public class IncTree extends Tree {

	private final int increment;

	/**
	 * Creates new <code>IncTree</code>.
	 * @param pos        position in the source
	 * @param increment  increment value
	 */
	public IncTree(SourcePosition pos, int increment) {
		super(pos);
		this.increment = increment;
	}

	/**
	 * Returns the value of increment.
	 */
	public int getIncrement() {
		return increment;
	}

	@Override
	public <R,D> R accept(TreeVisitor<R,D> v, D data) {
		return v.visitInc(this, data);
	}
}

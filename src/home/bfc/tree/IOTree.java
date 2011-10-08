package home.bfc.tree;

/**
 * A tree that involves I/O operations.
 * @author Sergey Basalaev
 */
public abstract class IOTree extends Tree {

	/** Constructor for subclasses. */
	protected IOTree(SourcePosition pos) {
		super(pos);
	}
}

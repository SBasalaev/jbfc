package home.bfc.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree for compilation unit.
 * @author Sergey Basalaev
 */
public class UnitTree {

	private final String source;

	private final List<Tree> children = new ArrayList<Tree>();

	/**
	 * Creates new <code>UnitTree</code>.
	 * @param source  name of the source file
	 */
	public UnitTree(String source) {
		this.source = source;
	}

	/**
	 * Returns name of the source file.
	 * May be used by compilers to generate
	 * debugging info.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Returns a list of children of this tree which then
	 * can be freely modified.
	 */
	public List<Tree> getChildren() {
		return children;
	}

	public <R,D> R accept(TreeVisitor<R,D> v) {
		return v.visitUnit(this);
	}
}

package home.bfc.opt;

import home.bfc.Options;
import home.bfc.tree.*;
import java.util.List;

/**
 * Eliminates all from last I/O operation till end.
 * Every visit method returns <code>true</code> if the tree
 * may be removed and <code>false</code> otherwise.
 * Deletion starts from the end and stops on first
 * <code>false</code> result.
 *
 * @author Sergey Basalaev
 */
public class TailCutter implements TreeVisitor<Boolean, Void> {

	public void setOptions(Options options) { }

	public Boolean visitInc(IncTree tree, Void data) {
		return true;
	}

	public Boolean visitMove(MoveTree tree, Void data) {
		return true;
	}

	public Boolean visitGetChar(GetCharTree tree, Void data) {
		return false;
	}

	public Boolean visitPutChar(PutCharTree tree, Void data) {
		return false;
	}

	public Boolean visitLoop(LoopTree tree, Void data) {
		for (Tree child : tree.getChildren()) {
			if (!child.accept(this, data)) return false;
		}
		return true;
	}

	public Boolean visitUnit(UnitTree tree) {
		List<Tree> children = tree.getChildren();
		for (int i = children.size()-1; i >= 0; i--) {
			if (children.get(i).accept(this, null)) {
				children.remove(i);
			} else {
				break;
			}
		}
		return false;
	}
}

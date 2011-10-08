package home.bfc.opt;

import home.bfc.Options;
import home.bfc.tree.*;
import java.util.List;
import java.util.ListIterator;

/**
 * Removes zero increments and shifts.
 * Merges consecutive increments and shifts.
 * Removes trailing shift.
 * @author Sergey Basalaev
 */
public class Simplifier implements TreeVisitor<Void, Void> {

	public void setOptions(Options options) {
		//not needed
	}

	public Void visitInc(IncTree tree, Void data) {
		return null;
	}

	public Void visitMove(MoveTree tree, Void data) {
		return null;
	}

	public Void visitGetChar(GetCharTree tree, Void data) {
		return null;
	}

	public Void visitPutChar(PutCharTree tree, Void data) {
		return null;
	}

	public Void visitLoop(LoopTree tree, Void data) {
		visitChildren(tree.getChildren());
		return null;
	}

	public Void visitUnit(UnitTree tree) {
		List<Tree> children = tree.getChildren();
		visitChildren(children);
		// remove trailing shift
		if (!children.isEmpty() && children.get(children.size()-1) instanceof MoveTree) {
			children.remove(children.size()-1);
		}
		return null;
	}

	private void visitChildren(List<Tree> children) {
		boolean optimized;
		do {
			optimized = false;
			ListIterator<Tree> iter = children.listIterator();
			while (iter.hasNext()) {
				Tree t1 = iter.next();
				if (t1 instanceof IncTree) {
					final IncTree itree = (IncTree)t1;
					if (itree.getIncrement() == 0) {
						iter.remove();
						optimized = true;
						continue;
					}
					if (iter.hasNext()) {
						Tree t2 = iter.next();
						if (t2 instanceof IncTree) {
							int incr = itree.getIncrement() + ((IncTree)t2).getIncrement();
							iter.remove();
							iter.previous();
							iter.set(new IncTree(t1.getSourcePosition(), incr));
							optimized = true;
							continue;
						} else {
							iter.previous();
						}
					}
				} else if (t1 instanceof MoveTree) {
					final MoveTree mtree = (MoveTree)t1;
					if (mtree.getOffset() == 0) {
						iter.remove();
						optimized = true;
						continue;
					}
					if (iter.hasNext()) {
						Tree t2 = iter.next();
						if (t2 instanceof MoveTree) {
							int ofs = mtree.getOffset() + ((MoveTree)t2).getOffset();
							iter.remove();
							iter.previous();
							iter.set(new MoveTree(t1.getSourcePosition(), ofs));
							optimized = true;
							continue;
						} else {
							iter.previous();
						}
					}
				} else {
					t1.accept(this, null);
				}
			}
		} while (optimized);
	}
}

package home.bfc.tree;

import home.bfc.Options;

/**
 * A visitor that visit all standard BF nodes.
 *
 * @param <R> a type that visitor methods return
 * @param <D> a type of additional data passed to visitor methods
 * @author Sergey Basalaev
 */
public interface TreeVisitor<R,D> {
	R visitGetChar(GetCharTree tree, D data);
	R visitPutChar(PutCharTree tree, D data);
	R visitInc(IncTree tree, D data);
	R visitMove(MoveTree tree, D data);
	R visitLoop(LoopTree tree, D data);
	R visitUnit(UnitTree tree);
	void setOptions(Options options);
}

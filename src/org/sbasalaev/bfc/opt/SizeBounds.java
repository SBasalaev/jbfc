package org.sbasalaev.bfc.opt;

/**
 * Used in SizeCalculator.
 * @author Sergey Basalaev
 */
class SizeBounds {
	int maxIndex = 0;
	int currentIndex = 0;

	void updateMax() {
		if (currentIndex > maxIndex) maxIndex = currentIndex;
	}
}

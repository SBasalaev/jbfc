package home.bfc.opt;

/**
 * Thrown when SizeCalculator fails to estimate size.
 * @author Sergey Basalaev
 */
class SizeEstimateException extends RuntimeException {

	public SizeEstimateException() {
	}

	public SizeEstimateException(String msg) {
		super(msg);
	}
}

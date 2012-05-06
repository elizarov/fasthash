package fasthash.benchmark;

import java.util.Locale;

/**
 * @author Roman Elizarov
 */
class Stats {
	private int n;
	private double mean;
	private double nvar;

	public void add(double x) {
		n++;
		double prev = mean;
		mean += (x - prev) / n;
		nvar += (x - prev) * (x - mean);
	}

	public int n() {
		return n;
	}

	public double mean() {
		return mean;
	}

	public double dev() {
		return Math.sqrt(nvar / (n - 1));
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "%.3f +- %.3f", mean(), dev());
	}
}

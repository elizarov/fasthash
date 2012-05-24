package fasthash.model;

import fasthash.stats.CacheStats;
import fasthash.stats.ProbeCounter;

/**
 * @author Roman Elizarov
 */
public abstract class AbstractCache implements Cache {
	public String describe() {
		return getClass().getSimpleName();
	}

	public void collectStats(long[] access, CacheStats stats) {
		stats.get("fill").add(getFillFactor());
		stats.get("probes").add(getProbes());
		stats.get("wp").add(getWeightedProbes(access));
		stats.get("wpb").add(getWeightedProbeBlocks(access));
	}

	protected double getFillFactor() {
		return Double.NaN;
	}

	private double getProbes() {
		return countTotalProbes() / size();
	}

	private double getWeightedProbes(long[] access) {
		return countAccessProbes(access, new ProbeCounter(0)) / access.length;
	}

	private double getWeightedProbeBlocks(long[] access) {
		return countAccessProbes(access, new ProbeCounter(10)) / access.length;
	}

	protected double countTotalProbes() {
		return Double.NaN;
	}

	protected double countAccessProbes(long[] access, ProbeCounter cnt) {
		return Double.NaN;
	}
}

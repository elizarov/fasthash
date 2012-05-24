package fasthash.stats;

/**
 * @author Roman Elizarov
 */
public class ProbeCounter {
	private final int blockSizeShift;

	private int lastBlock = -1;
	private long count;

	public ProbeCounter(int blockSizeShift) {
		this.blockSizeShift = blockSizeShift;
	}

	public void access(int index) {
		if (blockSizeShift == 0) {
			count++;
			return;
		}
		int block = index >>> blockSizeShift;
		if (lastBlock != block) {
			count++;
			lastBlock = block;
		}
	}

	public long getCount() {
		return count;
	}
}

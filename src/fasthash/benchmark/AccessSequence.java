package fasthash.benchmark;

import java.util.*;

import fasthash.model.Order;

/**
 * @author Roman Elizarov
 */
public class AccessSequence {
	public static final int N = 10000000;
	public static final int LAST = 10000000;

	public static final double MEAN = 250000;
	public static final double P = 1 / MEAN;
	public static final double DIVISOR = Math.log(1 - P);

	public static final long SEED0 = 1234;

	public static final boolean SCRAMBLE = Boolean.getBoolean("scramble");

	public final long seed;

	// access sequence -- native longs as required for Cache.getById method
	public final long[] access = new long[N];

	// initialization sequence -- common layout for all algos
	public final List<Order> orders = new ArrayList<Order>();

	static {
		if (SCRAMBLE)
			System.out.println("Access sequence is scrambled");
	}

	private long scramble(long id) {
		return SCRAMBLE ? (id * 12501169) % 1600153859 : id;
	}

	public AccessSequence(long seed) {
		this.seed = seed;
		Random rnd = new Random(seed);
		BitSet seen = new BitSet(LAST + 1);
		for (int i = 0; i < N; i++) {
			int id = LAST - (int)(Math.log(rnd.nextDouble()) / DIVISOR);
			access[i] = scramble(id);
			if (!seen.get(id)) {
				seen.set(id);
				orders.add(new Order(scramble(id), rnd.nextInt()));
			}
		}
		shuffleAccess(rnd);
	}

	private void shuffleAccess(Random rnd) {
		for (int i = 0; i < N - 1; i++) {
			int j = i + rnd.nextInt(N - i);
			long t = access[i];
			access[i] = access[j];
			access[j] = t;
		}
	}

	// test method to check AccessSequence properties
	public static void main(String[] args) {
		for (long seed = SEED0; seed <= SEED0 + 10; seed++)
			testSeed(seed);
	}

	private static void testSeed(long seed) {
		AccessSequence seq = new AccessSequence(seed);
		HashMap<Long, Integer> s = new HashMap<Long, Integer>();
		long min = Integer.MAX_VALUE;
		long max = Integer.MIN_VALUE;
		long mostCommonId = 0;
		int mostCommonCnt = 0;
		for (int i = 0; i < N; i++) {
			long id = seq.access[i];
			Integer oldCnt = s.get(id);
			int newCnt = oldCnt == null ? 1 : oldCnt + 1;
			s.put(id, newCnt);
			min = Math.min(min, id);
			max = Math.max(max, id);
			if (newCnt > mostCommonCnt) {
				mostCommonCnt = newCnt;
				mostCommonId = id;
			}
		}
		System.out.printf(Locale.US, "%d: Generated %,d ids: %,d distinct from %,d to %,d, most common %,d occurs %,d times%n",
			seed, N, s.size(), min, max, mostCommonId, mostCommonCnt);
	}
}

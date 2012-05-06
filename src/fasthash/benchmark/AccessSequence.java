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

	public static final long SEED = 1234;

	// access sequence
	public final long[] access = new long[N];

	// initialization sequence -- common layout for all algos
	public final Long[] init = new Long[N];
	public final HashMap<Long, Order> orders = new HashMap<Long, Order>();

	public AccessSequence() {
		Random rnd = new Random(SEED);
		for (int i = 0; i < N; i++) {
			access[i] = LAST - (long)(Math.log(rnd.nextDouble()) / DIVISOR);
		}
		// create init objects (extra memory consumption adds more realism)
		for (int i = 0; i < N; i++)
			init[i] = access[i];
		// shuffle
		for (int i = 0; i < N - 1; i++) {
			int j = i + rnd.nextInt(N - i);
			Long t = init[i];
			init[i] = init[j];
			init[j] = t;
		}
		// allocate orders
		for (int i = 0; i < N; i++)
			if (!orders.containsKey(init[i]))
				orders.put(init[i], new Order(init[i], rnd.nextInt()));
	}

	public static void main(String[] args) {
		AccessSequence seq = new AccessSequence();
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
		System.out.printf(Locale.US, "Generated %,d ids: %,d distinct from %,d to %,d, most common %,d occurs %,d times%n",
			N, s.size(), min, max, mostCommonId, mostCommonCnt);
	}
}

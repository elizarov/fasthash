package fasthash.benchmark;

import java.io.*;
import java.util.Locale;

import fasthash.model.Cache;
import fasthash.model.Order;

/**
 * @author Roman Elizarov
 */
public class BenchmarkAccessSpeed {
	private static final int PASSES = 50;
	private static final int STABLE_PASS = 3;
	private static final int PASSES_PER_SEED = 3;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: " + BenchmarkAccessSpeed.class.getName() + " <impl-class-name>");
			return;
		}
		PrintWriter log = new PrintWriter(new FileWriter("BenchmarkAccessSpeed.log", true), true);
		try {
			new BenchmarkAccessSpeed(args[0], log).go();
		} finally {
			log.close();
		}
	}

	private final String implClassName;
	private final PrintWriter log;

	public BenchmarkAccessSpeed(String implClassName, PrintWriter log) {
		this.implClassName = implClassName;
		this.log = log;
	}

	Cache createImpl() {
		try {
			return (Cache)Class.forName(implClassName).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private AccessSequence seq;
	private Cache impl;
	private int lastCheckSum;

	private void go() {
		long nextSeed = AccessSequence.SEED0;
		int nextInitPass = 1;
		Stats stats = new Stats();
		for (int pass = 1; pass <= PASSES; pass++) {
			if (pass >=  nextInitPass) {
				init(nextSeed++);
				nextInitPass = pass + PASSES_PER_SEED;
			}
			System.out.printf(Locale.US, "PASS #%2d: ", pass);
			double time = timePass();
			System.out.printf(Locale.US, "%.3f ns per item", time);
			if (pass >= STABLE_PASS) {
				stats.add(time);
				System.out.print(", avg " + stats);
			} else
				 nextInitPass++;
			System.out.printf(Locale.US, " (checksum %d)%n", lastCheckSum);
		}
		log.printf(Locale.US, "%-30s %2d : %7.3f +- %7.3f with %s (checksum %d)%n",
			impl.describe(), stats.n(), stats.mean(), stats.dev(), seq, lastCheckSum);
	}

	private void init(long seed) {
		System.out.println("Creating access sequence with seed " + seed + " ...");
		seq = new AccessSequence(seed);
		System.out.println("Created access sequence with seed " + seq);
		System.out.println("Initializing " + implClassName + " ...");
		impl = createImpl();
		for (Order order : seq.orders) {
			assert impl.getById(order.getId()) == null;
			impl.addObject(order);
		}
		assert impl.size() == seq.orders.size();
		System.out.println("Initialized with " + impl.size() + " objects " + impl.describe());
	}

	private double timePass() {
		long time = System.nanoTime();
		lastCheckSum = accessOnce();
		return ((double)(System.nanoTime() - time)) / seq.access.length;
	}

	private int accessOnce() {
		int checkSum = 0;
		for (long id : seq.access)
			checkSum += impl.getById(id).getCheck();
		return checkSum;
	}
}

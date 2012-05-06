package fasthash.benchmark;

import java.io.*;
import java.util.Locale;

import fasthash.model.Cache;

/**
 * @author Roman Elizarov
 */
public class BenchmarkAccessSpeed {
	private static final int PASSES = 10;
	private static final int STABLE_PASS = 3;
	private static final long TARGET_TIME_NS = 1000000000; // 1sec

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
	private final AccessSequence seq;
	private final PrintWriter log;

	public BenchmarkAccessSpeed(String implClassName, PrintWriter log) {
		this.implClassName = implClassName;
		this.log = log;
		System.out.println("Creating access sequence...");
		seq = new AccessSequence();
	}

	Cache createImpl() {
		try {
			return (Cache)Class.forName(implClassName).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Cache impl;
	private int lastCheckSum;

	private void go() {
		System.out.println("Initializing " + implClassName + " ...");
		init();
		System.out.println("Initialized with " + impl.size() + " objects " + impl.describe());
		assert impl.size() == seq.orders.size();
		Stats stats = new Stats();
		for (int pass = 1; pass <= PASSES; pass++) {
			System.out.printf(Locale.US, "PASS #%2d: ", pass);
			double time = timePass(1);
			int reps = Math.max(1, (int)(TARGET_TIME_NS / time));
			if (reps > 1)
				time = timePass(reps);
			time /= seq.access.length;
			System.out.printf(Locale.US, "[%4d reps] %.3f ns per item", reps, time);
			if (pass >= STABLE_PASS) {
				stats.add(time);
				System.out.print(", avg " + stats);
			}
			System.out.printf(Locale.US, " (checksum %d)%n", lastCheckSum);
		}
		log.printf(Locale.US, "%-30s %2d : %7.3f +- %7.3f with %d %d%n",
			impl.describe(), stats.n(), stats.mean(), stats.dev(), AccessSequence.SEED, lastCheckSum);
	}

	private void init() {
		impl = createImpl();
		for (Long id : seq.init)
			if (impl.getById(id) == null)
				impl.addObject(seq.orders.get(id));
	}

	private double timePass(int rep) {
		long time = System.nanoTime();
		makePass(rep);
		return ((double)(System.nanoTime() - time)) / rep;
	}

	private void makePass(int rep) {
		for (int i = 0; i < rep; i++)
			lastCheckSum = accessOnce();
	}

	private int accessOnce() {
		int checkSum = 0;
		for (long id : seq.access)
			checkSum += impl.getById(id).getCheck();
		return checkSum;
	}
}

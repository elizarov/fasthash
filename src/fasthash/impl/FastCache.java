package fasthash.impl;

import java.util.Locale;

import fasthash.model.Cache;
import fasthash.model.Order;

/**
 * @author Roman Elizarov
 */
public class FastCache implements Cache {
	private static final int MAGIC = 0x9E3779B9;

	private static final int P = 8;

	private int size;
	private Order[] a = new Order[1 << P];

	private int shift = 32 - P;

	public int size() {
		return size;
	}

	private int hash(long id) {
		return (((int)id ^ (int)(id >>> 32)) * MAGIC) >>> shift;
	}

	public void addObject(Order order) {
		if (size >= (2 * a.length) / 3)
			rehash();
		if (putImpl(a, order) == null)
			size++;
	}

	private Order putImpl(Order[] a, Order order) {
		long id = order.getId();
		int index = hash(id);
		Order obj;
		while ((obj = a[index]) != null) {
			if (obj.getId() == id)
				break;
			if (index == 0)
				index = a.length;
			index--;
		}
		a[index] = order;
		return obj;
	}

	private void rehash() {
		shift--;
		Order[] b = new Order[a.length * 2];
		for (int i = a.length; --i >= 0;) {
			Order order = a[i];
			if (order != null)
				putImpl(b, order);
		}
		a = b;
	}

	public Order getById(long id) {
		int index = hash(id);
		Order obj;
		while ((obj = a[index]) != null) {
			if (obj.getId() == id)
				return obj;
			if (index == 0)
				index = a.length;
			index--;
		}
		return null;
	}

	String describe;

	public String describe() {
		if (describe == null) {
			describe = String.format(Locale.US, "%s %.2f%%, %.3f",
				getClass().getSimpleName(),
				100.0 * size / a.length,
				(double)totalDistance() / size );
		}
		return describe;
	}

	private long totalDistance() {
		long res = 0;
		for (Order order : a)
			if (order != null) {
				long id = order.getId();
				int index = hash(id);
				Order obj;
				while ((obj = a[index]) != null) {
					if (obj.getId() == id)
						break;
					if (index == 0)
						index = a.length;
					index--;
					res++;
				}
			}
		return res;
	}
}

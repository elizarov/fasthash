package fasthash.impl;

import fasthash.model.AbstractCache;
import fasthash.model.Order;
import fasthash.stats.ProbeCounter;

/**
 * @author Roman Elizarov
 */
public class FastCache extends AbstractCache {
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

	@Override
	protected double getFillFactor() {
		return (double)size / a.length;
	}

	@Override
	protected double countTotalProbes() {
		ProbeCounter cnt = new ProbeCounter(0);
		for (Order order : a)
			if (order != null)
				countProbes(order.getId(), cnt);
		return cnt.getCount();
	}

	@Override
	protected double countAccessProbes(long[] access, ProbeCounter cnt) {
		for (long id : access)
			countProbes(id, cnt);
		return cnt.getCount();
	}

	private void countProbes(long id, ProbeCounter cnt) {
		int index = hash(id);
		Order obj;
		cnt.access(index);
		while ((obj = a[index]) != null) {
			if (obj.getId() == id)
				break;
			if (index == 0)
				index = a.length;
			cnt.access(--index);
		}
	}
}

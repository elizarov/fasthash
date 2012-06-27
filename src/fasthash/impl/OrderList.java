package fasthash.impl;

import java.util.List;

import fasthash.model.AbstractCache;
import fasthash.model.Order;

/**
 * @author Roman Elizarov
 */
public class OrderList extends AbstractCache {
	int size;
	int pos;
	Order[] a;

	public int size() {
		return 0;
	}

	public void addObject(Order order) {
		throw new UnsupportedOperationException();
	}

	public Order getById(long id) {
		if (pos >= a.length)
			pos = 0;
		return a[pos++];
	}

	@Override
	public void init(List<Order> orders, long[] access) {
		FastCache lookup = new FastCache();
		lookup.init(orders, access);
		int n = access.length;
		a = new Order[n];
		for (int i = 0; i < n; i++)
			a[i] = lookup.getById(access[i]);
	}
}

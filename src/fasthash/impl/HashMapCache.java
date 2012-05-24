package fasthash.impl;

import java.util.HashMap;

import fasthash.model.AbstractCache;
import fasthash.model.Order;

public class HashMapCache extends AbstractCache {
	private final HashMap<Long, Order> map = new HashMap<Long, Order>();

	public int size() {
		return map.size();
	}

	public void addObject(Order order) {
		map.put(order.getId(), order);
	}

	public Order getById(long id) {
		return map.get(id);
	}
}

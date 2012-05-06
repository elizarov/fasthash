package fasthash.impl;

import java.util.HashMap;

import fasthash.model.Cache;
import fasthash.model.Order;

public class HashMapCache extends HashMap<Long, Order> implements Cache {
	public void addObject(Order order) {
		put(order.getId(), order);
	}

	public Order getById(long id) {
		return get(id);
	}

	public String describe() {
		return getClass().getSimpleName();
	}
}

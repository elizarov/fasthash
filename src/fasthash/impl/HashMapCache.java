package fasthash.impl;

import java.util.HashMap;

import fasthash.model.Cache;
import fasthash.model.Order;

public class HashMapCache implements Cache {
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

	public String describe() {
		return getClass().getSimpleName();
	}
}

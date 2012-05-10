package fasthash.impl;

import fasthash.model.Cache;
import fasthash.model.Order;
import javolution.util.FastMap;

public class JavolutionCache implements Cache {
	private final FastMap<Long, Order> map = new FastMap<Long, Order>();

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
		return JavolutionCache.class.getSimpleName();
	}
}

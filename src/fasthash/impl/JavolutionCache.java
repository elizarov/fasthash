package fasthash.impl;

import fasthash.model.Cache;
import fasthash.model.Order;
import javolution.util.FastMap;

public class JavolutionCache extends FastMap<Long, Order> implements Cache {
	public void addObject(Order order) {
		put(order.getId(), order);
	}

	public Order getById(long id) {
		return get(id);
	}

	public String describe() {
		return JavolutionCache.class.getSimpleName();
	}
}

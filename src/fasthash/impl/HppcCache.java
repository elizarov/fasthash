package fasthash.impl;

import com.carrotsearch.hppc.LongObjectOpenHashMap;
import fasthash.model.Cache;
import fasthash.model.Order;

public class HppcCache extends LongObjectOpenHashMap<Order> implements Cache {
	public void addObject(Order order) {
		put(order.getId(), order);
	}

	public Order getById(long id) {
		return get(id);
	}

	public String describe() {
		return HppcCache.class.getSimpleName();
	}
}

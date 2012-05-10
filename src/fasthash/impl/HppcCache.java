package fasthash.impl;

import com.carrotsearch.hppc.LongObjectOpenHashMap;
import fasthash.model.Cache;
import fasthash.model.Order;

public class HppcCache implements Cache {
	private final LongObjectOpenHashMap<Order> map = new LongObjectOpenHashMap<Order>();

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
		return HppcCache.class.getSimpleName();
	}
}

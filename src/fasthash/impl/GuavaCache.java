package fasthash.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fasthash.model.Order;

public class GuavaCache implements fasthash.model.Cache {
	private final Cache<Long,Order> cache = CacheBuilder.newBuilder().build();

	public int size() {
		return (int)cache.size();
	}

	public void addObject(Order order) {
		cache.put(order.getId(), order);
	}

	public Order getById(long id) {
		return cache.getIfPresent(id);
	}

	public String describe() {
		return getClass().getSimpleName();
	}
}

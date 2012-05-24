package fasthash.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fasthash.model.AbstractCache;
import fasthash.model.Order;

public class GuavaCache extends AbstractCache {
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
}

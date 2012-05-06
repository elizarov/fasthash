package fasthash.impl;

import fasthash.model.Cache;
import fasthash.model.Order;
import gnu.trove.map.hash.TLongObjectHashMap;

public class TroveCache extends TLongObjectHashMap<Order> implements Cache {
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

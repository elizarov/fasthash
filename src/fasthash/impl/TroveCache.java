package fasthash.impl;

import fasthash.model.AbstractCache;
import fasthash.model.Order;
import gnu.trove.map.hash.TLongObjectHashMap;

public class TroveCache extends AbstractCache {
	private final TLongObjectHashMap<Order> map = new TLongObjectHashMap<Order>();

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

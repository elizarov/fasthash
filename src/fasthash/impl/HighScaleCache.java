package fasthash.impl;

import fasthash.model.Cache;
import fasthash.model.Order;
import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

public class HighScaleCache implements Cache {
	private final NonBlockingHashMapLong<Order> map = new NonBlockingHashMapLong<Order>();

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

package fasthash.impl;

import fasthash.model.Cache;
import fasthash.model.Order;
import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

public class HighScaleCache extends NonBlockingHashMapLong<Order> implements Cache {
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

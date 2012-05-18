package fasthash.impl;

import java.lang.reflect.Field;
import java.util.Locale;

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
		// We get into private fields to figure out hash capacity and to compute its fill factor.
		int capacity;
		try {
			Field chmField = NonBlockingHashMapLong.class.getDeclaredField("_chm");
			chmField.setAccessible(true);
			Object chm = chmField.get(map);
			Field valsField = chm.getClass().getDeclaredField("_vals");
			valsField.setAccessible(true);
			Object[] vals = (Object[])valsField.get(chm);
			capacity = vals.length;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return String.format(Locale.US, "%s %.2f%%",
			getClass().getSimpleName(),
			100.0 * map.size() / capacity);
	}
}

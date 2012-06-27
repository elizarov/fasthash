package fasthash.model;

import java.util.List;

import fasthash.stats.CacheStats;

/**
 * @author Roman Elizarov
 */
public interface Cache {
	public int size();
	public void addObject(Order order);
	public Order getById(long id);

	public String describe();

	public void init(List<Order> orders, long[] access);
	public void collectStats(long[] access, CacheStats stats);
}

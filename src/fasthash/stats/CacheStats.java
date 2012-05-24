package fasthash.stats;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Roman Elizarov
 */
public class CacheStats {
	private final Map<String, Stats> map = new TreeMap<String, Stats>();

	public Stats get(String key) {
		Stats stats = map.get(key);
		if (stats == null)
			map.put(key, stats = new Stats());
		return stats;
	}

	@Override
	public String toString() {
		return map.toString();
	}
}

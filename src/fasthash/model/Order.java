package fasthash.model;

/**
 * @author Roman Elizarov
 */
public class Order {
	private final long id;
	private final int check;

	private int attr3;
	private int attr4;
	private int attr5;
	private int attr6;
	private int attr7;

	public Order(long id, int check) {
		this.id = id;
		this.check = check;
	}

	public long getId() {
		return id;
	}

	public int getCheck() {
		return check;
	}
}

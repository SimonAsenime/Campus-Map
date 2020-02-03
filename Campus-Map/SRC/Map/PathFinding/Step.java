package map.pathfinding;

public class Step {

	private int x;
	private int y;

	public Step(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int get_x() {
		return x;
	}

	public int get_y() {
		return y;
	}

	public int hashCode() {
		return x*y;
	}

	public boolean equals(Object other) {
		if (other instanceof Step) {
			Step o = (Step) other;
			return (o.get_x() == x) && (o.get_y() == y);
		}
		return false;
	}
}

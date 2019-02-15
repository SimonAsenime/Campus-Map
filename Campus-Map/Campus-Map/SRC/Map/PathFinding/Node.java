package map.pathfinding;

public class Node implements Comparable {

	public int x, y, depth;
	public double cost;
	public Node parent;
	public double heuristic;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int setParent(Node parent) {
		depth = parent.depth + 1;
		this.parent = parent;
		return depth;
	}

	public int compareTo(Object other) {
		Node o = (Node) other;
		double f = heuristic + cost;
		double of = o.heuristic + o.cost;
		if (f < of) {
			return -1;
		} else if (f > of) {
			return 1;
		} else {
			return 0;
		}
	}
}

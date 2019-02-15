package map.pathfinding;

import java.util.*;

public class Path_Finder {

	private ArrayList<Node> closed = new ArrayList<Node>();
  private ArrayList<Node> open = new ArrayList<Node>();
	private int maxSearchDistance;
	private Node[][] nodes;
	private boolean allowDiagMovement;
  private int width, height;
  private byte[] map;

	public Path_Finder(int maxSearchDistance, boolean allowDiagMovement, int width, int height, byte[] map) {
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
    this.width = width;
    this.height = height;
    this.map = map.clone();

		nodes = new Node[width][height];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				nodes[x][y] = new Node(x,y);
			}
		}
	}

	public Route findPath(int sx, int sy, int tx, int ty) {
		if (map[tx*height+ty] == 1) {
			return null;
		}
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		nodes[tx][ty].parent = null;
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			Node current = getFirstInOpen();
			if (current == nodes[tx][ty]) {
				break;
			}
			removeFromOpen(current);
			addToClosed(current);
			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
					if ((x == 0) && (y == 0)) {
						continue;
					}
					if (!allowDiagMovement) {
						if ((x != 0) && (y != 0)) {
							continue;
						}
					}
					int xp = x + current.x;
					int yp = y + current.y;
					if (isValidLocation(sx,sy,xp,yp)) {
						double nextStepCost = current.cost + 1;
						Node neighbour = nodes[xp][yp];
						if (nextStepCost < neighbour.cost) {
							if (inOpenList(neighbour)) {
								removeFromOpen(neighbour);
							}
							if (inClosedList(neighbour)) {
								removeFromClosed(neighbour);
							}
						}
						if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
							neighbour.cost = nextStepCost;
							neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							addToOpen(neighbour);
						}
					}
				}
			}
		}
		if (nodes[tx][ty].parent == null) {
			return null;
		}
		Route path = new Route();
		Node target = nodes[tx][ty];
		while (target != nodes[sx][sy]) {
			path.add_first_step(target.x, target.y);
			target = target.parent;
		}
		path.add_first_step(sx,sy);
		return path;
	}

	protected Node getFirstInOpen() {
		return (Node) open.get(0);
	}

	protected void addToOpen(Node node) {
		open.add(node);
	}

	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}

	protected void removeFromOpen(Node node) {
		open.remove(node);
	}

	protected void addToClosed(Node node) {
		closed.add(node);
	}

	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}

	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}

	protected boolean isValidLocation(int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= width || (y >= height));
		if ((!invalid) && ((sx != x) || (sy != y))) {
			invalid = map[x*height+y] == 1;
		}
		return !invalid;
	}

	public double getHeuristicCost(int x, int y, int tx, int ty) {
		return Absolute_Heuristic.get_cost(x, y, tx, ty);
	}
}

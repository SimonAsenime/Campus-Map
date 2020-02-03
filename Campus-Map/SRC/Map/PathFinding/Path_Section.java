package map.pathfinding;

import java.util.*;

public class Path_Section extends Thread {

  private Path_Finder finder;
  private int sx, sy, tx, ty, width, height;
  //private ArrayList<Node> closed = new ArrayList<Node>();
  //private ArrayList<Node> open = new ArrayList<Node>();
  private ArrayList<Node> f_list = new ArrayList<Node>();
  private byte[] map;
  //private Node[][] nodes;
  private boolean searching = true; //private boolean allowDiagMovement;

  public Path_Section (Path_Finder find, int width, int height, int sx, int sy, int tx, int ty, byte[] map, boolean diag) {
    finder = find;
    this.map = map; this.width = width; this.height = height;
    this.sx = sx; this.sy = sy; this.tx = tx; this.ty = ty;
    //allowDiagMovement = diag;

    /*nodes = new Node[width][height];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				nodes[x][y] = new Node(x,y);
			}
		}*/
  }

  public void run () {
    int cx=sx;int cy=sy; int stx=sy;int sty= sy;
    double cost1=0; double cost2=0;
		/*nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		nodes[tx][ty].parent = null;
		while (open.size() != 0) {
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
						double nextStepCost = current.cost+1;
						Node neighbour = nodes[xp][yp];
						if (nextStepCost > neighbour.cost) {
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
              neighbour.setParent(current);
							addToOpen(neighbour);
						}
					}
				}
			}
		}*/
    cost1 = Absolute_Heuristic.get_cost(cx, cy, tx, ty);
    f_list.add(new Node(cx,cy));
    while (searching) {
      for (int x=-1;x<2;x++) {
        for (int y=-1;y<2;y++) {
          if ((x == 0) && (y == 0)) {
            continue;
          }
          int mx = stx+x; int my = sty+y;
          if (is_valid_location(mx, my)) {
            cost2 = Absolute_Heuristic.get_cost(mx, my, tx, ty);
            if (cost2 < cost1) {
              cx = mx; cy = my;
              cost1 = cost2;
            }
          }
        }

        System.out.println("Something");
        stx = cx; sty = cy;
        f_list.add(new Node(cx,cy));
        if (cx == tx && cy == ty) {
          searching = false;
          break;
        }
      }
    }


		/*Node target = nodes[tx][ty];
		while (target != nodes[sx][sy]) {
			f_list.add(0, target);
			target = target.parent;
		}
    f_list.add(nodes[sx][sy]);*/
    synchronized (finder) {
      finder.add_to_path(f_list);
    }
  }

  public void set_s (int sx, int sy) {
    this.sx = sx; this.sy = sy;
  }

  public void set_t (int tx, int ty) {
    this.tx = tx; this.ty = ty;
  }

  /*protected Node getFirstInOpen() {
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
  }*/

  protected boolean is_valid_location(int x, int y) {
    boolean invalid = (x < 0) || (y < 0) || (x >= width || (y >= height));
    if (!invalid) {
      invalid = map[x*height+y] == 1;
    }
    return !invalid;
  }

  //public double getHeuristicCost(int x, int y, int tx, int ty) {
    //return Absolute_Heuristic.get_cost(x, y, tx, ty);
  //}
}

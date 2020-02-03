package map.pathfinding;

import java.util.*;

public class Path_Finder {

	private ArrayList<Node> set = new ArrayList<Node>();
	private boolean allowDiagMovement;
  private int width, height;
  private byte[] map;
	private int num_threads;
	private Path_Section[] threads;

	public Path_Finder(boolean allowDiagMovement, int width, int height, byte[] map, int thread_number) {
		this.allowDiagMovement = allowDiagMovement;
    this.width = width;
    this.height = height;
    this.map = map.clone();
		num_threads = thread_number;
		threads = new Path_Section[num_threads];
	}

	public Route findPath(int sx, int sy, int tx, int ty) {
		set = new ArrayList<Node>();
		int dist = (ty-sy)/num_threads; int fx = 0; int fy = 0; double cost1 = 0; double cost2 = 0;
		int s2x = sx; int s2y = sy;
		if ((tx != 0 && ty != 0) && (map[tx*height+ty] != 1)) {
			for (int t = 0; t < num_threads; t++) {
				if (t!=num_threads-1) {
					fx = 0;
					fy = s2y+dist;
					cost1 = Absolute_Heuristic.get_cost(0, fy, tx, ty);
					cost2 = Absolute_Heuristic.get_cost(1, fy, tx, ty);
					if (cost1 < cost2) {
						fx = 1;
						cost1 = cost2;
					}
					for (int x = 2; x < width; x++) {
						if (map[x*height+fy] != 1) {
							cost2 = Absolute_Heuristic.get_cost(x, fy, tx, ty);
							if (cost2 < cost1) {
								fx = x;
								cost1 = cost2;
							}
						}
					}
				} else {
					fy = ty;
					fx = tx;
				}
				threads[t] = new Path_Section(this, width, height, s2x, s2y, fx, fy, map, allowDiagMovement);
				System.out.println("Start "+t+": "+s2x+", "+s2y);
				System.out.println("End "+t+": "+fx+", "+fy);
				s2x = fx; s2y = fy;
			}
		}
		for (int t = 0; t < num_threads; t++) {
			threads[t].start();
		}
		for (int t = 0; t < num_threads; t++) {
			try {
				threads[t].join();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		Route path = new Route();
		for (int s = 0; s < set.size(); s++) {
			path.add_step(set.get(s).x, set.get(s).y);
		}
		return path;
	}

	public void set_open (ArrayList<Node> sa) {
		set = sa;
	}

	public byte[] get_map () {
		return map;
	}

	public boolean get_diag_movement () {
		return allowDiagMovement;
	}

	public void add_to_path (ArrayList<Node> list) {
		for (int s = 0; s < list.size(); s++) {
			set.add(list.get(s));
		}
	}

	public int get_width () {return width;} public int get_height () {return height;}
}

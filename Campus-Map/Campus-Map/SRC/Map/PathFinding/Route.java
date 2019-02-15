package map.pathfinding;

import java.util.ArrayList;

public class Route {

	private ArrayList<Step> steps = new ArrayList<Step>();

	public Route() {
	}

	public int get_length() {
		return steps.size();
	}

	public Step get_step(int index) {
		return (Step) steps.get(index);
	}

	public int get_x(int index) {
		return get_step(index).get_x();
	}

	public int get_y(int index) {
		return get_step(index).get_y();
	}

	public void add_step(int x, int y) {
		steps.add(new Step(x,y));
	}

	public void add_first_step(int x, int y) {
		steps.add(0, new Step(x, y));
	}

	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}
}

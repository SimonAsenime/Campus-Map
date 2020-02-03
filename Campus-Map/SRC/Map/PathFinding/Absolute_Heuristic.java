package map.pathfinding;

public class Absolute_Heuristic {
  public static double get_cost (int x, int y, int tx, int ty) {
    return Math.abs(tx-x)+Math.abs(ty-y);
  }
}

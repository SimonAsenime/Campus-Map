package map.input;

import java.util.ArrayList;
import java.awt.event.*;

public class Input extends Input_Codes  implements KeyListener {

  private ArrayList<Integer> pressed_keys = new ArrayList<Integer>();
  private ArrayList<Integer> down_keys = new ArrayList<Integer>();

  public void keyPressed (KeyEvent e) {
    int key = e.getKeyCode();
    if (down_keys.indexOf(key) == -1) {
      pressed_keys.add(key);
      down_keys.add(key);
    }
  }

  public void keyReleased (KeyEvent e) {
    int key = e.getKeyCode();
    if (pressed_keys.indexOf(key) != -1)
      pressed_keys.remove(pressed_keys.indexOf(key));
    if (down_keys.indexOf(key) != -1)
      down_keys.remove(down_keys.indexOf(key));
  }

  // UNUSED
  public void keyTyped (KeyEvent e) {}

  // Game Access
  public boolean is_key_pressed (int key) {
    if ((pressed_keys.indexOf(key) != -1)) {
      pressed_keys.remove(pressed_keys.indexOf(key));
      return true;
    }
    return false;
  }

  public boolean is_key_down (int key) {
    return (down_keys.indexOf(key) != -1);
  }
}

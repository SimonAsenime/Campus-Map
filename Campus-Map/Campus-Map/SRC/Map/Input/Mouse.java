package map.input;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Mouse extends Input_Codes implements MouseListener, MouseMotionListener {

  private static ArrayList<Integer> pressed_buttons = new ArrayList<Integer>();
  private static ArrayList<Integer> down_buttons = new ArrayList<Integer>();

  private static int x, y;

  public void mousePressed (MouseEvent e) {
    x = e.getX();
    y = e.getY();
    int button = e.getButton();
    if (down_buttons.indexOf(button) == -1) {
      pressed_buttons.add(button);
      down_buttons.add(button);
    }
  }

  public void mouseReleased (MouseEvent e) {
    int button = e.getButton();
    if (pressed_buttons.indexOf(button) != -1)
      pressed_buttons.remove(pressed_buttons.indexOf(button));
    if (down_buttons.indexOf(button) != -1)
      down_buttons.remove(down_buttons.indexOf(button));
  }

  public void mouseDragged (MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }

  public void mouseMoved (MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }

  // Unused_Methods
  public void mouseClicked (MouseEvent e) {}
  public void mouseEntered (MouseEvent e) {}
  public void mouseExited (MouseEvent e) {}


  // Game Access
  public boolean is_button_pressed (int button) {
      if ((pressed_buttons.indexOf(button) != -1)) {
        pressed_buttons.remove(pressed_buttons.indexOf(button));
        return true;
      }
      return false;
    }

  public boolean is_button_down (int button) {
      return (down_buttons.indexOf(button) != -1);
    }

  public int get_x () {
    return x;
  }

  public int get_y () {
    return y;
  }
}

package map.graphics;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import map.input.*;

public class Tool_Bar extends JPanel {

  private static final long serialVersionUID = 42l;

  private Dimension size;
  private Time time;
  private boolean path_type, creating_path;
  private int mouse_x, mouse_y, distance;

  public Tool_Bar (int width, int height) {
    size = new Dimension(width, height);
    setPreferredSize(size);
  }

  public void paintComponent (Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    draw_background(g2d);
    draw_text(g2d);
  }

  public void draw_background (Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    g2d.drawRoundRect(0, 0, size.width, size.height, (int)Math.sqrt(size.width), (int)Math.sqrt(size.height));
    g2d.drawRoundRect(1, 1, size.width-1, size.height-1, (int)Math.sqrt(size.width-1), (int)Math.sqrt(size.height-1));
    g2d.drawRoundRect(2, 2, size.width-2, size.height-2, (int)Math.sqrt(size.width-2), (int)Math.sqrt(size.height-2));

    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRoundRect(3, 3, size.width-3, size.height-3, (int)Math.sqrt(size.width-3), (int)Math.sqrt(size.height-3));
  }

  public void draw_text (Graphics2D g2d) {
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Times New Roman", Font.PLAIN, 15));
    g2d.drawString(mouse_x+", "+mouse_y, 4, 20);
    g2d.drawString(distance/100+" m", 4, 50);
    g2d.drawString(time.get_hours()+" hrs, "+time.get_minutes()+" mins, "+time.get_seconds()+" secs", 4, 80);
    g2d.drawString("Diagonal Path: " + path_type, 4, 110);
    g2d.drawString("Creating Path: " + creating_path, 4, 140);
  }

  public void update (boolean path_type, boolean creating_path, int mx, int my, int distance) {
    this.path_type = path_type;
    this.creating_path = creating_path;
    mouse_x = mx; mouse_y = my;
    this.distance = distance;
    time = Time.calc_time(distance);
  }

}

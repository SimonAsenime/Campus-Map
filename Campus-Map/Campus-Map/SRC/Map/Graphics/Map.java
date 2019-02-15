package map.graphics;

import javax.swing.*;
import net.coobird.thumbnailator.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import map.input.*;
import java.nio.file.*;
import map.pathfinding.*;

public class Map extends JPanel {
  private static final long serialVersionUID = 42l;

  private BufferedImage img, walk_img, path_img;
  private int x, y, sx1, sx2, sy1, sy2, cur_distance;
  private int x1 = 0; private int x2 = 0;
  private int y1 = 0; private int y2 = 0;
  private Dimension size;
  private byte[] walk_map = new byte[626*700];
  private boolean draw_walkmap = false; private boolean diag = true;

  private Mouse mouse = new Mouse();
  private Input input = new Input();
  private Path_Finder path_creater;
  private Tool_Bar bar;

  public Map (int x, int y, int sx, int sy) {
    this.x = x;
    this.y = y;
    size = new Dimension(sx, sy);
    setPreferredSize(size);
    render_map();
    resize_map(size.width, size.height);
    path_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    for (int s = 0; s < walk_map.length; s++) {
      walk_map[s] = 0;
    }
  }

  public void add_toolbar (Tool_Bar bar) {
    this.bar = bar;
  }

  public void start_input_listeners () {
    this.addMouseListener(mouse);
    this.addMouseMotionListener(mouse);
  }

  public boolean path_type () {
    return diag;
  }

  public int positize (int val) {
    return (int)Math.sqrt(Math.pow(val, 2));
  }

  public Input get_input () {
    return input;
  }

  public Mouse get_mouse () {
    return mouse;
  }

  public void render_map () {
    try {
      img = ImageIO.read(new File("Resources/CampusMap.png"));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void resize_map (int sx, int sy) {
    try {
      img = Thumbnails.of(img).size(sx, sy).asBufferedImage();
      size.setSize(sx, sy);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void set_points (int xp1, int yp1, int xp2, int yp2) {
    x1 = xp1;
    x2 = xp2;
    y1 = yp1;
    y2 = yp2;
  }

  public int distance_calc () {
    return (int)((1830/Math.sqrt(130)) * cur_distance);
  }

  public void write_walkmap () {
    byte[] walkmap = new byte[img.getWidth()*img.getHeight()];
    int red1, blue1, green1, pixel;
    for (int s = 0; s < img.getWidth(); s++) {
      for (int i = 0; i < img.getHeight(); i++) {
        pixel = img.getRGB(s, i);
        red1 = (pixel >> 16) & 0xff;
        green1 = (pixel >> 8) & 0xff;
        blue1 = (pixel) & 0xff;
        boolean pixel1 = (red1 == 169 && green1 == 186 && blue1 == 255);
        boolean pixel2 = (red1 == 113 && green1 == 124 && blue1 == 170);
        boolean pixel3 = (red1 == 51 && green1 == 51 && blue1 == 51);
        boolean pixel4 = (red1 == 63 && green1 == 63 && blue1 == 63);
        boolean pixel5 = (red1 == 0 && green1 == 0 && blue1 == 0);
        boolean pixel6 = (red1 == 212 && green1 == 255 && blue1 == 216);
        if (pixel1 || pixel2 || pixel3 || pixel4) {
          walkmap[s*img.getHeight()+i] = 1;
        } else {
          walkmap[s*img.getHeight()+i] = 0;
        }
      }
    }
    delete_walkmap();
    Path file = Paths.get("Resources/Walkmap");
    try {
      Files.write(file, walkmap, StandardOpenOption.CREATE);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void save_walkmap () {
    delete_walkmap();
    Path file = Paths.get("Resources/Walkmap");
    try {
      Files.write(file, walk_map, StandardOpenOption.CREATE);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void load_walkmap (String path) {
    try {
      walk_map = Files.readAllBytes(new File(path).toPath());
      construct_walkmap();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void delete_walkmap () {
    Path file = Paths.get("Resources/Walkmap");
    try {
      Files.delete(file);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void construct_walkmap () {
    walk_img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    int red_rgba = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 128).getRGB();
    for (int s = 0; s < getWidth(); s++) {
      for (int i = 0; i < getHeight(); i++) {
        if (walk_map[s*getHeight()+i] == 1) {
          walk_img.setRGB(s, i, red_rgba);
        }
      }
    }
  }

  @Override
  public void paintComponent (Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.WHITE);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.drawImage(img, null, x, y);

    if (draw_walkmap) {
      g2d.drawImage(walk_img, null, 0, 0);
    }
    g2d.drawImage(path_img, null, x, y);
  }

  public void clear_rect_walk () {
    int clear_rgba = new Color(255, 255, 255, 0).getRGB();
    Rectangle area = new Rectangle(sx1, sy1, positize(sx1-sx2), positize(sy1-sy2));
    for (int x = 0; x < img.getWidth(); x++) {
      for (int y = 0; y < img.getHeight(); y++) {
        if (area.contains(x, y)) {
          walk_map[x*img.getHeight()+y] = 0;
          walk_img.setRGB(x, y, clear_rgba);
        }
      }
    }
    sx1 = 0; sy1 = 0; sx2 = 0; sy2 = 0;
    save_walkmap();
  }

  public void fill_rect_walk () {
    int red_rgba = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 128).getRGB();
    Rectangle area = new Rectangle(sx1, sy1, positize(sx1-sx2), positize(sy1-sy2));
    for (int x = 0; x < img.getWidth(); x++) {
      for (int y = 0; y < img.getHeight(); y++) {
        if (area.contains(x, y)) {
          walk_map[x*img.getHeight()+y] = 1;
          walk_img.setRGB(x, y, red_rgba);
        }
      }
    }
    sx1 = 0; sy1 = 0; sx2 = 0; sy2 = 0;
    save_walkmap();
  }

  public void create_path () {
    path_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    path_creater = new Path_Finder(100000, diag, getWidth(), getHeight(), walk_map);
    Route route = path_creater.findPath(x1, y1, x2, y2);
    if (route != null) {
      for (int s = 0; s < route.get_length(); s++) {
        path_img.setRGB(route.get_x(s), route.get_y(s), Color.GREEN.getRGB());
        path_img.setRGB(route.get_x(s)-1, route.get_y(s), Color.GREEN.getRGB());
        path_img.setRGB(route.get_x(s)+1, route.get_y(s), Color.GREEN.getRGB());
      }
      cur_distance = route.get_length();
    }
  }

  public void update () {
    if (mouse.is_button_pressed(Mouse.LEFT_MOUSE)) {
      x1 = mouse.get_x();
      y1 = mouse.get_y();
      path_img.setRGB(x1, y1, Color.CYAN.getRGB());
      path_img.setRGB(x1+1, y1, Color.CYAN.getRGB());
      path_img.setRGB(x1-1, y1, Color.CYAN.getRGB());
    }
    if (mouse.is_button_pressed(Mouse.RIGHT_MOUSE)) {
        x2 = mouse.get_x();
        y2 = mouse.get_y();
        path_img.setRGB(x2, y2, Color.CYAN.getRGB());
        path_img.setRGB(x2+1, y2, Color.CYAN.getRGB());
        path_img.setRGB(x2-1, y2, Color.CYAN.getRGB());
    }
    if (input.is_key_pressed(Input.KEY_W)) {
      if (draw_walkmap) {
        draw_walkmap = false;
      } else {
        draw_walkmap = true;
      }
    }

    if (input.is_key_pressed(Input.KEY_C)) {
      if (diag) {
        diag = false;
      } else {
        diag = true;
      }
    }
    if (input.is_key_pressed(Input.KEY_P)) {
      create_path();
    }
    if (input.is_key_pressed(Input.KEY_L)) {
      load_walkmap("Resources/Walkmap");
      construct_walkmap();
    }
    if (input.is_key_pressed(Input.KEY_R)) {
      load_walkmap("Walkmap");
      construct_walkmap();
      save_walkmap();
    }
    bar.update(diag, mouse.get_x(), mouse.get_y(), distance_calc());
  }

  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("Campus Map");
    Tool_Bar bar = new Tool_Bar(174, 700);
    Map map = new Map(0, 0, 626, 700);
    map.add_toolbar(bar);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(map, BorderLayout.WEST);
    frame.getContentPane().add(bar, BorderLayout.EAST);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    frame.addKeyListener(map.get_input());
    map.start_input_listeners();
    map.load_walkmap("Walkmap");
    map.construct_walkmap();
    while(true) {
      map.update();
      frame.repaint();
      Thread.sleep(17);
    }
  }
}

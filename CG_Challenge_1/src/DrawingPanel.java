import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage curImage;   // Current BG image to avoid re-drawing all shapes
    private Graphics2D curImageG2D;   // Graphics component of the current image
    private ArrayList<Circle> circAr; // List of Circles in screen;
    private ArrayList<Line> lineAr;   // List of Lines in screen;
    private ArrayList<Integer> dotX;  // List of random X coordinates for 500 points
    private ArrayList<Integer> dotY;  // List of random Y coordinates for 500 points
    private Window window;            // The parent Window object
    private String status;            // Current application status
    private int radius;               // Selected radius of drawn Circles
    private int lines;                // Selected amount of drawn Lines
    private int midX;                 // X coordinate of panel center
    private int midY;                 // Y coordinate of panel center
    private int dimX;                 // Width of frame/panel
    private int dimY;                 // Height of frame/panel
    private int curX;                 // Current mouse X position
    private int curY;                 // Current mouse Y position
    private int INF;                  // A very large number 

    // Constructor method
    public DrawingPanel(Window window) {
        this.window = window; // Store reference to parent object
        addMouseListener(this);
        addMouseMotionListener(this);
        dimY = window.getDimY(); // Get the Y dimension of the parent object
        dimX = window.getDimX(); // Get the X dimension of the parent object
        midY = dimY / 2; // Get the Y midpoint of the parent object
        midX = dimX / 2; // Get the X midpoint of the parent object
        dotX = new ArrayList<Integer>();
        dotY = new ArrayList<Integer>();
        lineAr = new ArrayList<Line>();
        circAr = new ArrayList<Circle>();
        INF = 214214; // A very large number indeed.. kindof
        changeStatus("LINER");
    }

    // Reset the currently rendered image
    private void resetCurrentImage() {
        curImage = null;
        curImageG2D = null;
    }

    // When the status of the application is changed by a button from OptionPanel
    public void changeStatus(String newStatus) {
        resetCurrentImage();
        curX = curY = INF;
        if (newStatus == "LINER") {
            lines = 0;
            generateDots();
            generateRandomLines();
            // Show a screen with new set of dots but no lines drawn
        }
        if (newStatus == "CIRCLER") {
            circAr.clear();
            // Show a screen with no circles drawn
        }
        status = newStatus;
        paintImmediately(0, 0, dimX, dimY);
    }

    // Methods for handling Mouse events
    // --------------------------------------//
    public void mouseEntered(MouseEvent arg0) {
    } // Not used

    public void mousePressed(MouseEvent arg0) {
    } // Not used

    public void mouseReleased(MouseEvent arg0) {
    } // Not used

    public void mouseDragged(MouseEvent arg0) {
    } // Not used

    public void mouseExited(MouseEvent arg0) {
        curX = curY = INF;
        paintImmediately(0, 0, dimX, dimY);
    }

    public void mouseMoved(MouseEvent arg0) {
        curX = arg0.getX() - midX;
        curY = arg0.getY() - midY;
        paintImmediately(0, 0, dimX, dimY);
    }

    public void mouseClicked(MouseEvent arg0) {
        curX = arg0.getX() - midX;
        curY = arg0.getY() - midY;
        resetCurrentImage();
        if (status == "CIRCLER") {
            // This will ask for the radius of the Circle that is going to be
            // drawn
            // after every time the user presses the mouse over the drawing area
            String res = JOptionPane.showInputDialog("Enter radius of desired Circle");
            try {
                radius = Integer.parseInt(res);
            }
            catch (Exception e) {
                radius = 1; // by default if it can't parse an integer, the
                            // radius will be one.
            }
            generateCircle();
        }
        if (status == "LINER") {
            // This will ask for the amount of Lines that are going to be drawn
            // after every time the user presses the mouse over the drawing area
            String res = JOptionPane.showInputDialog("Enter desired amount of Lines:");
            try {
                lines = Integer.parseInt(res);
            } 
            catch (Exception e) {
                lines = 0; // by default if it can't parse an integer, the
                           // amount of lines will be 0.
            }
            generateRandomLines();
        }
        paintImmediately(0, 0, dimX, dimY);
    }

    // -------------------------------------------------------------------------//

    // Methods within the painting method for drawing different things
    // --------//
    private void doLines(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255));
        paintDots(g2d);
        paintLines(g2d);
    }

    private void doCircles(Graphics2D g2d) {
        paintCircles(g2d);
    }

    private void doAxis(Graphics2D g2d) {
        // Drawing the axis' with bresenham algorithm, proof of it working
        // on both horizontal and vertical lines!
        bresenhamLine(0, midY, 0, -midY, g2d); // vertical axis
        bresenhamLine(-midX, 0, midX, 0, g2d); // horizontal axis
    }

    private void doCursor(Graphics2D g2d) {
        // A hovering sign with the current coordinates of the mouse, this one
        // ..is not drawn into the currentImage because the idea is to show the
        // ..coordinates of the cursor hovering the image, which instead of being
        // ..re-rendered piece by piece, it is just loaded and then this is drawn.
        g2d.setColor(new Color(64, 64, 64));
        String pos = curX + ", " + curY * -1;
        int mx = curX + midX;
        int my = curY + midY;
        g2d.fillRoundRect(mx, my - 15, pos.length() * 10, 20, 4, 4);
        g2d.setColor(Color.WHITE);
        g2d.drawString(pos, mx + 5, my);
    }

    // -------------------------------------------------------------------------//

    // Methods for generating or painting lines and circles
    // -------------------//
    private void generateDots() {
        // Generate 500 random dots (in X/Y pairs) and add them to the dotX and
        // dotY lists
        Random r = new Random();
        dotX.clear();
        dotY.clear();
        for (int i = 0; i < 500; i++) {
            // generate dots between -350 and 350, for X and Y, since the window
            // dimensions are 700x700, so the half, midX and midY, 350, r % 350
            // generates a number between 0 and 350, positive or negative.
            dotX.add(r.nextInt() % midX);
            dotY.add(r.nextInt() % midY);
        }
    }

    private void generateRandomLines() {
        // Draw 20 lines between 20 pairs of randomly selected dots between the
        // 500 existing in the dotX and dotY lists.
        Random r = new Random();
        lineAr.clear();
        for (int i = 0; i < lines; i++) {
            int j = Math.abs(r.nextInt()) % dotX.size();
            int k = Math.abs(r.nextInt()) % dotY.size();
            int x1 = dotX.get(j);
            int y1 = dotY.get(j);
            int x2 = dotX.get(k);
            int y2 = dotY.get(k);
            Line line = new Line(x1, y1, x2, y2);
            lineAr.add(line);
        }
    }

    private void generateCircle() {
        Circle circ = new Circle(curX, -curY, radius);
        circAr.add(circ);
    }

    private void paintDots(Graphics2D g2d) {
        // Draw in the screen the 500 random dots to draw Lines between afterwards
        for (int i = 0; i < dotX.size(); ++i) {
            int x = dotX.get(i);
            int y = dotY.get(i);
            g2d.fillOval(x + midX - 2, -y + midY - 2, 4, 4);
            curImageG2D.setColor(g2d.getColor());
            curImageG2D.fillOval(x + midX - 2, -y + midY - 2, 4, 4);
        }
    }

    private void paintLines(Graphics2D g2d) {
        for (int i = 0; i < lineAr.size(); ++i) {
            Line line = lineAr.get(i);
            int x1 = line.getX1();
            int x2 = line.getX2();
            int y1 = line.getY1();
            int y2 = line.getY2();
            bresenhamLine(x1, y1, x2, y2, g2d);
        }
    }

    private void paintCircles(Graphics2D g2d) {
        for (int i = 0; i < circAr.size(); ++i) {
            Circle circ = circAr.get(i);
            int x = circ.getX();
            int y = circ.getY();
            int r = circ.getR();
            bresenhamCircle(x, y, r, g2d);
        }
    }

    // -------------------------------------------------------------------------//

    // Bresenham methods (extended to all octants) for lines and circles
    // ------//

    private void bresenhamLine(int x1, int y1, int x2, int y2, Graphics2D g2d) {
        // draw lines, only works for the first octant, but there's a workaround
        // for that, and it involves treating the lines as if they were in there
        ArrayList<Point> linePoints = new ArrayList<Point>();
        double m; // the slope
        int dy = y2 - y1; // change in y coordinates
        int dx = x2 - x1; // change in x coordinates
        if (x1 == x2) {
            int INF = 2142; // a really 'big' slope.
            m = INF; // if its a vertical line (x1 == x2), then we asume a slope
            // ..with an arbitrary value which absolute value is greater than 1,
            // ..so when swapping coordinates, it will be seen as a horizontal line
        } 
        else {
            m = (double) (dy) / (double) (dx); // the real slope of the
            // line, will be preserved ever after the line is 'truncated' so we
            // can tell it was 'truncated', and if it was, then the pixels are
            // drawn with swapped coordinates, restoring the truncating.
        }

        int dir = (m < 0) ? -1 : 1; // Direction of vertical change

        // Algorithm requires dx and dy to be positive, having an absolute dx
        // ..doesnt matter from here on because it will start from the leftmost x 
        // ..until the rightmost x (a positive direction), and having an absolute 
        // ..dy doesnt matter because it will be only used for calculation, direc-
        // ..tion of change is set individualy to be either positive or negative.
        dy = Math.abs(dy);
        dx = Math.abs(dx);

        if (Math.abs(m) > 1) {
            // If the absolute value of the slope is greater than one, this will
            // ..get a new line where the absolute value is less than one, for the
            // ..algorithm to work. The original slope is preserved so when pixels 
        	// ..are being drawn, if the original absolute value was greater than 
        	// ..one, the coordinates are swapped back at the time of printing.
            int temp = dx;
            dx = dy;
            dy = temp;
            temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x2 < x1) {
            // This is so we can use the absolute value of dx, since we are
            // ..starting always at the leftmost point, going in a positive 
        	// ..direction to the one that its at the rightmost.
            int temp = x2;
            x2 = x1;
            x1 = temp;
            temp = y2;
            y2 = y1;
            y1 = temp;
        }

        // HERE IS WHERE THE ORIGINAL BRESENHAM ALGORITHM STARTS:
        // taken from the teacher's slides, thank you Helmuth <3
        int d = 2 * dy - dx;
        int incE = 2 * dy; // increment to east
        int incNE = 2 * dy - 2 * dx; // increment to northeast
        // Whats REALLY fast about this algorithm (and precise) is that it only
        // ..uses addition and comparissons, the only needed multiplications (which
        // ..increases error) are computed only once. also, it only uses integers, 
        // ..and that gives a lot of precission and speed.
        while (dx > 0) {
            if (d <= 0) {
                d += incE;
            } else {
                d += incNE;
                y1 += dir;
            }
            if (Math.abs(m) > 1) {
                linePoints.add(new Point(y1, x1)); // Swapped-back ordering
            } else {
                linePoints.add(new Point(x1, y1)); // Normal ordering
            }
            dx--;
            x1++;
        }
        for (int i = 0; i < linePoints.size(); ++i) {
            g2d.setColor(Color.getHSBColor(
                    (float) i / (float) linePoints.size(), 1f, 1f));
            int x = linePoints.get(i).x;
            int y = linePoints.get(i).y;
            drawPixel(g2d, x, y);
        }
    }

    private void bresenhamCircle(int cx, int cy, int radius, Graphics2D g2d) {
        double rad = (double) radius;
        int y = (int) Math.round(rad);
        int x = 0;
        double d = 3 - (2 * rad);
        ArrayList<Point> oct1 = new ArrayList<Point>();
        ArrayList<Point> oct2 = new ArrayList<Point>();
        ArrayList<Point> oct3 = new ArrayList<Point>();
        ArrayList<Point> oct4 = new ArrayList<Point>();
        ArrayList<Point> oct5 = new ArrayList<Point>();
        ArrayList<Point> oct6 = new ArrayList<Point>();
        ArrayList<Point> oct7 = new ArrayList<Point>();
        ArrayList<Point> oct8 = new ArrayList<Point>();
        while (x <= y) {
            // This method will draw only an octant, but thanks to the circle
            // symmetry, it can be easily fixed for all the other octants, this
            // is by trying all possible combinations of place and signs of the
            // x and y coordinates, and adding the current position of the
            // mouse, without changing that one!
            oct1.add(new Point( x,  y));
            oct2.add(new Point( y,  x));
            oct3.add(new Point( y, -x));
            oct4.add(new Point( x, -y));
            oct5.add(new Point(-x, -y));
            oct6.add(new Point(-y, -x));
            oct7.add(new Point(-y,  x));
            oct8.add(new Point(-x,  y));
            if (d < 0) {
                d += 4 * x + 6;
            } else {
                d += 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
        ArrayList<Point> listPoints = new ArrayList<Point>();
        for (int i = 0; i < oct1.size(); ++i) {
            listPoints.add(oct1.get(i));
        }
        for (int i = 0; i < oct2.size(); ++i) {
            listPoints.add(oct2.get(oct2.size() - i - 1));
        }
        for (int i = 0; i < oct3.size(); ++i) {
            listPoints.add(oct3.get(i));
        }
        for (int i = 0; i < oct4.size(); ++i) {
            listPoints.add(oct4.get(oct4.size() - i - 1));
        }
        for (int i = 0; i < oct5.size(); ++i) {
            listPoints.add(oct5.get(i));
        }
        for (int i = 0; i < oct6.size(); ++i) {
            listPoints.add(oct6.get(oct6.size() - i - 1));
        }
        for (int i = 0; i < oct7.size(); ++i) {
            listPoints.add(oct7.get(i));
        }
        for (int i = 0; i < oct8.size(); ++i) {
            listPoints.add(oct8.get(oct8.size() - i - 1));
        }
        for (int i = 0; i < listPoints.size(); ++i) {
            g2d.setColor(Color.getHSBColor(
                    (float) i / (float) listPoints.size(), 1f, 1f));
            int px = listPoints.get(i).x;
            int py = listPoints.get(i).y;
            drawPixel(g2d, px + cx, py + cy);
        }
    }

    private void drawPixel(Graphics2D g2d, int x, int y) {
        y *= -1; // because the usual Y orientation is reversed, grows downwards
        // Drawing a pixel is like drawing a line that ends where it begins
        x += midX; // add the x offset of the mid point because the usual 0,0 is
        y += midY; // ..at the northwest-most place of the screen
        g2d.drawLine(x, y, x, y);
        curImageG2D.setColor(g2d.getColor());
        curImageG2D.drawLine(x, y, x, y);
    }

    // -------------------------------------------------------------------------//

    // Method that paints into the panel
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d); // Clears the screen
        if (curImage == null) {
            // This is to avoid re-rendering each of the circles/lines when the
            // ..screen is refreshed (which happens due to the constantly showing 
        	// ..of the current mouse coordinates.
            curImage = new BufferedImage(dimX, dimY,
                    BufferedImage.TYPE_INT_ARGB);
            curImageG2D = curImage.createGraphics();
            curImageG2D.setColor(Color.BLACK);
            curImageG2D.fillRect(0, 0, dimX, dimY);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, dimX, dimY);
            if (status == "LINER")
                doLines(g2d);
            if (status == "CIRCLER")
                doCircles(g2d);
            doAxis(g2d);
        } else {
            // If the image is not null, it means nothing has made it null (like, 
        	// ..changing between modes, resetting a mode, inserting a new circle 
        	// ..or changing the desired amount of lines) so simply draw the image
            g2d.drawImage(curImage, null, 0, 0);
        }
        doCursor(g2d);
    }

    private class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

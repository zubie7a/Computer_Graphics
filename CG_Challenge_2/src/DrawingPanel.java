import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener{
	private BufferedImage curImage;    // Current BG image to avoid re-drawing all shapes
	private Graphics2D curImageG2D;    // Graphics component of the current image
	private ArrayList<Line> lineAr;    // List of Lines in screen
	private Window window;             // The parent Window object
	private String status;             // Current application status
	private boolean press;             // Whether the pointer is pressed and inside the box
	private int midX;                  // X coordinate of panel center
	private int midY;                  // Y coordinate of panel center
	private int dimX;                  // Width of frame/panel
	private int dimY;                  // Height of frame/panel
	private int curX;                  // Current mouse X position
	private int curY;                  // Current mouse Y position
	private int preX;                  // Current mouse X position
	private int preY;                  // Current mouse Y position
	private int INF;                   // A very large number (?)
	private Box box;                   // The box used for the clipping
	private double totalTime;          // A variable for storing the total cumulative time of line clipping
	public DrawingPanel(Window window){
		this.window = window;          // Store reference to parent object
		addMouseListener(this);
		addMouseMotionListener(this);    // Why would there be two separate Mouse-thing listeners?
		dimY = window.getDimY();         // Get the Y dimension of the parent object
		dimX = window.getDimX();         // Get the X dimension of the parent object
		midY = dimY / 2;                 // Get the Y midpoint of the parent object
		midX = dimX / 2;                 // Get the X midpoint of the parent object
		lineAr = new ArrayList<Line>();  // List that will hold a random amount of lines 
                                         // to be clipped and drawn
		INF = 214214;                    // A very large number indeed.. kindof
		makeLines();
		makeBox();
		changeStatus("SUTHER");
	}
	
	// Reset the currently rendered image.
	// When only the pointer is moved but not the clipping box, the last rendered image will be used
	// ..instead of reclippling all the lines again. This image will be erased at the moment the box
	// ..is either dragged around, its position resetted, or number of lines or algorithm is changed.
	private void resetCurrentImage(){
		curImage = null;
		curImageG2D = null;
	}
	
	// When the status of the application is changed by a button from OptionPanel.
	public void changeStatus(String newStatus){
		resetCurrentImage();
		curX = curY = preX = preY = INF;
		makeBox();
		status = newStatus;
		paintImmediately(0, 0, dimX, dimY);
	} 
	
	// Methods for handling Mouse events --------------------------------------//
	public void mouseEntered(MouseEvent arg0){
		press = false;
	}
	public void mousePressed(MouseEvent arg0){
		
	}
	public void mouseClicked(MouseEvent arg0){
		curX = arg0.getX() - midX;
		curY = arg0.getY() - midY;
		paintImmediately(0, 0, dimX, dimY);
	}
	public void mouseReleased(MouseEvent arg0){
		mouseExited(arg0);
	} 
	public void mouseDragged(MouseEvent arg0){
		preX = curX; preY = curY;
		// Before obtaining the cursor position from the most recent event, store the
		// ..old positions inside the pre variables (for previous), this way it will be
		// ..easy to calculate the delta in cursor movement if its needed to move the box
		curX = arg0.getX() - midX; // The positions in the most recent mouse-event are
		curY = arg0.getY() - midY; // ..stored into the cur variables (for current)
		if(preX == INF && preY == INF){
			// If these are INF its because there actually was no previous event in the
			// ..chain, so the previous will be determined to be the same as the current.
			preX = curX; preY = curY;
		}
		if(box.isInsideBox(curX, -curY) == true){
			// If the actual position of the cursor is inside the clipping area/box while
			// ..the 'dragged' event is happening, then...
			press = true; // Set the boolean press to true, as in the mouse is being pressed.
			int dX = curX - preX; int dY = curY - preY;
			box.moveBox(dX, dY); // Change the position of the box with the delta in pointer
			resetCurrentImage(); // Make null the rendered image to render it again
		}
		else{
			press = false;
		}
		paintImmediately(0, 0, dimX, dimY);
	} 	
	public void mouseExited(MouseEvent arg0){
		preX = preY = curX = curY = INF;
		press = false;
		paintImmediately(0, 0, dimX, dimY);
	} 
	public void mouseMoved(MouseEvent arg0){
		// Wont move the box, it is here simply to keep refreshing the screen with the current
		// ..position of the cursor. Note that the current position of the cursor will disappear
		// ..while the box is being moved, and the position of the box center and vertices is shown
		curX = arg0.getX() - midX;
		curY = arg0.getY() - midY;
		paintImmediately(0, 0, dimX, dimY);
	}

	// Methods within the painting method for drawing different things --------//	
	private void drawBox(Graphics2D g2d){
		// The current clipping box/area will be drawn, it is an object that consists of 4 Lines.
		drawLine(g2d, box.getLeft  ());
		drawLine(g2d, box.getRight ());
		drawLine(g2d, box.getTop   ());
		drawLine(g2d, box.getBottom());
		if(press == true){
			// If the box is being moved, then the coordinates of all its four vertices and its center
			// ..will be displayed in place of the regular pointer coordinates.
			int px1, px2, px3, px4;
			int py1, py2, py3, py4;
			int cx, cy;
			px1 = box.getMinX(); py1 = box.getMinY();
			px2 = box.getMinX(); py2 = box.getMaxY();
			px3 = box.getMaxX(); py3 = box.getMinY();
			px4 = box.getMaxX(); py4 = box.getMaxY();
			cx = box.getCX();
			cy = box.getCY();
			drawCursor(g2d, px1, py1);
			drawCursor(g2d, px2, py2);
			drawCursor(g2d, px3, py3);
			drawCursor(g2d, px4, py4);
			drawCursor(g2d, cx,  cy);
		}
	}
	private void drawCursor(Graphics2D g2d, int px, int py){
		// A hovering sign with the current coordinates of a point, this one is
		// ..not drawn into the currentImage because the idea is to show the coordi-
		// ..nates of the cursor hovering the image.
		g2d.setColor(new Color(64, 64, 64));
		String pos = px + ", " + py;
		int mx = px + midX;
		int my = -py + midY;
		g2d.fillRoundRect(mx, my - 15, pos.length()*10, 20, 4, 4);
		g2d.setColor(Color.WHITE);
		g2d.drawString(pos, mx + 5, my);
		// This method will be used to draw either the current pointer coordinates,
		// ..or the box coordinates (vertices and center) in case its being dragged.
	}
	private void makeBox(){
		box = new Box(0, 0, 200, dimX, dimY); //A box/area created for clipping, at the center of the
		//screen, 'real' coordinates x and y at 0,0, with sides of 200px.
	}
	public void makeLines(){
		int numLines;
		String res = JOptionPane.showInputDialog("Write the desired number of Lines:");
		try{
			numLines = Integer.parseInt(res);
		}
		catch (Exception e){
			numLines = 1000; // Number of lines to be clipped by default is 1000.
			res = "1000";
		}
		Random r = new Random();
		resetCurrentImage(); // The currently rendered image is erased to render a new one in its place
		lineAr.clear();
		for (int i = 0; i < numLines; i++){
			// This method of generation of random numbers goes from -midX to +midX for X, and goes from
			// ..-midY to midY for Y, being mid@ the half of the canvas size in each dimension.
			int x1 = r.nextInt() % midX;
			int y1 = r.nextInt() % midY;
			int x2 = r.nextInt() % midX;
			int y2 = r.nextInt() % midY;
			Line line = new Line(x1, y1, x2, y2);
			lineAr.add(line);
			// Lines are stored, so when changing between clipping modes, or dragging the clipping box/area
			// ..around will reuse always the same Lines. The only way to generate a new set of Lines is by
			// ..pressing the "Set Lines" button, which is expressely made for that.
		}
		paintImmediately(0, 0, dimX, dimY);
		window.setLines(res);
	}
	private void drawAxis(Graphics2D g2d){
		g2d.setStroke(new BasicStroke(5));
		// Draw the screen Axis at crossing the 'real' center of coordinates ('real' because Java uses a 
		// ..'fake' coordinate system where Y increases downwards, and X/Y starts at the top-left place of 
		// ..the screen. Some changes are made to the coordinates used in the program to transform this 
		// ..system to the usual system of coordinates (the 'real' one) and later converted back to the 
		// ..application's 'fake' system at the time of line drawing. All internal operations (and coor-
		// ..dinate displaying) use the 'real' system.
		drawLine(g2d, new Line(0, -midX, 0, midX));
		drawLine(g2d, new Line(-midY, 0, midY, 0));
	}
	private void drawLines(Graphics2D g2d){
		totalTime = 0;
		for(int i = 0; i < lineAr.size(); ++i){
			Line line = lineAr.get(i);
			drawLine(g2d, line);
		}
		window.updateTime(Double.toString(totalTime));
	}
	private void drawLine(Graphics2D g2d, Line line){
		String type = line.getType();
		int x1, x2, y1, y2;
			
		if(type == "BOX"){
			// The lines that bound the clipping box/area
			x1 = line.getX1(); x2 = line.getX2();
			y1 = line.getY1(); y2 = line.getY2();
			y1 *= -1; y2 *= -1;
			x1 += midX; x2 += midX;
			y1 += midY; y2 += midY;
			g2d.setColor(Color.WHITE);
			g2d.drawLine(x1, y1, x2, y2);
		}
		else{
			ArrayList<Line> newLines = new ArrayList<Line>();
			double t1 = 0, t2 = 0;
			if(status == "SUTHER"){
				t1 = new Date().getTime();
				newLines = box.splitCohenSuther(line); 
				t2 = new Date().getTime();
			}
			if(status == "BARSKY"){
				t1 = new Date().getTime();
				newLines = box.splitLiangBarsky(line);
				t2 = new Date().getTime();
			}
			totalTime += t2 - t1;
			// This call will return a list with up to 3 Lines.
			// 1: if the line is a trivial case, either both are inside box/area
			// ...or both are outside but in the same side of the box
			// 2: if the line begins outside, and finishes inside the box/area,
			// or if the line doesn't cross the box area, but has the 2 ends on 
			// different sides of the box
			// 3: if the line crosses the box area, with both ends outside
			for(int i = 0; i < newLines.size(); ++i){
				line = newLines.get(i);
				x1 = line.getX1(); x2 = line.getX2();
				y1 = line.getY1(); y2 = line.getY2();
				y1 *= -1; y2 *= -1;
				x1 += midX; x2 += midX;
				y1 += midY; y2 += midY;
				type = line.getType();
				if(type == "OUTER"){
					// Lines outside the clipping box/area are blueish
					g2d.setColor(new Color(0, 128, 214));
				}
				else if(type == "INNER"){
					// Lines inside the clipping box/area are greenish
					g2d.setColor(new Color(0, 214, 128));
				}				
				t1 = new Date().getTime();
				g2d.drawLine(x1, y1, x2, y2);
				t2 = new Date().getTime();
				totalTime += t2 - t1;
				curImageG2D.setColor(g2d.getColor());
				curImageG2D.drawLine(x1, y1, x2, y2);
			}
		}
			
	}
	//-------------------------------------------------------------------------//

	// Method that paints into the panel
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d); // Clears the screen
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, dimX, dimY);
		if (curImage == null){
			// This is to avoid re-rendering/clipping all the lines when the screen is
			// ..refreshed (which happens when moving the mouse or minimize/maximize) 
			// ..but if the clipping area/box is moved, then the clipping and drawing will 
			// ..be done again.
			curImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
			curImageG2D = curImage.createGraphics();
			curImageG2D.setColor(Color.BLACK);
			curImageG2D.fillRect(0, 0, dimX, dimY );
			drawLines(g2d);
		}
		else{
			// If the image is not null, it means nothing has made it null (like, changing
			// ..between modes, resetting a mode, moving the clipping area/box, etc)
			g2d.drawImage(curImage, null, 0, 0);
		}
		drawAxis(g2d);
		drawBox(g2d);
		if(press == false) drawCursor(g2d, curX, -curY);
	}
}

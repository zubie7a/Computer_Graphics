import java.util.ArrayList;

import processing.core.*;
import processing.event.KeyEvent;

public class Window extends PApplet {

	Framecito frame;     // A little frame for setting values for transforming
	;                    // ..the little house, these values are cumulative so
	;                    // ..pressing 'rotate XY' twice while at 90 degrees
	;                    // ..will rotate 90 degrees to a total of 180.
	ArrayList <Shape3D> shapeList; // A list containing all the shapes to be drawn
	String type;         // The type of the last transformation applied
	String mode;         // The mode, if its "default" it paints the regular shape
	;                    // ..if its "input" it paints the inputted shape
	boolean init;        // Canvas recently initialized
	Camera cam;          // Custom Camera object used to visualize the shapes
	char key;            // This is where the currently pressed character is
	
	public void setup() {
		PFont f = createFont("helvetica", 120, false);
		textFont(f);
		cam = new Camera();
		if(frame == null){
			frame = new Framecito(this);
		}
		size(700, 700, P3D);
		mode = "input";
		key = ' ';
		initializer();
		noLoop();
	}
	
	public void initializer() {
		init = true;
		shapeList = new ArrayList<Shape3D>();
		if(mode.equals("default")){
			shapeList.add(new Shape3D("HOUSE", 0));
			shapeList.add(new Shape3D("SQUARE", 128));
		}
		else{
			shapeList = get3DShapesFromFile();
		}
		loop();
	}
	
	public void keyPressed(KeyEvent e){
		// These are for moving the camera in a certain axis direction
		if(e.getKey() == 'w') cam.shiftZ( 42);
		if(e.getKey() == 's') cam.shiftZ(-42);
		// Moving in the Z axis
		if(e.getKey() == 'a') cam.shiftX(-10);
		if(e.getKey() == 'd') cam.shiftX( 10);
		// Moving in the X axis
		if(e.getKey() == 'q') cam.shiftY( 10);
		if(e.getKey() == 'e') cam.shiftY(-10);
		// Moving in the Y axis

		// These are for rotating the camera around a certain axis
		if(e.getKey() == 'i') cam.rotateYZ(-10);
		if(e.getKey() == 'k') cam.rotateYZ( 10);
		// Rotating in the YZ plane, or around the X axis
		if(e.getKey() == 'j') cam.rotateZX(-10);
		if(e.getKey() == 'l') cam.rotateZX( 10);
		// Rotating in the ZX plane, or around the Y axis
		if(e.getKey() == 'u') cam.rotateXY(-10);
		if(e.getKey() == 'o') cam.rotateXY( 10);
		// Rotating in the XY plane, or around the Z axis
		
		if(e.getKey() == 'r') cam = new Camera();
		// r is a special button that will reset the camera position
		
		key = e.getKey();
		loop();
	}
		
	public void draw() {
		background(0);
		if(keyPressed){
			fill(255);
			text("" + key, 60, height - 120);
		}
		strokeWeight(2);
		drawShapes();
		drawAxis();
		noLoop();
	}
		
	public void drawShapes() {
		for(int i = 0; i < shapeList.size(); ++i){
			drawShape3D(shapeList.get(i));
		}
	}
	
	public void drawAxis() {
		// The three axis' will be drawn
		// Each one will have a length of 100
		Point3D p0 = new Point3D(  0,   0,   0);
		Point3D p1 = new Point3D(100,   0,   0);
		Point3D p2 = new Point3D(  0, 100,   0);
		Point3D p3 = new Point3D(  0,   0, 100);
		strokeWeight(4);
		p0.align(cam);
		// The X axis is RED
		p1.align(cam);
		drawLine2D(p0, p1, 255, 0, 0);
		// The Y axis is GREEN
		p2.align(cam);
		drawLine2D(p0, p2, 0, 255, 0);
		// The Z axis is BLUE
		p3.align(cam);
		drawLine2D(p0, p3, 0, 0, 255);
		// All axis are defined as lines between a central point and
		// ..three different points. These axis' are aligned to the 
		// ..camera so changes in it are reflected upon the axis ori-
		// ..entation. However, transformations on the shapes won't
		// ..affect how the axis' look!
	}

	public float mapX(float x) {
		// Map the value of X, which is simply adding half the width
		return width / 2 + x;
	}

	public float mapY(float y) {
		// Map the value of Y, which is substracting it from half the
		// ..height, since normally Y increases upwards.
		return height / 2 - y;
	}
	
	public void drawLine2D(Point3D p1, Point3D p2, int r, int g, int b) {
		// This will be used to draw the axis' lines and put a circle at the end
		float x1 = mapX(p1.x);
		float y1 = mapY(p1.y);
		float x2 = mapX(p2.x);
		float y2 = mapY(p2.y);
		stroke(r, g, b);
		line(x1, y1, x2, y2);
		fill(r, g, b);
		ellipse(x2, y2, 5, 5);
	}
	
	public void do2DVertex(float x, float y) {
		// Paint in 2D after doing custom projection, this is used for the shapes
		vertex(mapX(x), mapY(y));
	}
	
	public void drawShape3D(Shape3D shape) {
		beginShape();
		// From here on a polygon will start to be drawn, and its filling
		// ..(if any) can be specified, same for its edge strokes.
		stroke(255);
		noFill();
		for (int i = 0; i < shape.points.size(); i++) {
			Point3D p = shape.points.get(i).clone();
			p.align(cam);
			do2DVertex(p.x, p.y);
		}
		endShape();
	}

	public void updateValues3D(float axy, float ayz, float azx, float sx,
			float sy, float sz, float tx, float ty, float tz, String type) {
		for(int i = 0; i < shapeList.size(); ++i){
			Shape3D shape = shapeList.get(i);
			if (type == "ROTATE") {
				shape.rotate(axy, ayz, azx);
			}
			if (type == "SCALE") {
				shape.scale(sx, sy, sz);
			}
			if (type == "TRANSLATE") {
				shape.translate(tx, ty, tz);
			}
		}
		loop();
	}
	

	public ArrayList<Shape3D> get3DShapesFromFile(){
		ArrayList<Shape3D> shapes = new ArrayList<Shape3D>();
		
		String lines[] = loadStrings("input.txt");
		// Get from a file called 'input.txt' in 'src/data' the lines
		
		int pointsNum = Integer.parseInt(lines[0]);
		// The first value in that file is the number of points a 3D
		// ..shape will have. These could represent a collection of 
		// ..polygons, in this case one that will show a 3D house.
		
		Point3D points[] = new Point3D[pointsNum]; 
		// An array for storing the given points
		
		for (int i = 1 ; i <= pointsNum && i <= lines.length; i++) {
		   String line = lines[i];
		   // Each line read has 4 values separated by a space
		   // The number of the point, and its x/y/z coordinates
		   
		   String strPoints[] = line.split(" ");
		   //This will split the line into its 4 separate values

		   int pointPos = Integer.parseInt(strPoints[0]);
		   float x = Float.parseFloat(strPoints[1]);
		   float y = Float.parseFloat(strPoints[2]);
		   float z = Float.parseFloat(strPoints[3]);
		   Point3D point = new Point3D(x, y, z);
		   points[pointPos-1] = point;
		   // The point its created and stored into an array
		}
		// After all the points have been read, the remainder of the 
		// ..file will contain polygons, described by the set of points
		// ..that conform them. One of these polygons could have 3 points,
		// ..some may have 4 points, etc
		for(int i = pointsNum + 2; i < lines.length; ++i){
			Shape3D shape = new Shape3D("", (int)random(255));
			// Each polygon describing the house will have a random color.
			String line = lines[i];
			String strPoints[] = line.split(" ");
			for(int j = 0; j < strPoints.length; ++j){
				int pointPos = Integer.parseInt(strPoints[j]);
				shape.points.add(points[pointPos - 1].clone());
			}
			shapes.add(shape);
		}
		return shapes;
	}
}

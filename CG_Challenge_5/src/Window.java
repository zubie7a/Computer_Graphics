import java.util.ArrayList;

import processing.core.*;

public class Window extends PApplet {

	float axy, ayz, azx; // Variables regarding the angle of rotation
	;                    // ..between each pair of axis (XY, YZ, ZX)
	float sx, sy, sz;    // Variables regarding the scale for each axis
	float tx, ty, tz;    // Variables regarding the translating for each axis
	Framecito frame;     // A little frame for setting values for transforming
	;                    // ..the little house, these values are cumulative so
	;                    // ..pressing 'rotate XY' twice while at 90 degrees
	;                    // ..will rotate 90 degrees to a total of 180.
	ArrayList <Shape3D> shapeList; // A list containing all the shapes to be drawn
	String type;         // The type of the last transformation applied
	String mode;         // The mode, if its "default" it paints the regular shape
	;                    // ..if its "input" it paints the inputted shape
	boolean fill;        // Whether shapes will be filled, or left as a wireframe
	boolean alph;        // Whether shapes will be semi-trasparent or not
	boolean init;        // Canvas recently initialized
	
	public void setup() {
		if(frame == null){
			frame = new Framecito(this);
		}
		size(700, 700, P3D);
		mode = "default";
		initializer();
		noLoop();
	}
	
	public void initializer() {
		init = true;
		axy = ayz = azx = 0;
		sx = sy = sz = 1;
		tx = ty = tz = 0;
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
	
	public void mousePressed(){
		loop(); // When the mouse touches the screen, we have a special if
		;       // ..in the drawing loop that will reset the application
	}
	
	public void draw() {
		if(mousePressed || init){  // If the mouse is being pressed
			background(0); // ..the canvas is cleaned
			type = "";
			init = false;
		}
		strokeWeight(2);
		drawShapes();
		drawAxis();
		noLoop();
	}
		
	public void drawShapes() {
		fill = frame.getFill();
		alph = frame.getAlph();
		for(int i = 0; i < shapeList.size(); ++i){
			drawShape3D(shapeList.get(i));
		}
	}
	
	public void drawAxis() {
		drawLine2D(-width / 2, 0, width / 2, 0);
		drawLine2D(0, -height / 2, 0, height / 2);
	}

	public float mapX(float x) {
		return x + width / 2;
	}

	public float mapY(float y) {
		return height / 2 - y;
	}
	
	public float mapZ(float z) {
		return - z;
	}
	
	public void drawLine2D(float x1, float y1, float x2, float y2) {
		stroke(255);
		line(mapX(x1), mapY(y1), mapX(x2), mapY(y2));
	}

	public void drawLine3D(float x1, float y1, float z1, float x2, float y2, float z2) {
		stroke(255);
		line(mapX(x1), mapY(y1), mapZ(z1), mapX(x2), mapY(y2), mapZ(z2));
		// For 3D z is 'mapped' for changing the orientation of the Z axis
	}

	public void do3DVertex(float x, float y, float z) {
		// Paint with native projection
		vertex(mapX(x), mapY(y), mapZ(-z));
	}
	
	public void do2DVertex(float x, float y) {
		// Paint in 2D after doing custom projection
		vertex(mapX(x), mapY(y));
	}
	
	public void drawShape3D(Shape3D shape) {
		if (type == "ROTATE") {
			shape.rotate(axy, ayz, azx);
			shape.shiftHue();
		}
		if (type == "SCALE") {
			shape.scale(sx, sy, sz);
			shape.shiftHue();
		}
		if (type == "TRANSLATE") {
			shape.translate(tx, ty, tz);
			shape.shiftHue();
		}
		beginShape();
		// From here on a polygon will start to be drawn, and its filling
		// ..(if any) can be specified, same for its edge strokes.
		colorMode(HSB, 255);
		// Color Mode HSB: not RGB, but Hue-Saturation-Brightness
		// Hue from 0 to 255 goes from Red to Green to Blue to Red!
		int alpha;
		if(alph){
			alpha = 64;
		}
		else{
			alpha = 255;
		}
		if(fill){
			fill(shape.getHue(), 255, 255, alpha);
			noStroke();
		} else {
			stroke(255, 255, 255, alpha);
			noFill();
		}
		for (int i = 0; i < shape.points.size(); i++) {
			Point3D p = shape.points.get(i).clone();
			do3DVertex(p.x, p.y, p.z);
		}
		endShape();
	}

	public void updateValues3D(float axy, float ayz, float azx, float sx,
			float sy, float sz, float tx, float ty, float tz, String type) {
		this.type = type;
		if (type.equals("ROTATE")) {
			this.axy = axy;
			this.ayz = ayz;
			this.azx = azx;
		}
		if (type.equals("SCALE")) {
			this.sx = sx;
			this.sy = sy;
			this.sz = sz;
		}
		if (type.equals("TRANSLATE")) {
			this.tx = tx;
			this.ty = ty;
			this.tz = tz;
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

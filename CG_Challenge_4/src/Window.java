import processing.core.*;

public class Window extends PApplet {

	float axy;           // Variables regarding the angle of rotation
	;                    // ..between the pair of axis' X and Y
	float sx, sy;        // Variables regarding the scale for each axis
	float tx, ty;        // Variables regarding the translating for each axis
	Framecito frame;     // A little frame for setting values for transforming
	;                    // ..the little house, these values are cumulative so
	;                    // ..pressing 'rotate XY' twice while at 90 degrees
	;                    // ..will rotate 90 degrees to a total of 180.
	Shape2D square;      // The small square or 'window' inside of a house
	Shape2D house;       // The outer shape, a little house!
	String type;         // The type of the last transformation applied
	boolean alph;        // Whether shapes will be semi-trasparent or not

	public void setup() {
		if(frame == null){
			frame = new Framecito(this);
		}
		initializer();
		noLoop();
	}

	public void initializer() {
		background(0);
		axy = 0;
		sx = sy = 1;
		tx = ty = 0;
		size(700, 700, P3D);
		house = new Shape2D("HOUSE", 0);
		square = new Shape2D("SQUARE", 128);
	}

	public void mousePressed() {
		loop(); // When the mouse touches the screen, we have a special if
		;       // ..in the drawing loop that will reset the application
	}

	public void draw() {
		if (mousePressed) { // If the mouse is being pressed
			background(0); // ..the canvas is cleaned
			type = "";
		}
		strokeWeight(2);
		drawShapes();
		drawAxis();
		noLoop();
	}

	public void drawShapes() {
		alph = frame.getAlph();
		drawShape2D(house);
		drawShape2D(square);
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

	public void drawLine2D(float x1, float y1, float x2, float y2) {
		stroke(255);
		line(mapX(x1), mapY(y1), mapX(x2), mapY(y2));
	}
	
	public void do2DVertex(float x, float y) {
		vertex(mapX(x), mapY(y));
	}

	public void drawShape2D(Shape2D shape) {
		if (type == "ROTATE") {
			shape.rotate(axy);
			shape.shiftHue();
		}
		if (type == "SCALE") {
			shape.scale(sx, sy);
			shape.shiftHue();
		}
		if (type == "TRANSLATE") {
			shape.translate(tx, ty);
			shape.shiftHue();
		}
		int alpha;
		if(alph){
			alpha = 64;
		}
		else{
			alpha = 255;
		}
		beginShape();
		// From here on a polygon will start to be drawn, and its filling
		// ..(if any) can be specified, same for its edge strokes.
		colorMode(HSB, 255);
		// Color Mode HSB: not RGB, but Hue-Saturation-Brightness
		// Hue from 0 to 255 goes from Red to Green to Blue to Red!
		fill(shape.getHue(), 255, 255, alpha);
		noStroke();
		for (int i = 0; i < shape.points.size(); i++) {
			Point2D p = shape.points.get(i);
			do2DVertex(p.x, p.y);
		}
		endShape();
	}

	public void updateValues2D(float axy, float sx, float sy, float tx, float ty, String type) {
		this.type = type;
		if (type.equals("ROTATE")) {
			this.axy = axy;
		}
		if (type.equals("SCALE")) {
			this.sx = sx;
			this.sy = sy;
			
		}
		if (type.equals("TRANSLATE")) {
			this.tx = tx;
			this.ty = ty;
		}
		loop();
	}
}

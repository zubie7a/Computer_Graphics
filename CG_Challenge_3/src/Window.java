import java.util.ArrayList;

import processing.core.*;

public class Window extends PApplet {

	Shape2D houseA2D;
	Shape2D houseB2D;
	Shape2D houseC2D;
	Shape2D houseD2D;
	Shape2D square2D;
	Shape2D squareA2D;
	Shape2D squareB2D;
	Shape2D squareC2D;
	Shape2D squareD2D;

	ArrayList<Shape3D> square3DList;
	float angle2D;
	float shapeScale;
	int sizeDir;
	float shapeTranslate;
	int transDir;

	public void setup() {
		size(700, 700, P3D);
		houseA2D = new Shape2D("HOUSE", "TOP");
		houseB2D = new Shape2D("HOUSE", "DOWN");
		houseC2D = new Shape2D("HOUSE", "RIGHT");
		houseD2D = new Shape2D("HOUSE", "LEFT");
		square2D = new Shape2D("SQUARE", "");
		squareA2D = new Shape2D("SQUARE", "TOP");
		squareB2D = new Shape2D("SQUARE", "DOWN");
		squareC2D = new Shape2D("SQUARE", "RIGHT");
		squareD2D = new Shape2D("SQUARE", "LEFT");

		square3DList = new ArrayList<Shape3D>();
		for (int i = 0; i < 24; i++) {
			Shape3D shape = new Shape3D("SQUARE", i * 255 / 24);
			shape.translate(0, 200, 0);
			shape.rotate(0, 0, i * 360.0f / 24.0f);
			square3DList.add(shape);
			shape = new Shape3D("SQUARE", 255 - i * 255 / 24);
			shape.translate(0, -200, 0);
			shape.rotate(0, 0, i * 360.0f / 24.0f);
			square3DList.add(shape);
		}
		angle2D = 0;
		shapeScale = 1;
		sizeDir = 1;
		shapeTranslate = 0;
		transDir = 1;
	}

	public void drawLine2D(float x1, float y1, float x2, float y2) {
		stroke(255);
		line(mapX(x1), mapY(y1), mapX(x2), mapY(y2));
	}

	public void drawLine3D(float x1, float y1, float z1, float x2, float y2,
			float z2) {
		stroke(255);
		line(mapX(x1), mapY(y1), z1, mapX(x2), mapY(y2), z2);
	}

	public void do3DVertex(float x, float y, float z) {
		vertex(mapX(x), mapY(y), z);
	}

	public void drawShape2D(Shape2D shape) {
		Point2D p1, p2;
		float x1, y1, x2, y2;
		Shape2D newShape = shape.clone();
		newShape.transform(angle2D, shapeScale, shapeTranslate);
		for (int i = 0; i < newShape.points.size() - 1; i++) {
			p1 = newShape.points.get(i);
			p2 = newShape.points.get(i + 1);
			x1 = p1.x;
			y1 = p1.y;
			x2 = p2.x;
			y2 = p2.y;
			drawLine2D(x1, y1, x2, y2);
		}
	}

	public void drawSquare3DList() {
		for (int i = 0; i < square3DList.size(); i += 2) {
			colorMode(HSB, 255);
			Shape3D shape = square3DList.get(i);
			Shape3D newShape = shape.clone();
			fill(shape.fillHue, 255, 255);
			beginShape();
			stroke(0);
			newShape.transform(angle2D, 1, 0);
			newShape.rotateOnItself(angle2D);
			for (int j = 0; j < newShape.points.size(); ++j) {
				Point3D point = newShape.points.get(j);
				do3DVertex(point.x, point.y, point.z);
			}
			endShape();

			shape = square3DList.get(i + 1);
			newShape = shape.clone();
			fill(shape.fillHue, 255, 255);
			beginShape();
			stroke(0);
			newShape.transform(-angle2D, 1, 0);
			newShape.rotateOnItself(-angle2D);
			for (int j = 0; j < newShape.points.size(); ++j) {
				Point3D point = newShape.points.get(j);
				do3DVertex(point.x, point.y, point.z);
			}
			endShape();
		}
	}

	public void draw() {
		background(0);
		strokeWeight(2);
		drawShape2D(houseA2D);
		drawShape2D(houseB2D);
		drawShape2D(houseC2D);
		drawShape2D(houseD2D);
		drawShape2D(square2D);
		drawShape2D(squareA2D);
		drawShape2D(squareB2D);
		drawShape2D(squareC2D);
		drawShape2D(squareD2D);
		drawSquare3DList();
		updateAngle2D();
		updateSize();
		updateTranslate();
	}

	public void updateAngle2D() {
		angle2D++;
		angle2D %= 360;
	}

	public void updateSize() {
		shapeScale += 0.01 * sizeDir;
		if (shapeScale > 1.5 || shapeScale < 0.5) {
			sizeDir *= -1;
		}
	}

	public void updateTranslate() {
		shapeTranslate += transDir;
		if (shapeTranslate > 180 || shapeTranslate < 1) {
			transDir *= -1;
			shapeTranslate += transDir;
		}
	}

	public float mapX(float x) {
		return x + width / 2;
	}

	public float mapY(float y) {
		return height / 2 - y;
	}
}

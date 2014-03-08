import java.util.ArrayList;

public class Shape2D {
	
	public ArrayList<Vector2D> vectors;
	public ArrayList<Point2D> points;
	Point2D center;
	String type;
	int hue;

	public Shape2D(String type, int hue) {
		this.type = type;
		this.hue = hue;
		setType();
	}

	public void shiftHue() {
		hue += 20;
		hue %= 255;
	}

	public int getHue() {
		return hue;
	}

	public Shape2D(ArrayList<Point2D> oldpoints, String oldtype, Point2D oldcenter) {
		points = new ArrayList<Point2D>();
		for (int i = 0; i < oldpoints.size(); ++i) {
			Point2D point = oldpoints.get(i).clone();
			points.add(point);
		}
		center = oldcenter.clone();
		type = oldtype;
	}

	public void setType() {
		if (type.equals("SQUARE")) {
			makeSquare();
		} else if (type.equals("HOUSE")) {
			makeHouse();
		}
	}

	public Shape2D clone() {
		return new Shape2D(points, type, center);
	}
	
	public void makeHouse() {
		points = new ArrayList<Point2D>();
		points.add(new Point2D(50, -50));
		points.add(new Point2D(50, 50));
		points.add(new Point2D(0, 103));
		points.add(new Point2D(-50, 50));
		points.add(new Point2D(-50, -50));
		points.add(new Point2D(50, -50));
		center = new Point2D(0, 0);
	}

	public void makeSquare() {
		points = new ArrayList<Point2D>();
		points.add(new Point2D(25, -25));
		points.add(new Point2D(25, 25));
		points.add(new Point2D(-25, 25));
		points.add(new Point2D(-25, -25));
		points.add(new Point2D(25, -25));
		center = new Point2D(0, 0);
	}

	public void scale(float scaleX, float scaleY) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).scale(scaleX, scaleY);
		}
		center.scale(scaleX, scaleY);
	}

	public void rotate(float angle) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).rotate(angle);
		}
		center.rotate(angle);
	}

	public void translate(float dx, float dy) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).translate(dx, dy);
		}
		center.translate(dx, dy);
	}
}

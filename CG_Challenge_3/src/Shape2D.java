import java.util.ArrayList;

public class Shape2D {
	public ArrayList<Point2D> points;
	public ArrayList<Vector2D> vectors;
	int dirX;
	int dirY;
	String type;
	String direction;
	
	public Shape2D(String type, String direction) {
		this.type = type;
		this.direction = direction;
		setType();
		setDirection();
		setRotation();
	}

	public Shape2D(ArrayList<Point2D> oldpoints, String oldtype,
			String olddirection) {
		points = new ArrayList<Point2D>();
		for (int i = 0; i < oldpoints.size(); ++i) {
			Point2D point = oldpoints.get(i).clone();
			points.add(point);
		}
		type = oldtype;
		direction = olddirection;
		setDirection();
	}

	public void setType() {
		if (type.equals("HOUSE")) {
			makeHouse();
		} else if (type.equals("SQUARE")) {
			makeSquare();
		}
	}

	public void setDirection() {
		dirX = 0;
		dirY = 0;
		if (direction.equals("TOP")) {
			dirX = 0;
			dirY = 1;
		} else if (direction.equals("LEFT")) {
			dirX = -1;
			dirY = 0;
		} else if (direction.equals("DOWN")) {
			dirX = 0;
			dirY = -1;
		} else if (direction.equals("RIGHT")) {
			dirX = 1;
			dirY = 0;
		}
	}

	public void setRotation() {
		if (direction.equals("LEFT")) {
			rotate(90);
		} else if (direction.equals("DOWN")) {
			dirX = 0;
			dirY = -1;
			rotate(180);
		} else if (direction.equals("RIGHT")) {
			dirX = 1;
			dirY = 0;
			rotate(270);
		}
	}

	public void makeHouse() {
		points = new ArrayList<Point2D>();
		points.add(new Point2D(50, -50));
		points.add(new Point2D(50, 50));
		points.add(new Point2D(0, 103));
		points.add(new Point2D(-50, 50));
		points.add(new Point2D(-50, -50));
		points.add(new Point2D(50, -50));
	}

	public Shape2D clone() {
		return new Shape2D(points, type, direction);
	}

	public void makeSquare() {
		points = new ArrayList<Point2D>();
		points.add(new Point2D(25, -25));
		points.add(new Point2D(25, 25));
		points.add(new Point2D(-25, 25));
		points.add(new Point2D(-25, -25));
		points.add(new Point2D(25, -25));
	}

	public void scale(float scaleX, float scaleY) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).scale(scaleX, scaleY);
		}
	}

	public void rotate(float angle) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).rotate(angle);
		}
	}

	public void translate(float dx, float dy) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).translate(dx * dirX, dy * dirY);
		}
	}

	public void transform(float angle, float scale, float translate) {
		float dx, dy;
		dx = dy = translate;
		float sx, sy;
		sx = sy = scale;
		rotate(angle);
		scale(sx, sy);
		translate(dx, dy);
	}
}

import java.util.ArrayList;

public class Shape3D {
	
	public ArrayList<Vector3D> vectors;
	public ArrayList<Point3D> points;
	Point3D center;
	String type;
	int hue;

	public Shape3D(String type, int hue) {
		this.center = new Point3D(0, 0, 0);
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

	public Shape3D(ArrayList<Point3D> oldpoints, String oldtype, Point3D oldcenter) {
		points = new ArrayList<Point3D>();
		for (int i = 0; i < oldpoints.size(); ++i) {
			Point3D point = oldpoints.get(i).clone();
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
		} else {
			make();
		}
	}

	public Shape3D clone() {
		return new Shape3D(points, type, center);
	}

	public void makeHouse() {
		points = new ArrayList<Point3D>();
		points.add(new Point3D(50, -50, 1));
		points.add(new Point3D(50, 50, 1));
		points.add(new Point3D(0, 103, 1));
		points.add(new Point3D(-50, 50, 1));
		points.add(new Point3D(-50, -50, 1));
		points.add(new Point3D(50, -50, 1));
		center = new Point3D(0, 0, 1);
	}
	
	public void makeSquare() {
		points = new ArrayList<Point3D>();
		points.add(new Point3D(25, -25, 1));
		points.add(new Point3D(25, 25, 1));
		points.add(new Point3D(-25, 25, 1));
		points.add(new Point3D(-25, -25, 1));
		points.add(new Point3D(25, -25, 1));
		center = new Point3D(0, 0, 1);
	}

	public void make() {
		points = new ArrayList<Point3D>();
		center = new Point3D(0, 0, 0);
	}
	
	public void scale(float scaleX, float scaleY, float scaleZ) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).scale(scaleX, scaleY, scaleZ);
		}
		center.scale(scaleX, scaleY, scaleZ);
	}

	public void rotate(float angleXY, float angleYZ, float angleZX) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).rotate(angleXY, angleYZ, angleZX);
		}
		center.rotate(angleXY, angleYZ, angleZX);
	}

	public void translate(float dx, float dy, float dz) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).translate(dx, dy, dz);
		}
		center.translate(dx, dy, dz);
	}
}

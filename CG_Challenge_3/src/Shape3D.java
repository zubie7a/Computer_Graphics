import java.util.ArrayList;

public class Shape3D {
	public ArrayList<Point3D> points;
	public ArrayList<Vector3D> vectors;

	String type;
	int fillHue;
	Point3D center;

	public Shape3D(String type, int fillHue) {
		this.type = type;
		this.fillHue = fillHue;
		setType();
	}

	public Shape3D(ArrayList<Point3D> oldpoints, String oldtype,
			int oldfillHue, Point3D oldcenter) {
		points = new ArrayList<Point3D>();
		for (int i = 0; i < oldpoints.size(); ++i) {
			Point3D point = oldpoints.get(i).clone();
			points.add(point);
		}
		center = oldcenter.clone();
		type = oldtype;
		fillHue = oldfillHue;
	}

	public void setType() {
		if (type.equals("SQUARE")) {
			makeSquare();
		}
	}

	public Shape3D clone() {
		return new Shape3D(points, type, fillHue, center);
	}

	public void makeSquare() {
		points = new ArrayList<Point3D>();
		points.add(new Point3D(25, -25, 300));
		points.add(new Point3D(25, 25, 300));
		points.add(new Point3D(-25, 25, 300));
		points.add(new Point3D(-25, -25, 300));
		points.add(new Point3D(25, -25, 300));
		center = new Point3D(0, 0, 300);
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

	public void transform(float angle, float scale, float translate) {
		float dx, dy, dz;
		dx = dy = dz = translate;
		float sx, sy, sz;
		sx = sy = sz = scale;
		float ax, ay, az;
		ax = ay = az = angle;
		rotate(0, 0, az);
		scale(sx, sy, sz);
		translate(dx, dy, dz);
	}

	public void rotateOnItself(float angle) {
		float cx = center.x;
		float cy = center.y;
		float cz = center.z;
		translate(-cx, -cy, -cz);
		for (int i = 0; i < points.size(); i++) {
			points.get(i).rotate(angle * 3, angle * 3, angle * 3);
		}
		translate(cx, cy, cz);
	}
}

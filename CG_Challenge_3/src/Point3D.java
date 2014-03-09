public class Point3D {
	float x, y, z, w = 1;

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void rotate(float angleXY, float angleYZ, float angleZX) {
		angleXY *= Math.PI / 180;
		angleYZ *= Math.PI / 180;
		angleZX *= Math.PI / 180;
		float cos;
		float sin;

		cos = (float) Math.cos(angleXY);
		sin = (float) Math.sin(angleXY);
		float matrixXY[][] = { { cos, -sin, 0, 0 }, { sin, cos, 0, 0 },
				{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		Matrix3D rotationMatrixXY = new Matrix3D(matrixXY);
		Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(
				rotationMatrixXY, this);

		cos = (float) Math.cos(angleYZ);
		sin = (float) Math.sin(angleYZ);
		float matrixYZ[][] = { { 1, 0, 0, 0 }, { 0, cos, -sin, 0 },
				{ 0, sin, cos, 0 }, { 0, 0, 0, 1 } };
		Matrix3D rotationMatrixYZ = new Matrix3D(matrixYZ);
		rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixYZ,
				rotatedPoint);

		cos = (float) Math.cos(angleZX);
		sin = (float) Math.sin(angleZX);
		float matrixZX[][] = { { cos, 0, sin, 0 }, { 0, 1, 0, 0 },
				{ -sin, 0, cos, 0 }, { 0, 0, 0, 1 } };
		Matrix3D rotationMatrixZX = new Matrix3D(matrixZX);
		rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixZX,
				rotatedPoint);

		x = rotatedPoint.x;
		y = rotatedPoint.y;
		z = rotatedPoint.z;
	}

	public void scale(float scaleX, float scaleY, float scaleZ) {
		float matrix[][] = { { scaleX, 0, 0, 0 }, { 0, scaleY, 0, 0 },
				{ 0, 0, scaleZ, 0 }, { 0, 0, 0, 1 } };
		Matrix3D scaleMatrix = new Matrix3D(matrix);
		Point3D scaledPoint = Matrix3D
				.multiplyMatrixAndPoint(scaleMatrix, this);
		x = scaledPoint.x;
		y = scaledPoint.y;
		z = scaledPoint.z;
	}

	public void translate(float dx, float dy, float dz) {
		float matrix[][] = { { 1, 0, 0, dx }, { 0, 1, 0, dy }, { 0, 0, 1, dz },
				{ 0, 0, 0, 1 } };
		Matrix3D translateMatrix = new Matrix3D(matrix);
		Point3D translatedPoint = Matrix3D.multiplyMatrixAndPoint(
				translateMatrix, this);
		x = translatedPoint.x;
		y = translatedPoint.y;
		z = translatedPoint.z;
	}

	public Point3D clone() {
		// Clone this point
		return new Point3D(x, y, z);
	}

	// STATIC CLASS METHODS
	public static Point3D rotateNewPoint(Point3D p, float angle) {
		Point3D res;
		res = p.clone();
		res.rotate(angle, angle, angle);
		return res;
	}
}

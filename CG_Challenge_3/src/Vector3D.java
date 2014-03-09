public class Vector3D {
	float x, y, z; // Vector's dimensions
	float w = 1;

	public Vector3D(float x, float y, float z) {
		// Construct with given lengths
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Point3D p1, Point3D p2) {
		// Construct with two 3D-points
		x = p2.x - p1.x;
		y = p2.y - p1.y;
		z = p2.z - p1.z;
	}

	// VECTOR'S OWN METHODS
	public float length() {
		// Get this vector's length
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3D clone() {
		// Clone this vector
		return new Vector3D(x, y, z);
	}

	public void normalize() {
		// Make this vector's dimensions go from 0 to 1
		float length = length();
		x /= length;
		y /= length;
		z /= length;
	}

	public void scalarMultiply(float scalar) {
		// Multiply this vector's dimensions by a scalar value
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	public void substract(Vector3D v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void add(Vector3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	// STATIC CLASS METHODS
	public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
		// Create a new vector resulting from the cross product of two
		// vectors
		// x ~ i, y ~ j, z ~ k
		// i * j = k, j * k = i, k * i = j
		// j * i = -k, k * j = -i, i * k = -i
		float _x = v1.y * v2.z - v1.z * v2.y;
		float _y = v1.z * v2.x - v1.x * v2.z;
		float _z = v1.x * v2.y - v1.y * v2.x;
		return new Vector3D(_x, _y, _z);
	}

	public static float dotProduct(Vector3D v1, Vector3D v2) {
		// Return the scalar value resulting from the dot product of two
		// vectors
		float _x = v1.x * v2.x;
		float _y = v1.y * v2.y;
		float _z = v1.z * v2.z;
		return _x + _y + _z;
	}
}

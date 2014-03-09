public class Vector2D {

	float x, y; // Vector's magnitudes
	float w = 1;

	public Vector2D(float x, float y) {
		// Construct with given lengths
		this.x = x;
		this.y = y;
	}

	public Vector2D(Point2D p1, Point2D p2) {
		// Construct with two 2D-points
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}

	// VECTOR'S OWN METHODS
	public float length() {
		// Get this vector's length
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector2D clone() {
		// Clone this vector
		return new Vector2D(x, y);
	}

	public void normalize() {
		// Make this vector's dimensions go from 0 to 1
		float length = length();
		x /= length;
		y /= length;
	}

	public void scalarMultiply(float scalar) {
		// Multiply this vector's dimensions by a scalar value
		x *= scalar;
		y *= scalar;
	}

	public void substract(Vector2D v) {
		x -= v.x;
		y -= v.y;
	}

	public void add(Vector2D v) {
		x += v.x;
		y += v.y;
	}

	// STATIC CLASS METHODS
	public static float dotProduct(Vector2D v1, Vector2D v2) {
		// Return the scalar value resulting from the dot product of two
		// ..vectors
		float _x = v1.x * v2.x;
		float _y = v1.y * v2.y;
		return _x + _y;
	}
}
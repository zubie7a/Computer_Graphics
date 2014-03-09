public class Point2D {
	
	float x, y;
	float w = 1;

	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void rotate(float angle) {
		// Convert angle from Deg to Rad
		angle *= Math.PI / 180;
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float matrix[][] = { 
				{ cos, -sin, 0 }, 
				{ sin,  cos, 0 }, 
				{   0,    0, 1 } 
		}; 
		Matrix2D rotationMatrix = new Matrix2D(matrix);
		Point2D rotatedPoint = Matrix2D.multiplyMatrixAndPoint(rotationMatrix, this);
		this.x = rotatedPoint.x;
		this.y = rotatedPoint.y;
	}

	public void scale(float scaleX, float scaleY) {
		float matrix[][] = { 
				{ scaleX,      0, 0 }, 
				{      0, scaleY, 0 }, 
				{      0,      0, 1 } 
		};
		Matrix2D scaleMatrix = new Matrix2D(matrix);
		Point2D scaledPoint = Matrix2D.multiplyMatrixAndPoint(scaleMatrix, this);
		this.x = scaledPoint.x;
		this.y = scaledPoint.y;
	}

	public void translate(float transX, float transY) {
		float matrix[][] = { 
				{ 1, 0, transX }, 
				{ 0, 1, transY }, 
				{ 0, 0,      1 } 
		};
		Matrix2D translateMatrix = new Matrix2D(matrix);
		Point2D translatedPoint = Matrix2D.multiplyMatrixAndPoint(translateMatrix, this);
		this.x = translatedPoint.x;
		this.y = translatedPoint.y;
	}

	public Point2D clone() {
		// Clone this point
		return new Point2D(x, y);
	}
}

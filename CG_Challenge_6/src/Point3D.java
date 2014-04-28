public class Point3D {
	
	float x, y, z;
	float w = 1;

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void rotate(float angleXY, float angleYZ, float angleZX) {
		rotateXY(angleXY);
		rotateYZ(angleYZ);
		rotateZX(angleZX);
	}
	
	public void normalize(){
		x /= w;
		y /= w;
		z /= w;
		w /= w;
	}
	
	public void rotateXY(float angleXY){
		// Convert angle from Deg to Rad
		angleXY *= Math.PI / 180;
		float cos = (float) Math.cos(angleXY);
		float sin = (float) Math.sin(angleXY);
		float matrixXY[][] = { 
				{ cos, -sin, 0, 0 }, 
				{ sin,  cos, 0, 0 },
				{   0,    0, 1, 0 }, 
				{   0,    0, 0, 1 }
		};
		Matrix3D rotationMatrixXY = new Matrix3D(matrixXY);
		Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixXY, this);
		this.x = rotatedPoint.x;
		this.y = rotatedPoint.y;
		this.z = rotatedPoint.z;
	}
	
	public void rotateYZ(float angleYZ){
		// Convert angle from Deg to Rad
		angleYZ *= Math.PI / 180;
		float cos = (float) Math.cos(angleYZ);
		float sin = (float) Math.sin(angleYZ);
		float matrixYZ[][] = { 
				{ 1,   0,    0, 0 }, 
				{ 0, cos, -sin, 0 },
				{ 0, sin,  cos, 0 }, 
				{ 0,   0,    0, 1 } 
		};
		Matrix3D rotationMatrixYZ = new Matrix3D(matrixYZ);
		Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixYZ, this);
		this.x = rotatedPoint.x;
		this.y = rotatedPoint.y;
		this.z = rotatedPoint.z;
	}
	
	public void rotateZX(float angleZX){
		// Convert angle from Deg to Rad
		angleZX *= Math.PI / 180;
		float cos = (float) Math.cos(angleZX);
		float sin = (float) Math.sin(angleZX);
		float matrixZX[][] = { 
				{  cos, 0, sin, 0 }, 
				{    0, 1,   0, 0 },
				{ -sin, 0, cos, 0 }, 
				{    0, 0,   0, 1 } 
		};
		Matrix3D rotationMatrixZX = new Matrix3D(matrixZX);
		Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixZX, this);
		x = rotatedPoint.x;
		y = rotatedPoint.y;
		z = rotatedPoint.z;
	}

	public void scale(float scaleX, float scaleY, float scaleZ) {
		float matrix[][] = { 
				{ scaleX,      0,      0, 0 }, 
				{      0, scaleY,      0, 0 },
				{      0,      0, scaleZ, 0 }, 
				{      0,      0,      0, 1 } 
		};
		Matrix3D scaleMatrix = new Matrix3D(matrix);
		Point3D scaledPoint = Matrix3D.multiplyMatrixAndPoint(scaleMatrix, this);
		this.x = scaledPoint.x;
		this.y = scaledPoint.y;
		this.z = scaledPoint.z;
	}

	public void translate(float transX, float transY, float transZ) {
		float matrix[][] = { 
				{ 1, 0, 0, transX }, 
				{ 0, 1, 0, transY }, 
				{ 0, 0, 1, transZ },
				{ 0, 0, 0,      1 }
		};
		Matrix3D translateMatrix = new Matrix3D(matrix);
		Point3D translatedPoint = Matrix3D.multiplyMatrixAndPoint(translateMatrix, this);
		this.x = translatedPoint.x;
	    this.y = translatedPoint.y;
		this.z = translatedPoint.z;
	}
	
	public void project(){
		// This will flatten 3D points into 2D space using a perspective projection.
		// The projection plane is 1000 units from the origin, and the program has
		// ..to make sure the points doesn't cross this plane, or glitches will ha-
		// ..ppen (like the points crossing it having their signs changed, so if a
		// ..shape partially crosses this plane, some points will be swapped and
		// ..others won't resulting in a very messed up shape)
		float dist = 1000;
		float f = 1f/dist;
		z += dist; 
		float matrix[][] = { 
				{ 1, 0, 0, 0 }, 
				{ 0, 1, 0, 0 }, 
				{ 0, 0, 1, 0 },
				{ 0, 0, f, 0 }
		};
		Matrix3D projectMatrix = new Matrix3D(matrix);
		Point3D translatedPoint = Matrix3D.multiplyMatrixAndPoint(projectMatrix, this);
		this.x = translatedPoint.x;
	    this.y = translatedPoint.y;
		this.z = translatedPoint.z;
		this.w = translatedPoint.w;
		normalize();
	}

	public void align(Camera cam){
		// Every point before being drawn, is first aligned with the camera's
		// ..own orientation
		Camera camera = cam.clone();
		Vector3D n = camera.n; // The 'looking-at' vector
		n.normalize(); 
		Vector3D u = cam.u; // The 'kinda up' vector
		u = Vector3D.crossProduct(u, n); // Now a 'sideways' vector
		u.normalize();
		Vector3D v = Vector3D.crossProduct(n, u); // The 'real up' vector
		Vector3D camPos = new Vector3D(camera.point, new Point3D(0,0,0));
		float dx = Vector3D.dotProduct(u, camPos); 
		float dy = Vector3D.dotProduct(v, camPos);
		float dz = Vector3D.dotProduct(n, camPos);
		float matrix[][] = { 
				{ u.x, u.y, u.z, dx }, 
				{ v.x, v.y, v.z, dy }, 
				{ n.x, n.y, n.z, dz }, 
				{   0,   0,   0,  1 }
		};
		Matrix3D camMatrix = new Matrix3D(matrix);
		Point3D alignedPoint = Matrix3D.multiplyMatrixAndPoint(camMatrix, this);
		this.x = alignedPoint.x;
		this.y = alignedPoint.y;
		this.z = alignedPoint.z;
		this.w = alignedPoint.w;
		this.project();
	}
	
	
	public Point3D clone() {
		// Clone this point
		return new Point3D(x, y, z);
	}
}

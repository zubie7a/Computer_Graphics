
public class Camera {
	public Point3D pointb; // Helper point to find the 'looking' direction of the cam
	public Point3D point;  // Point defining the position of the camera
	public Vector3D n;     // Vector that shows the direction the camera is looking at
	public Vector3D u;     // Vector that shows the 'upwards' direction of the camera
	public float dX;       // Change in X position of the camera
	public float dY;       // Change in Y position of the camera
	public float dZ;       // Change in Z position of the camera
	
	public Camera(){
		dX = dY = dZ = 0;
		point = new Point3D(0, 0, -110);
		pointb = new Point3D(0, 0, -100);
		makeLook();
		makeUp();
	}
	
	public void makeLook(){
		// Make 'looking' vector using the position of the camera and a helper point
		n = new Vector3D(point, pointb);
	}
	
	public void makeUp(){
		// Default 'up' vector
		u = new Vector3D(0, 1, 0);
	}
	
	public Camera(Point3D point, Vector3D u, Vector3D n, float dX, float dY, float dZ){
		this.point = point.clone();
		this.dX = dX;
		this.dY = dY;
		this.dZ = dZ;
		this.u = u.clone();
		this.n = n.clone();
	}
	
	public Camera clone(){
		return new Camera(point, u, n, dX, dY, dZ);
	}
	
	
	// Everytime the camera is rotated, the two points defining the camera location
	// ..and its looking direction are rotated and then the looking vector is recalc-
	// ..ulated. The original 'up' vector is also rotated.
	public void rotateYZ(int dir){
		point.rotate(0, dir, 0);
		pointb.rotate(0, dir, 0);
		Point3D uP = new Point3D(u.x, u.y, u.z);
		uP.rotate(0, dir, 0);
		u = new Vector3D(uP.x, uP.y, uP.z);
		makeLook();		
	}
	
	public void rotateZX(int dir){
		point.rotate(0, 0, dir);
		pointb.rotate(0, 0, dir);
		Point3D uP = new Point3D(u.x, u.y, u.z);
		uP.rotate(0, 0, dir);
		u = new Vector3D(uP.x, uP.y, uP.z);
		makeLook();		
	}
	
	public void rotateXY(int dir){
		point.rotate(dir, 0, 0);
		pointb.rotate(dir, 0, 0);
		Point3D uP = new Point3D(u.x, u.y, u.z);
		uP.rotate(dir, 0, 0);
		u = new Vector3D(uP.x, uP.y, uP.z);
		makeLook();		
	}
	
	public void shiftZ(int dz){
		point.z += dz;
		pointb.z += dz;
	}
	
	public void shiftY(int dy){
		point.y += dy;
		pointb.y += dy;
	}
	
	public void shiftX(int dx){
		point.x += dx;
		pointb.x += dx;
	}

	public Point3D getPoint(){
		return new Point3D(dX, dY, dZ);
	}

}

public class Matrix2D {
	
	public float m[][];

	public Matrix2D(float m[][]) {
		this.m = m;
	}
	
	public Matrix2D(){
		m = new float[3][3];
	}

	public static Point2D multiplyMatrixAndPoint(Matrix2D mat, Point2D p) {
		// It uses a 3D matrix to make us of Homogeneous Coordinates, to be
		// ..able to translate with matrix operations
		float pt[] = { 0, 0, 0 };
		float vals[] = { p.x, p.y, 1.0f };
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				pt[i] += mat.m[i][j] * vals[j];
			}
		}
		return new Point2D(pt[0], pt[1]);
	}
}
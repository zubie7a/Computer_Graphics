
public class Line{
	private int x1, y1, x2, y2; // Line starting and ending points
	public Line(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.y2 = y2;
		this.x2 = x2;
	}
	public int getX1(){
		return x1;
	}
	public int getX2(){
		return x2;
	}
	public int getY1(){
		return y1;
	}
	public int getY2(){
		return y2;
	}
	public void setX1(int nx){
		x1 = nx;
	}
	public void setX2(int nx){
		x2 = nx;
	}
	public void setY1(int nx){
		y1 = nx;
	}
	public void setY2(int nx){
		y2 = nx;
	}
}

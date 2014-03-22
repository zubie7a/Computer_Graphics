import java.util.ArrayList;
import java.util.Stack;

public class Box{
	private int midX, midY; // Length of the canvas dimensions to determine the limits of
	                        // movement for the clipping box/area, cant drag any further
	private int  cX, cY;    // Coordinates of the clipping box/area center
	private Line bottom;    // Line that determines the lower clipping box/area boundary
	private Line right;     // Line that determines the rightside clipping box/area boundary
	private Line left;      // Line that determines the leftside clipping box/area boundary
	private Line top;       // Line that determines the upper clipping box/area boundary
	private int side;       // Length of the clipping box/area sides
	private int minX, maxX, minY, maxY; //Range of values for clipping box/area
	public Box(int cX, int cY, int side, int dimX, int dimY){
		minX = cX - side/2; maxX = cX + side/2;
		minY = cY - side/2; maxY = cY + side/2;
		bottom = new Line(minX, minY, maxX, minY); 
		right  = new Line(maxX, minY, maxX, maxY); 
		left   = new Line(minX, minY, minX, maxY); 
		top    = new Line(minX, maxY, maxX, maxY); 
		// Type of these lines is "BOX", to differentiate them for the lines to be clipped.
		left.setType  ("BOX");
		right.setType ("BOX");
		top.setType   ("BOX");
		bottom.setType("BOX");
		this.cX = cX;
		this.cY = cY;
		this.side = side;
		midX = dimX / 2;
		midY = dimY / 2;
	}
	public int getMaxX(){
		return maxX;
	}
	public int getMaxY(){
		return maxY;
	}
	public int getMinX(){
		return minX;
	}
	public int getMinY(){
		return minY;
	}
	public Line getLeft(){
		return left;
	}
	public Line getRight(){
		return right;
	}
	public Line getTop(){
		return top;
	}
	public Line getBottom(){
		return bottom;
	}
	public int getCX(){
		return cX;
	}
	public int getCY(){
		return cY;
	}
	public void moveBox(int dX, int dY){
		int x1, x2, y1, y2;
		dY *= -1; // Restore the 'real' Y-increasing upwards coordinate system
		// to the Java native 'fake' Y-increasing downwards coordinate system
		
		// This is to avoid the box moving beyond the canvas X-axis limits
		if(cX + side/2 + dX < midX && cX - side/2 + dX > -midX){
			x1 = bottom.getX1(); bottom.setX1(x1 + dX);
			x2 = bottom.getX2(); bottom.setX2(x2 + dX);
			x1 = right.getX1(); right.setX1(x1 + dX);
			x2 = right.getX2(); right.setX2(x2 + dX);
			x1 = left.getX1(); left.setX1(x1 + dX);
			x2 = left.getX2(); left.setX2(x2 + dX);
			x1 = top.getX1(); top.setX1(x1 + dX);
			x2 = top.getX2(); top.setX2(x2 + dX);
			minX += dX; maxX += dX;
			cX += dX;
		}
		// This is to avoid the box moving beyond the canvas Y-axis limits
		if(cY + side/2 + dY < midY && cY - side/2 + dY > -midY + 80){
			y1 = top.getY1(); top.setY1(y1 + dY);
			y2 = top.getY2(); top.setY2(y2 + dY);
			y1 = left.getY1(); left.setY1(y1 + dY);
			y2 = left.getY2(); left.setY2(y2 + dY);	
			y1 = right.getY1(); right.setY1(y1 + dY);
			y2 = right.getY2(); right.setY2(y2 + dY);
			y1 = bottom.getY1(); bottom.setY1(y1 + dY);
			y2 = bottom.getY2(); bottom.setY2(y2 + dY);
			minY += dY; maxY += dY;
			cY += dY;
		}
	}
	
	// LIANG-BARSKY LINE-CLIPPING METHOD
	public ArrayList<Line> splitLiangBarsky(Line line){
		int x1 = line.getX1(), y1 = line.getY1();
		int x2 = line.getX2(), y2 = line.getY2();
		ArrayList<Line> arr = new ArrayList<Line>();
		// List to store the results. Doing this permits to return all the pieces
		// of the clipped line, no matter if one, two or three. This is also to
		// avoid the "lazy" way of drawing the complete line and then the clipped
		// line above it!
		double dx = x2 - x1;
		double dy = y2 - y1;
		double p[] = new double[4];
		double q[] = new double[4];
		p[0] = -dx; p[1] = dx; 
		p[2] = -dy; p[3] = dy;
		q[0] = x1 - minX; q[1] = maxX - x1; 
		q[2] = y1 - minY; q[3] = maxY - y1;
		double ini, end, u;
		ini = 0; end = 1; 
		for(int i = 0; i < 4; i++){
			if(p[i] == 0 && q[i] < 0){
				// Special case where its a line thats parallel and right outside of
				// ..one of the clipping area boundaries.
				Line l1;
				l1 = new Line(x1, y1, x2, y2);
				l1.setType("OUTER");
				arr.add(l1);
				return arr;
			} 
	        else{
	        	u = q[i]/p[i];
	        	if(u == Double.POSITIVE_INFINITY || u == Double.NEGATIVE_INFINITY){
	        		continue;
	        	}
	        	// Ignore when its a line with dx or dy = 0, the used u will be
	        	// ..the one of the non-parallel side. Use of infinite constants!
	        	if(p[i] < 0){
	        		ini = Math.max(ini, u);
	        		// The furthest entering value
	        	}
	        	else{    
	        		end = Math.min(end, u);
	        		// The closest exiting value
	        	}
	        }            
		}
		int nx1, nx2, ny1, ny2;
		Line l1, l2, l3;
		nx1 = (int)(Math.round(x1 + ini*dx));
		nx2 = (int)(Math.round(x1 + end*dx));
		ny1 = (int)(Math.round(y1 + ini*dy));
		ny2 = (int)(Math.round(y1 + end*dy));
		if(ini < end){
	    	// Originally ini was 0 and end was 1, so ini < end. given the case that
	    	// ..now its true that ini > end, it means that its an outer line.
	    	
	    	// If its not a completely outer line, it means that it has a part inside
	    	// ..the clipping area, comprised of a pair of new x and y coordinates, so
	    	// nx_ = new x 1~2, ny_ = new y 1~2
	    	l1 = new Line(nx1, ny1, nx2, ny2);
	    	l1.setType("INNER");
	    	arr.add(l1);
	        if(ini > 0){
	        	// If after clipping the original u (ini) is modified, it means that 
	        	// ..the line has an origin outside the box, so store that line that goes
	        	// ..from the original origin to the new clipped origin to draw it separa-
	        	// ..tely in another color later.
	        	l2 = new Line(x1, y1, nx1, ny1);
	        	l2.setType("OUTER");
	        	arr.add(l2);
	        }
	        if(end < 1){
	        	// If after clipping the original u (end) is modified, it means that 
	        	// ..the line has an ending outside the box, so store that line that goes
	        	// ..from the original ending to the new clipped ending to draw it separa-
	        	// ..tely in another color later.
	        	l3 = new Line(nx2, ny2, x2, y2);
	        	l3.setType("OUTER");
	        	arr.add(l3);
	        }
	   } 
	   else{
	    	l1 = new Line(x1, y1, x2, y2);
	    	l1.setType("OUTER");
	    	arr.add(l1);
	   }
	   return arr;
	}
	
	// COHEN-SUTHERLAND LINE-CLIPPING METHOD
	public ArrayList<Line> splitCohenSuther(Line line){
		int x1 = line.getX1(), y1 = line.getY1();
		int x2 = line.getX2(), y2 = line.getY2();
		ArrayList<Line> arr = new ArrayList<Line>();
		// List to store the results. Doing this permits to return all the pieces
		// of the clipped line, no matter if one, two or three. This is also to
		// avoid the "lazy" way of drawing the complete line and then the clipped
		// line above it!
		Stack<Line> stack = new Stack<Line>();
		stack.push(line);
		while(stack.size() != 0){
			Line newLine;
			newLine = stack.pop(); 
			x1 = newLine.getX1(); y1 = newLine.getY1();
			x2 = newLine.getX2(); y2 = newLine.getY2();
			if(bothInside(x1, y1, x2, y2) == true){
				// Trivial Line Accept (both ends of Line are inside area)
				newLine.setType("INNER");
				arr.add(newLine); // Add to resulting list, don't add anything to stack
				continue;
			}
			if(bothSameSide(x1, y1, x2, y2) == true){
				// Trivial Line Reject (both ends of Line are on the same
				// outer side of the area)
				newLine.setType("OUTER");
				arr.add(newLine); // Add to resulting list, don't add anything to stack
				continue;
			}
			double nx = 0, ny = 0;
			// At this stage, at least one of the points is outside the box, so if the
			// ..beginning point is inside of it, swap it with the ending point so to
			// ..ensure that the beginning point will always be outside the box.
			if(isInsideBox(x1, y1) == true){
				int temp;
				temp = x1; x1 = x2; x2 = temp;
				temp = y1; y1 = y2; y2 = temp;
			}
			// y - y1 = dy/dx * (x - x1)
			// . . .. ... ..... ........
			// y = y1 + dy/dx * (x - x1)
			// x = x1 + dx/dy * (y - y1)
			// x and y are known, target box limits
			double dx = x2 - x1;
			double dy = y2 - y1;
			if(upOfBox(y1) == true){
				nx = x1 + (dx / dy) * (maxY-y1);
				ny = maxY;
			}
			else if(downOfBox(y1) == true){
				nx = x1 + (dx / dy) * (minY-y1);
				ny = minY;
			}
			else if(rightOfBox(x1) == true){
				ny = y1 + (dy / dx) * (maxX-x1);
				nx = maxX;
			}
			else if(leftOfBox(x1) == true){
				ny = y1 + (dy / dx) * (minX-x1);
				nx = minX;
			}
			int xx = (int)Math.round(nx);
			int yy = (int)Math.round(ny);
			line = new Line(xx, yy, x1, y1);
			arr.add(line);
			stack.push(new Line(xx, yy, x2, y2));
		}
		return arr;
	}
	public boolean isInsideBox(int x, int y){
		//p is 0000
		return rightOfBox(x) == false &&
		       downOfBox(y)  == false &&
		       leftOfBox(x)  == false &&
		       upOfBox(y)    == false;
	}
	private boolean bothSameSide(int x1, int y1, int x2, int y2){
		//trivial reject: p1 & p2 != 0
		//p1 and p2 have at least a 1 in the same position
		return rightOfBox(x1) == true && rightOfBox(x2) == true ||
		       downOfBox(y1)  == true && downOfBox(y2)  == true ||
		       leftOfBox(x1)  == true && leftOfBox(x2)  == true ||
		       upOfBox(y1)    == true && upOfBox(y2)    == true;
	}
	private boolean bothInside(int x1, int y1, int x2, int y2){
		//trivial accept
		//p1 and p2 are both 0000 / 0000
		return isInsideBox(x1,y1) == true && 
		       isInsideBox(x2,y2) == true;
	}
	private boolean leftOfBox(int x){
		return x < minX;
	}
	private boolean rightOfBox(int x){
		return x > maxX;
	}
	private boolean upOfBox(int y){
		return y > maxY;
	}
	private boolean downOfBox(int y){
		return y < minY;
	}
}

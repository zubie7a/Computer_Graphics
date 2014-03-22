import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame{

	private int dimX;
	private int dimY;
	private OptionPanel options;
	private DrawingPanel canvas;
	
	public Window(){
		dimX = 700;
		dimY = 700;
		this.setResizable(false);
		options = new OptionPanel(this);
		canvas = new DrawingPanel(this);
		this.setLayout(new BorderLayout());
		this.add(options, BorderLayout.SOUTH);
		this.add(canvas, BorderLayout.CENTER);
		this.setSize(new Dimension(dimX, dimY));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Challenge #1 - Bresenham Lines / Circles");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[]Args){
		Window window = new Window();
	}
	
	public int getDimX(){
		return dimX;
	}
	
	public int getDimY(){
		return dimY;
	}
	
	//This method passes messages from OptionPanel to DrawingPanel
	public void changeStatus(String newStatus){
		canvas.changeStatus(newStatus); 
	}
}

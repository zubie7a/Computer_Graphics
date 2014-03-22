import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends JFrame{

	private OptionPanel options;
	private DrawingPanel canvas;
	private int dimX;
	private int dimY;
	
	public Window(){
		dimX = 700;
		dimY = 700;
		this.setResizable(false);
		canvas = new DrawingPanel(this);
		options = new OptionPanel(this);
		this.setLayout(new BorderLayout());
		this.add(options, BorderLayout.SOUTH);
		this.add(canvas, BorderLayout.CENTER);
		this.setSize(new Dimension(dimX, dimY));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Challenge #2 - Cohen-Sutherland, Liang-Barsky Line Clipping");
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
	
	//These methods passes messages from OptionPanel to DrawingPanel or viceversa
	public void changeStatus(String newStatus){
		canvas.changeStatus(newStatus); 
	}
	public void makeLines(){
		canvas.makeLines();
	}
	public void updateTime(String s){
		options.updateTime(s);
	}
	public void setLines(String s){
		options.setLines(s);
	}
}

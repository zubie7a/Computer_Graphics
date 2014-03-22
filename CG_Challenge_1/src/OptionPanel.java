import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class OptionPanel extends JPanel implements ActionListener{
	private final int dSUTHER = 1; // A group of constant values for using
	private final int dBARSKY = 2; // ..in the switch/case that responds to
	private final int dLINES = 3;  // ..actions listened from the buttons
	private String SUTHER = "1";   // A group of action commands that will be
	private String BARSKY = "2";   // ..given to a set of buttons, which the
	private String LINES = "3";    // ..program will listen and act accordingly
	private JTextField tSuther;    // A pair of textfields for putting the time
	private JTextField tBarsky;    // ..each algorithm takes to clip some lines
	private JButton suther;        // A button for using the Cohen-Sutherland algorithm
	private JButton barsky;        // A button for using the Liang-Barksy algorithm
	private JButton lines;         // A button for changing the amount of lines in screen
	private Window window;         // A reference to the parent object
	private String status;         // The currently used algorithm in the application
	private JLabel linum;          // A label that will contain the number of current lines
	public OptionPanel(Window window){
		this.setBackground(Color.DARK_GRAY);
		this.window = window;
		this.setLayout(new GridLayout(2, 3)); // 2 rows, 3 columns
		// Create the buttons
		suther = new JButton("Cohen-Sutherland");
		barsky = new JButton("Liang-Barsky");
		lines = new JButton("Set Lines");
		// Set the action commands that will identify each one
		suther.setActionCommand(SUTHER);
		barsky.setActionCommand(BARSKY);
		lines.setActionCommand(LINES);
		// Configure this object to listen to these buttons
		suther.addActionListener(this);
		barsky.addActionListener(this);
		lines.addActionListener(this);
		// Configure the textfields
		tSuther = new JTextField("- ms");
		tBarsky = new JTextField("- ms");
		tSuther.setEditable(false);
		tBarsky.setEditable(false);
		// Add the objects in the desired order to the 2 x 3 GridLayout
		this.add(suther);
		this.add(lines);
		this.add(barsky);
		this.add(tSuther);
		linum = new JLabel("");
		this.add(linum);
		this.add(tBarsky);
		status = "SUTHER"; // Default application algorithm is Cohen-Sutherland
		setActiveButton();
	}

	public void actionPerformed(ActionEvent arg0){
		int command = Integer.parseInt(arg0.getActionCommand());
		switch (command){
		case dSUTHER:
			status = "SUTHER";
			setActiveButton();
			window.changeStatus(status);
			break;
		case dBARSKY:
			status = "BARSKY";
			setActiveButton();
			window.changeStatus(status);
			break;
		case dLINES:
			window.makeLines();
			tBarsky.setText("- ms");
			tSuther.setText("- ms");
			window.changeStatus(status);
			break;
		default:
			break;
		}
	}
	
	private void setActiveButton(){
		if(status == "SUTHER"){
			barsky.setForeground(Color.BLACK);
			suther.setForeground(Color.RED);
		}
		else if(status == "BARSKY"){
			barsky.setForeground(Color.RED);
			suther.setForeground(Color.BLACK);
		}
	}
	
	public void setLines(String s){
		linum.setForeground(Color.WHITE);
		linum.setText(s + " Lines");
	}
	
	public void updateTime(String s){
		s += " ms";
		if(status == "SUTHER") tSuther.setText(s);
		if(status == "BARSKY") tBarsky.setText(s);
	}
}

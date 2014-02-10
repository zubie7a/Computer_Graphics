import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JButton;


public class OptionPanel extends JPanel implements ActionListener{
	private final int dCIRCLER = 2;
	private final int dLINER = 1;
	private String CIRCLER = "2";
	private String LINER = "1";
	private JButton circler;
	private JButton liner;
	private Window window;

	public OptionPanel(Window window){
		this.window = window;
		this.setLayout(new GridLayout(1,2)); // 1 row, 3 columns
		// Create the buttons
		circler = new JButton("Draw Circles");
		liner = new JButton("Draw Lines");
		// Set the action commands that will identify each one
		circler.setActionCommand(CIRCLER);
		liner.setActionCommand(LINER);
		// Configure this object to listen to these buttons
		circler.addActionListener(this);
		liner.addActionListener(this);
		// Add the objects in the desired order to the 1 x 2 GridLayout
		this.add(liner);
		this.add(circler);
	}

	public void actionPerformed(ActionEvent arg0) {
		int command = Integer.parseInt(arg0.getActionCommand());
		switch(command){
		case dLINER:
			window.changeStatus("LINER");
			break;
		case dCIRCLER:
			window.changeStatus("CIRCLER");
			break;
		default: break;
		}
	}
}

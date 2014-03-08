import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Framecito extends JFrame{
	
	private Window window;
	private Panel panel;
	
	@SuppressWarnings("deprecation")
	public Framecito(Window window){
		this.window = window;
		panel = new Panel();
		this.add(panel);
		this.setVisible(true);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setSize(new Dimension(550, 100));
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocation(window.location().x, window.location().y+42);
	}
	
	private class Panel extends JPanel implements ActionListener, ChangeListener{
		float axy;
		float sx, sy;
		float tx, ty;
		JTextField taxy;
		JTextField tsx, tsy;
		JTextField ttx, tty;
		JButton rot;
		JButton tra;
		JButton sca;
		JCheckBox alp;
		
		private Panel(){
			JLabel label;
			this.setBackground(new Color(84, 84, 84));
			this.setLayout(new GridLayout(3,5));
			// GridLayout is perfect for something like a button control grid
			
			// GUI elements for translating control
			label = new JLabel(" Trans X:");
			label.setForeground(Color.WHITE);
			this.add(label);
			ttx = new JTextField("0");
			this.add(ttx);
			label = new JLabel("Trans Y:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tty = new JTextField("0");
			this.add(tty);
			tra = new JButton("Trans");
			tra.setActionCommand("TRANSLATE");
			tra.addActionListener(this);
			this.add(tra);
			
			// GUI elements for scaling control
			label = new JLabel(" Scale X:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tsx = new JTextField("1.0");
			this.add(tsx);
			label = new JLabel("Scale Y:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tsy = new JTextField("1.0");
			this.add(tsy);
			sca = new JButton("Scale");
			sca.setActionCommand("SCALE");
			sca.addActionListener(this);
			this.add(sca);
						
			// GUI elements for rotating control
			label = new JLabel(" Angle XY:");
			label.setForeground(Color.WHITE);
			this.add(label);
			taxy = new JTextField("0.0");
			this.add(taxy);
			
			label = new JLabel("Alpha");
			label.setForeground(Color.WHITE);
			this.add(label);
			
			alp = new JCheckBox();
			alp.addChangeListener(this);
			alp.setSelected(false);
			this.add(alp);
			
			rot = new JButton("Rotate");
			rot.setActionCommand("ROTATE");
			rot.addActionListener(this);
			this.add(rot);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			// This is called once some GUI element with an ActionCommand is used
			axy = 0;
			sx = sy = 0;
			tx = ty = 0;
			if(arg0.getActionCommand().equals("ROTATE")){
				try{
					axy = Float.parseFloat(taxy.getText());
				}
				catch(Exception e){
					axy = 0;
					taxy.setText("0.0");
				}
			}
			if(arg0.getActionCommand().equals("SCALE")){
				try{
					sx = Float.parseFloat(tsx.getText());
					if(sx == 0){
						sx = 1;
						tsx.setText("1.0");
					}
				}
				catch(Exception e){
					sx = 1;
					tsx.setText("1.0");
				}
				try{
					sy = Float.parseFloat(tsy.getText());
				}
				catch(Exception e){
					sy = 1;
					tsy.setText("1.0");
				}
				if(sy == 0){
					sy = 1;
					tsy.setText("1.0");
				}
			}
			if(arg0.getActionCommand().equals("TRANSLATE")){
				try{
					tx = Float.parseFloat(ttx.getText());
				}
				catch(Exception e){
					tx = 0;
					ttx.setText("0.0");
				}
				try{
					ty = Float.parseFloat(tty.getText());
				}
				catch(Exception e){
					ty = 0;
					tty.setText("0.0");
				}
			}
			window.updateValues2D(axy, sx, sy, tx, ty, arg0.getActionCommand());
		}

		public void stateChanged(ChangeEvent arg0) {
		}
	}

	public boolean getAlph(){
		return panel.alp.isSelected();
	}

}

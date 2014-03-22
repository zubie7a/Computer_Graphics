import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class Framecito extends JFrame {

	private Window window;
	private Panel panel;

	public Framecito(Window window) {
		this.window = window;
		panel = new Panel();
		this.add(panel);
		this.setVisible(true);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setSize(new Dimension(550, 100));
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocation(window.location().x, window.location().y + 42);
	}

	private class Panel extends JPanel implements ActionListener{
		float axy, ayz, azx;
		float sx, sy, sz;
		float tx, ty, tz;
		JTextField taxy, tayz, tazx;
		JTextField tsx, tsy, tsz;
		JTextField ttx, tty, ttz;
		JButton rot;
		JButton tra;
		JButton sca;
		JButton rea;

		private Panel() {
			JLabel label;
			this.setBackground(new Color(84, 84, 84));
			this.setLayout(new GridLayout(4, 7));
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
			label = new JLabel("Trans Z:");
			label.setForeground(Color.WHITE);
			this.add(label);
			ttz = new JTextField("0");
			this.add(ttz);
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
			label = new JLabel("Scale Z:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tsz = new JTextField("1.0");
			this.add(tsz);
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
			label = new JLabel("Angle YZ:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tayz = new JTextField("0.0");
			this.add(tayz);
			label = new JLabel("Angle ZX:");
			label.setForeground(Color.WHITE);
			this.add(label);
			tazx = new JTextField("0.0");
			this.add(tazx);
			rot = new JButton("Rotate");
			rot.setActionCommand("ROTATE");
			rot.addActionListener(this);
			this.add(rot);

			// GUI elements for default shape or external input
			this.add(new JLabel(""));
			this.add(new JLabel(""));
			this.add(new JLabel(""));
			this.add(new JLabel(""));
			this.add(new JLabel(""));
			this.add(new JLabel(""));

			rea = new JButton("Read Input");
			rea.setActionCommand("READ");
			rea.addActionListener(this);
			this.add(rea);
		}

		public void actionPerformed(ActionEvent arg0) {
			axy = ayz = azx = 0;
			sx = sy = sz = 0;
			tx = ty = tz = 0;
			if (arg0.getActionCommand().equals("READ")) {
				if (window.mode.equals("default")) {
					window.mode = "input";
					rea.setText("Default");
				} else {
					window.mode = "default";
					rea.setText("Read Input");
				}
				window.initializer();
				return;
			}
			if (arg0.getActionCommand().equals("ROTATE")) {
				try {
					axy = Float.parseFloat(taxy.getText());
				} catch (Exception e) {
					axy = 0;
					taxy.setText("0.0");
				}
				try {
					ayz = Float.parseFloat(tayz.getText());
				} catch (Exception e) {
					ayz = 0;
					tayz.setText("0.0");
				}
				try {
					azx = Float.parseFloat(tazx.getText());
				} catch (Exception e) {
					azx = 0;
					tazx.setText("0.0");
				}
			}
			if (arg0.getActionCommand().equals("SCALE")) {
				try {
					sx = Float.parseFloat(tsx.getText());
					if (sx == 0) {
						sx = 1;
						tsx.setText("1.0");
					}
				} catch (Exception e) {
					sx = 1;
					tsx.setText("1.0");
				}
				try {
					sy = Float.parseFloat(tsy.getText());
					if (sy == 0) {
						sy = 1;
						tsy.setText("1.0");
					}
				} catch (Exception e) {
					sy = 1;
					tsy.setText("1.0");
				}
				try {
					sz = Float.parseFloat(tsz.getText());
					if (sz == 0) {
						sz = 1;
						tsz.setText("1.0");
					}
				} catch (Exception e) {
					sz = 1;
					tsz.setText("1.0");
				}
			}
			if (arg0.getActionCommand().equals("TRANSLATE")) {
				try {
					tx = Float.parseFloat(ttx.getText());
				} catch (Exception e) {
					tx = 0;
					ttx.setText("0.0");
				}
				try {
					ty = Float.parseFloat(tty.getText());
				} catch (Exception e) {
					ty = 0;
					tty.setText("0.0");
				}
				try {
					tz = Float.parseFloat(ttz.getText());
				} catch (Exception e) {
					tz = 0;
					ttz.setText("0.0");
				}
			}
			window.updateValues3D(axy, ayz, azx, sx, sy, sz, tx, ty, tz, arg0.getActionCommand());
		}
	}

}

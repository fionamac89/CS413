package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartScreen extends JFrame {

	public StartScreen() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		pack();
		setSize(200, 200);
		setVisible(true);

	}

	private void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		panel.add(new JButton("Random"));
		panel.add(new JButton("Simon Says"));
		panel.add(new JButton("SmartPhone Controlled"));
		add(panel);
	}

}

package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ButtonListener;

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
		ActionListener al = new ButtonListener();
		panel.add(new ESButton("Random", al));
		panel.add(new ESButton("Simon Says", al));
		panel.add(new ESButton("SmartPhone Controlled", al));
		add(panel);
	}

}

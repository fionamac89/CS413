package view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartScreen extends JFrame {

	public StartScreen(ActionListener al) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init(al);
		pack();
		setSize(200, 200);
		setVisible(true);

	}

	private void init(ActionListener al) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		panel.add(new ESButton("Random", al));
		panel.add(new ESButton("Simon Says", al));
		panel.add(new ESButton("SmartPhone Controlled", al));
		add(panel);
	}

}

package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GameManager;


public class HighScoreFrame extends JFrame {

	private GameManager g;

	public HighScoreFrame(GameManager g) {
		this.g = g;
		init();
		pack();
		setSize(200, 200);
		setVisible(true);
	}

	private void init() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		label.setText(g.getScoreBoard().getHighestScores());
		panel.add(label);
		this.getContentPane().add(panel);
	}
}

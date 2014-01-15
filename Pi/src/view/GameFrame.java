package view;

import games.GameMode;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import comms.model.GameManager;
import comms.model.Score;

public class GameFrame extends JFrame {

	private GameManager g;

	public GameFrame(GameManager g) {
		this.g = g;
		init();
		pack();
		setSize(200, 200);
		setVisible(true);
	}

	private void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		panel.add(new ESButton("Play", new PlayListener(g)));
		panel.add(new ScorePanel((GameMode) g.getMode()));
		this.getContentPane().add(panel);
	}

	private class PlayListener implements ActionListener {
		private GameManager g;

		public PlayListener(GameManager g) {
			this.g = g;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// change to swap button with a stop button
			new Thread(new GameRunner()).start();
		}
	}

	private class GameRunner implements Runnable {

		@Override
		public void run() {
			Score s = g.getMode().playGame();
			g.getScoreBoard().addScoreName("Test", s);
		}

	}
}

package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.GameManager;

import view.GameFrame;
import view.HighScoreFrame;


public class ButtonListener implements ActionListener {

	private GameManager g;

	public ButtonListener(GameManager g) {
		this.g = g;
	}

	/**
	 * @param args
	 */

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case "Random":
		case "Simon Says":
		case "SmartPhone Controlled":
			g.newGame(e.getActionCommand());
			new GameFrame(g);
			break;
		case "Save Scores":
			System.out.println("Save Scores");
			g.saveScores();
			break;
		case "High Scores":
			System.out.println("High Scores");
			new HighScoreFrame(g);
			break;

		}

	}

}

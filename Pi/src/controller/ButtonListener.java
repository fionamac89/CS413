package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.GameFrame;

import comms.model.GameManager;

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
		g.newGame(e.getActionCommand());
		new GameFrame(g);
		// long l = g.getMode().playGame();
		// TODO Inpout user name
		// g.getScoreBoard().addScoreName("Test", l);
	}

}

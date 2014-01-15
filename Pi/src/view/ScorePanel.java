package view;

import games.GameMode;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel implements Observer {
	private JLabel label;
	private GameMode game;

	public ScorePanel(GameMode game) {
		super();
		init();
		game.addObserver(this);
		this.game = game;
	}

	private void init() {
		label = new JLabel("Score:\t");
	}

	@Override
	public void update(Observable o, Object arg) {
		label.setText("Score:\t" + game.getScore());
	}
}

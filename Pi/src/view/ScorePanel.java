package view;

import games.GameMode;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel implements Observer {
	private JLabel totalHits;
	private JLabel averageResponseTime;
	private JLabel totalResponseTime;

	private GameMode game;

	public ScorePanel(GameMode game) {
		super();
		init();
		game.addObserver(this);
		this.game = game;
	}

	private void init() {
		totalHits = new JLabel("Total Hits:\t");
		add(totalHits);
		averageResponseTime = new JLabel("Average Response Time:\t");
		add(averageResponseTime);
		totalResponseTime = new JLabel("Total Response Time:\t");
		add(totalResponseTime);
	}

	@Override
	public void update(Observable o, Object arg) {
		totalHits.setText("Total Hits:\t" + game.getScore().getTotalPulses());
		averageResponseTime.setText("Average Response Time:\t"
				+ game.getScore().getAverageResponseTime());
		totalResponseTime.setText("Total Response Time:\t"
				+ game.getScore().getSumOfResponses());
		System.out.println("Score Updated");
	}
}

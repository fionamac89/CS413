package comms.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

	private List<Score> scores;

	public ScoreBoard() {
		// TODO Display Scores Somewhere (Maybe from menu)
		scores = new ArrayList<Score>();

	}

	public void addScoreName(String name, Score score) {
		score.setName(name);
		scores.add(score);
	}

}

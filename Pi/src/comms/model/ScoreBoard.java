package comms.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {

	private List<String> names;
	private List<Long> scores;

	public ScoreBoard() {
		// TODO Display Scores Somewhere (Maybe from menu)
		names = new ArrayList<String>();
		scores = new ArrayList<Long>();

	}

	public void addScoreName(String name, long score) {
		names.add(name);
		scores.add(score);
	}

}

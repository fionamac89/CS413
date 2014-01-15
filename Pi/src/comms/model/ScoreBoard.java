package comms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Score> scores;

	public ScoreBoard() {
		scores = new ArrayList<Score>();

	}

	public void addScoreName(String name, Score score) {
		score.setName(name);
		scores.add(score);
	}

	public String getHighestScores() {

		Collections.sort(scores, new MyIntComparable());
		String s = "<HTML> <U>High Scores</U><br/>";
		for (int i = 0; (i < 5) && i < scores.size(); i++) {
			s += scores.get(i).getName() + "\t"
					+ scores.get(i).getAverageResponseTime() + "<br/>";
		}
		return s + "<HTML>";

	}

	private class MyIntComparable implements Comparator<Score> {

		@Override
		public int compare(Score o1, Score o2) {
			if (o1.getAverageResponseTime() < o2.getAverageResponseTime()) {
				return 1;
			}
			return -1;
		}
	}

}

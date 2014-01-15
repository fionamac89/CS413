package games;

import java.util.Observer;

import comms.model.Score;

public interface Game extends Observer {
	public Score playGame();
}

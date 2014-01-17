package games;

import java.util.Observer;

import model.Score;


public interface Game extends Observer {
	public Score playGame();
}

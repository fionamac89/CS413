import games.RemoteGame;

import comms.arduino.ArduinoInputOutput;

public class Run {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		// StartScreen start = new StartScreen();
		// start.setVisible(true);
		// Game g = new RandomMode(5, new ArduinoInputOutput());
		// g.playGame();

		// SimonSaysMode g = new SimonSaysMode(5, new ArduinoInputOutput());
		// g.startRound();
		RemoteGame r = new RemoteGame(5, new ArduinoInputOutput());
		r.playGame();
	}
}

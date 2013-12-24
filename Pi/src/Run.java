import games.RandomMode;

import comms.arduino.ArduinoInputOutput;

public class Run {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		// StartScreen start = new StartScreen();
		// start.setVisible(true);
		new RandomMode(5, new ArduinoInputOutput()).playGame();
	}

}

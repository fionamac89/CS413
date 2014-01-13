public class Run {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// StartScreen start = new StartScreen();
		// start.setVisible(true);
		// Game g = new RandomMode(5, new ArduinoInputOutput());
		// g.playGame();

		// SimonSaysMode g = new SimonSaysMode(5, new ArduinoInputOutput());
		// g.startRound();
		// RemoteGame r = new RemoteGame(5, new ArduinoInputOutput());
		// r.playGame();

		SerialOut so = new SerialOut("/dev/ttyS99");
		while (true) {
			so.sendString("1");
			Thread.sleep(2000);
		}

	}
}

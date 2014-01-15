import view.StartScreen;

import comms.model.GameManager;

import controller.ButtonListener;

public class Run {

	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		// TODO add save menu button
		GameManager g = new GameManager("/dev/ttyS97");
		StartScreen start = new StartScreen(new ButtonListener(g));
		start.setVisible(true);
		// Game g = new RandomMode(3, new ArduinoSerialIO("/dev/ttyS97"));
		// g.playGame();
		//
		// SimonSaysMode g = new SimonSaysMode(5, new ArduinoSerialIO(
		// "/dev/ttyS97"));
		// g.startRound();
		// RemoteGame r = new RemoteGame(5, new ArduinoInputOutput());
		// r.playGame();

		// SerialOut so = new SerialOut("/dev/ttyS99");
		// while (true) {
		// so.sendString("1");
		// Thread.sleep(2000);
		// }

	}
}

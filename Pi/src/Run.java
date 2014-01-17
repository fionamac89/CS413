import model.GameManager;
import view.StartScreen;


import controller.ButtonListener;

public class Run {

	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		GameManager g = new GameManager("/dev/ttyS97");
		StartScreen start = new StartScreen(new ButtonListener(g));
		start.setVisible(true);
	}
}

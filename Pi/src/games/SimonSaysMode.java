package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import comms.arduino.ArduinoSerialIO;
import comms.model.Data;
import comms.model.Response;
import comms.model.Score;

public class SimonSaysMode extends GameMode {

	private List<Integer> sent;
	private List<Integer> recived;
	private int roundNo = 0;

	public SimonSaysMode(int noOfPads, ArduinoSerialIO arduinoInput) {
		super(noOfPads, arduinoInput);
	}

	@Override
	public Score playGame() {
		super.playGame();
		startRound();
		return score;
	}

	public void startRound() {
		roundNo++;
		sent = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 0; i <= roundNo; i++) {
			sent.add(random.nextInt(noOfPads));
		}
		// System.out.println("Sent:\t" + sent);

		for (Integer i : sent) {
			arduinoInput.sendCommand(new Data(i));
		}
		recived = new ArrayList<Integer>();
	}

	@Override
	public void update(Observable o, Object arg) {
		Response currentResponse;
		if ((currentResponse = arduinoInput.getResponse()) != null) {

			recived.add(currentResponse.getPadNo());
			if (sent.size() == recived.size()) {
				System.out.println(recived);
				boolean matching = true;
				for (int i = 0; i < sent.size(); i++) {
					matching = matching && (sent.get(i) == recived.get(i));

				}
				if (matching) {
					score.increaseResponseTimes((long) sent.size());
					try {
						// TODO Fix this thread, maybe flag a notification
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					startRound();
				}

			}

		}
	}
}

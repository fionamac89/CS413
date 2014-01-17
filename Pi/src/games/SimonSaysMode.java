package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import model.Config;
import model.Data;
import model.Response;
import model.Score;

import comms.arduino.ArduinoSerialIO;

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
		arduinoInput.sendConfig(new Config().appendChar('s'));
		// Alex code start
		// Wait for signal transition
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startRound();
		System.out.println("Game Complete");
		return score;
	}

	public void startRound() {
		sent = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 0; i <= roundNo; i++) {
			sent.add(random.nextInt(noOfPads));
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Integer i : sent) {
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			arduinoInput.sendCommand(new Data(i));

		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		arduinoInput.sendConfig(new Config().appendChar('p'));
		roundNo++;

		recived = new ArrayList<Integer>();
		boolean matching = true;
		while (!(sent.size() == recived.size())) {

			Response currentResponse;
			while (true) {
				currentResponse = arduinoInput.getResponse();
				if (currentResponse != null)
					break;
			}
			recived.add(currentResponse.getPadNo());

			// check progress so far
			if (!isMatchingSoFar()) {
				matching = false;
				break;
			}

		}
		if (!matching) {
			arduinoInput.sendConfig(new Config().appendChar('q'));
		} else {
			arduinoInput.sendConfig(new Config().appendChar('c'));
			if (roundNo < 5) {
				startRound();
			}
		}

	}

	private boolean isMatchingSoFar() {
		boolean matching = true;

		for (int i = 0; (i < recived.size()); i++) {
			if (recived.get(i) != null) {
				matching = matching && (sent.get(i) == recived.get(i));
			}
		}
		return matching;
	}

	@Override
	public void update(Observable o, Object arg) {
	}

}

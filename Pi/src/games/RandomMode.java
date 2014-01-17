package games;

import java.util.Observable;
import java.util.Random;

import model.Config;
import model.Data;
import model.Response;
import model.Score;

import comms.arduino.ArduinoSerialIO;

public class RandomMode extends GameMode {
	private Random random;

	private long startTime;

	public RandomMode(int noOfPads, ArduinoSerialIO ip) {
		super(noOfPads, ip);
		// arduinoInput.addObserver(this);
		random = new Random();
	}

	public Score playGame() {
		super.playGame();
		startTime = System.currentTimeMillis();
		// arduinoInput.sendCommand(new Data(printme));
		arduinoInput.sendConfig(new Config().appendChar('d'));
		nextFlash();
		return score;
	}

	private void nextFlash() {
		int printme = random.nextInt(noOfPads);
		arduinoInput.sendCommand(new Data(printme));
		System.out.println("Sent: " + printme);

		Response currentResponse;

		while (true) {
			currentResponse = arduinoInput.getResponse();
			if (currentResponse != null)
				break;
		}

		if (currentResponse.getResponseTime() < 3000) {
			score.increaseResponseTimes(currentResponse.getResponseTime());
			score.increaseTotalPulse();
			System.out.println("Score Changed");
			setChanged();
			notifyObservers();
		}
		System.out.println(System.currentTimeMillis() - startTime);
		if (!((System.currentTimeMillis() - startTime) > 30000l)) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextFlash();
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			arduinoInput.sendConfig(new Config().appendChar('q'));
		}

	}

	@Override
	public void update(Observable o, Object arg) {
	}

}

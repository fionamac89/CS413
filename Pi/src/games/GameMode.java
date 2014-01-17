package games;

import java.util.Observable;

import model.Response;
import model.Score;

import comms.arduino.ArduinoSerialIO;

public abstract class GameMode extends Observable implements Game {

	protected int noOfPads;
	protected ArduinoSerialIO arduinoInput;
	protected Score score;

	public GameMode(int noOfPads, ArduinoSerialIO arduinoInput) {
		this.noOfPads = noOfPads;
		this.arduinoInput = arduinoInput;
		this.score = new Score();
		arduinoInput.addObserver(this);
	}

	@Override
	public Score playGame() {
		score = new Score();
		// Sending null to init the socket
		arduinoInput.init();
		try {
			Thread.sleep((long) (1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		Response currentResponse;
		// System.out.println("Response Reterived");
		if ((currentResponse = arduinoInput.getResponse()) != null) {
			score.increaseResponseTimes(currentResponse.getResponseTime());
			score.increaseTotalPulse();
			System.out.println("Score Changed");
			setChanged();
			notifyObservers();
		}
	}

	public Score getScore() {
		return score;
	}

}
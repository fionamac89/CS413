package games;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

import comms.arduino.ArduinoInputOutput;
import comms.model.Response;

public abstract class GameMode implements Game {

	protected int noOfPads;
	protected ArduinoInputOutput arduinoInput;
	protected AtomicLong score;

	public GameMode(int noOfPads, ArduinoInputOutput arduinoInput) {
		this.noOfPads = noOfPads;
		this.arduinoInput = arduinoInput;
		this.score = new AtomicLong();
		arduinoInput.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		Response currentResponse;
		if ((currentResponse = arduinoInput.getResponse()) != null) {
			System.out.println(currentResponse.toString());
			System.out.println("Score:\t"
					+ score.addAndGet(currentResponse.getResponseTime()));
		}
	}
}
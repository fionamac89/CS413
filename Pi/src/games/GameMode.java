package games;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

import comms.arduino.ArduinoSerialIO;
import comms.model.Data;
import comms.model.Response;

public abstract class GameMode extends Observable implements Game {

	protected int noOfPads;
	protected ArduinoSerialIO arduinoInput;
	protected AtomicLong score;

	public GameMode(int noOfPads, ArduinoSerialIO arduinoInput) {
		this.noOfPads = noOfPads;
		this.arduinoInput = arduinoInput;
		this.score = new AtomicLong();
		arduinoInput.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		Response currentResponse;
		if ((currentResponse = arduinoInput.getResponse()) != null) {
			// System.out.println(currentResponse.toString());
			// System.out.println("Score:\t"+
			score.addAndGet(currentResponse.getResponseTime());
		}
	}

	@Override
	public long playGame() {
		score = new AtomicLong();
		// Alex's code
		// TODO First command is skipped. May be the protocol needs to
		// synchronise with the clock first?
		arduinoInput.sendCommand(new Data(1));
		try {
			Thread.sleep((long) (1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		arduinoInput.sendCommand(new Data(1));
		try {
			Thread.sleep((long) (1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Alex's code end

		return 0;
	}

	public long getScore() {
		return score.get();
	}

}
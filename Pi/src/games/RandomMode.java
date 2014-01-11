package games;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import comms.arduino.ArduinoInputOutput;
import comms.model.Data;

public class RandomMode extends GameMode {

	public RandomMode(int noOfPads, ArduinoInputOutput ip) {
		super(noOfPads, ip);
		arduinoInput.addObserver(this);
	}

	public long playGame() {
		score = new AtomicLong();
		double secondsBetweenSignals = 1;
		Random random = new Random();
		boolean playing = true;
		while (playing) {
			// TODO look at ensuring that only lights that are off are used
			// TODO Max running time
			arduinoInput.sendCommand(new Data(random.nextInt(noOfPads)));
			secondsBetweenSignals = secondsBetweenSignals * 0.9;
			try {
				Thread.sleep((long) (secondsBetweenSignals * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return score.get();
	}

}

package games;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import comms.arduino.ArduinoSerialIO;
import comms.model.Data;

public class RandomMode extends GameMode {

	public RandomMode(int noOfPads, ArduinoSerialIO ip) {
		super(noOfPads, ip);
		arduinoInput.addObserver(this);
	}

	public long playGame() {
		score = new AtomicLong();
		double secondsBetweenSignals = 10;
		Random random = new Random();
		boolean playing = true;
		while (playing) {
			// TODO look at ensuring that only lights that are off are used
			// TODO Max running time
			Data tmp = new Data(1);
			arduinoInput.sendCommand(tmp);// random.nextInt(noOfPads)));
			// System.out.println("Sent");
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

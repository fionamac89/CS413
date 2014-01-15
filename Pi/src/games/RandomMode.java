package games;

import java.util.Random;

import comms.arduino.ArduinoSerialIO;
import comms.model.Data;
import comms.model.Score;

public class RandomMode extends GameMode {

	public RandomMode(int noOfPads, ArduinoSerialIO ip) {
		super(noOfPads, ip);
		arduinoInput.addObserver(this);
	}

	public Score playGame() {
		super.playGame();
		double secondsBetweenSignals = 5;
		Random random = new Random();
		boolean playing = true;
		while (playing) {
			// TODO look at ensuring that only lights that are off are used
			// TODO Max running time
			// TODO put minimum time so that reaction time doesn't get
			// impossible to meet
			int printme = random.nextInt(noOfPads);
			arduinoInput.sendCommand(new Data(printme));
			System.out.println("Sent: " + printme);
			secondsBetweenSignals = secondsBetweenSignals * 0.9;
			try {
				Thread.sleep((long) (secondsBetweenSignals * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return score;
	}
}

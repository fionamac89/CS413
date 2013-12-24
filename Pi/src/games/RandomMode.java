package games;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import comms.arduino.ArduinoInputOutput;
import comms.model.Data;
import comms.model.Response;

public class RandomMode implements GameMode {

	private int noOfPads;
	private ArduinoInputOutput arduinoInput;
	private AtomicInteger score;

	public RandomMode(int noOfPads, ArduinoInputOutput ip) {
		this.noOfPads = noOfPads;
		this.arduinoInput = ip;
		score = new AtomicInteger();
	}

	public int playGame() throws InterruptedException {
		AtomicInteger score = new AtomicInteger();
		double secondsBetweenSignals = 5;
		Random random = new Random();
		boolean playing = true;
		new ScoreResponses().start();
		while (playing) {
			arduinoInput.sendCommand(new Data(random.nextInt(noOfPads)));
			secondsBetweenSignals = secondsBetweenSignals * 0.9;
			Thread.sleep((long) (secondsBetweenSignals * 1000));
		}
		return score.get();
	}

	private class ScoreResponses extends Thread {

		@Override
		public void run() {
			super.run();
			while (true) {
				Response currentResponse;
				if ((currentResponse = arduinoInput.getResponse()) != null) {
					System.out.println("Score:\t"
							+ score.addAndGet(currentResponse.getScore()));
				}

			}

		}
	}
}

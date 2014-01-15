package test;

import java.util.concurrent.atomic.AtomicInteger;

import comms.arduino.ArduinoInputOutput;
import comms.arduino.Data;
import comms.arduino.Response;

public class ArduinoIODriver {
	/**
	 * This class was created in an effort to test the command protocol to the
	 * hardware interface.
	 */
	private AtomicInteger score;
	private ArduinoInputOutput aIO;

	public ArduinoIODriver() {
		aIO = new ArduinoInputOutput();
		score = new AtomicInteger();
		new OPThread().start();
		new IPThread().start();
	}

	private class OPThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				while (true) {
					Thread.sleep(1000);
					aIO.sendCommand(new Data(1));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class IPThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (true) {
				Response response = aIO.getResponse();
				if (response != null) {
					System.out.println(response.toResponseString());
					System.out.println("Score:\t" + score.addAndGet(1));
				}
			}
		}
	}

}

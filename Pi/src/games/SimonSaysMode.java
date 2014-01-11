package games;

import comms.arduino.ArduinoInputOutput;

public class SimonSaysMode extends GameMode {

	public SimonSaysMode(int noOfPads, ArduinoInputOutput arduinoInput) {
		super(noOfPads, arduinoInput);
	}

	@Override
	public int playGame() {
		return 0;
	}

}

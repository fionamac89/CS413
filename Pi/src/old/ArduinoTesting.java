import comms.arduino.ArduinoSerialIO;
import comms.model.Data;

public class ArduinoTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArduinoSerialIO as = new ArduinoSerialIO("/dev/ttyS90");
		as.sendCommand(new Data(1));

		as.sendCommand(new Data(1));

		as.sendCommand(new Data(1));

	}

}

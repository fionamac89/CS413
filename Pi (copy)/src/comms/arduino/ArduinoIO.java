package comms.arduino;

import comms.model.Command;
import comms.model.Response;

public interface ArduinoIO {

	public abstract void sendCommand(Command c);

	public abstract Response getResponse();

}
package comms.arduino;

import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import comms.model.Command;
import comms.model.Data;
import comms.model.Response;

public class ArduinoInputOutput extends Observable {

	private Queue<Response> inputBuffer;
	private Queue<Command> outputBuffer;

	public ArduinoInputOutput() {
		inputBuffer = new LinkedBlockingQueue<Response>();
		outputBuffer = new LinkedBlockingQueue<Command>();

		new InputProcessorThread().start();
	}

	public void sendCommand(Command c) {
		// TODO Send this using the Xbee Modules
		outputBuffer.offer(c);
		// new InputProcessorThread().start();
	}

	public Response getResponse() {
		return inputBuffer.poll();
	}

	// FIXME This is used to simulate the input from the arduino this will be
	// changed once hardware prototype is completed.
	private class InputProcessorThread extends Thread {

		public InputProcessorThread() {

		}

		@Override
		public void run() {
			super.run();
			// TODO Get Data From Arduino using the Xbee modules

			while (true) {
				Command outputCommand = outputBuffer.poll();
				if (outputCommand != null) {
					inputBuffer.offer(new Data(outputCommand.getPadNo(),
							new Long(10l)));
					setChanged();
					notifyObservers();
				}
			}

		}
	}
}

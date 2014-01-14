package comms.arduino;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Queue;
import java.util.TooManyListenersException;
import java.util.concurrent.LinkedBlockingQueue;

import comms.model.Command;
import comms.model.Data;
import comms.model.Response;

public class ArduinoSerialIO extends Observable implements ArduinoIO {

	private InputStream in;
	private BufferedWriter bw;
	private OutputStream out;

	private Queue<Response> inputBuffer;

	public ArduinoSerialIO(String portName) {
		inputBuffer = new LinkedBlockingQueue<Response>();
		try {
			init(portName);
		} catch (NoSuchPortException | PortInUseException
				| UnsupportedCommOperationException | IOException
				| TooManyListenersException e) {
			e.printStackTrace();
		}
	}

	private void init(String portName) throws NoSuchPortException,
			PortInUseException, UnsupportedCommOperationException, IOException,
			TooManyListenersException {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				bw = new BufferedWriter(new OutputStreamWriter(out));

				new Thread(new SerialReader(in)).start();
				// serialPort.addEventListener(new
				// SerialReaderEventListener(in));
				// serialPort.notifyOnDataAvailable(true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see comms.arduino.ArduinoIO#sendCommand(comms.model.Command)
	 */
	@Override
	public void sendCommand(Command c) {
		try {
			bw.write(c.getPadNo());
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see comms.arduino.ArduinoIO#getResponse()
	 */
	@Override
	public Response getResponse() {
		return inputBuffer.poll();
	}

	// FIXME This is used to simulate the input from the arduino this will be
	// changed once hardware prototype is completed.
	private class SerialReader extends Thread {

		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void run() {
			int i = 0;
			while (true) {
				try {
					String s = "";
					char c = (char) -1;
					while (c != 10) {
						c = (char) in.read();
						s += c;
					}

					if (s.length() > 0) {
						System.out.println(s);
						System.out.println("Read:\t" + i);
						i++;
						String[] data = s.replace("\n", "").split(", ");
						inputBuffer.add(new Data(Integer.parseInt(data[0]),
								Integer.parseInt(data[1])));
						setChanged();
						notifyObservers();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

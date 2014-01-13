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
import java.util.TooManyListenersException;

public class SerialOut {

	private InputStream in;
	private BufferedWriter bw;
	private OutputStream out;

	public SerialOut(String portName) throws Exception {
		init(portName);
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

	public void sendString(String s) throws IOException {
		bw.write(s);
		bw.flush();
	}

}

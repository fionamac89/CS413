import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 * 
 */
public class TwoWaySerialComm {
	public TwoWaySerialComm() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				out.write('1');
				out.write(1);
				// BufferedWriter bw = new BufferedWriter(new
				// OutputStreamWriter(
				// out));
				// bw.write("1");
				// FIXME Send Data Out
				// (new Thread(new SerialWriter(out))).start();

				// FIXME Fix the SerialReader
				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private BufferedReader br;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.br = new BufferedReader(new InputStreamReader(in));
		}

		public void serialEvent(SerialPortEvent arg0) {
			String data;

			try {
				int len = 0;
				if ((data = br.readLine()) != null) {
					System.out.println(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public static class SerialWriter implements Runnable {
		BufferedWriter bw;

		public SerialWriter(OutputStream out) {
			this.bw = new BufferedWriter(new OutputStreamWriter(out));
		}

		public void run() {
			try {
				this.bw.write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			// Runtime.getRuntime().exec(
			// "echo psy49chE | sudo -S ln -s /dev/ttyACM0 /dev/ttyS99");
			(new TwoWaySerialComm()).connect("/dev/ttyS99");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;

public class SerialReaderEventListener implements SerialPortEventListener {

	private InputStream in;
	private byte[] buffer = new byte[1024];

	public SerialReaderEventListener(InputStream in) {
		this.in = in;

	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
			int data;
			int len = 0;
			while (true) {
				data = in.read();
				if (data == '\n') {
					break;
				}
				buffer[len++] = (byte) data;
			}
			System.out.print(new String(buffer, 0, len));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}

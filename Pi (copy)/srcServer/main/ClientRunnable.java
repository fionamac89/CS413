package main;

import java.io.IOException;

public class ClientRunnable implements Runnable {

	@Override
	public void run() {
		try {
			main.RunClient.main(null);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
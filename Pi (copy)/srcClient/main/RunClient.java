package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class RunClient {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		Socket kkSocket = new Socket("192.168.1.113", 4444);
		PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));

		Random random = new Random();
		while (true) {
			// int i = random.nextInt(5);
			// System.out.println("Client: " + i);
			// out.println(i);
			// Thread.sleep(1000);

			String fromUser;
			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}

		}
	}
}

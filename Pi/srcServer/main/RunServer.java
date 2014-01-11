package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RunServer {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		ServerSocket serverSocket = new ServerSocket(4444);
		System.out.println("Waiting");
		// new Thread(new ClientRunnable()).start();
		Socket clientSocket = serverSocket.accept();
		System.out.println("Client Found");
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		String inputLine;
		while (true) {
			if ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
		}

	}

}

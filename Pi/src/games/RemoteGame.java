package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import comms.arduino.ArduinoSerialIO;
import comms.model.Data;
import comms.model.Score;

public class RemoteGame extends GameMode {

	public RemoteGame(int noOfPads, ArduinoSerialIO arduinoInput) {
		super(noOfPads, arduinoInput);
	}

	@Override
	public Score playGame() {

		try {
			ServerSocket serverSocket = new ServerSocket(4444);
			System.out.println("Waiting");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client Found");
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String inputLine;
			while (true) {
				if ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);

					arduinoInput.sendCommand(new Data(Integer
							.parseInt(inputLine)));

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return score;
	}

}

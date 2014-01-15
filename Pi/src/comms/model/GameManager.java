package comms.model;

import games.Game;
import games.RandomMode;
import games.RemoteGame;
import games.SimonSaysMode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import comms.arduino.ArduinoSerialIO;

public class GameManager {

	private Game game;
	private ScoreBoard scores;
	private ArduinoSerialIO arduino;
	private int noOfPads = 4;

	public GameManager(String portName) {
		arduino = new ArduinoSerialIO(portName);
		scores = getScores();
	}

	private ScoreBoard getScores() {
		ScoreBoard s;
		try {
			FileInputStream fin = new FileInputStream("./scores.ser");
			ObjectInputStream oos = new ObjectInputStream(fin);
			Object o = oos.readObject();
			s = (ScoreBoard) o;

		} catch (Exception e) {
			s = new ScoreBoard();

		}
		return s;
	}

	public void saveScores() {
		try {
			FileOutputStream fout = new FileOutputStream("./scores.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(scores);
			oos.close();
		} catch (IOException ioe) {
			System.out.println("Error Saving");
		}
		System.out.println("Done");
	}

	public void newGame(String actionCommand) {
		switch (actionCommand) {
		case "Random":
			game = new RandomMode(noOfPads, arduino);
			break;
		case "Simon Says":
			game = new SimonSaysMode(noOfPads, arduino);
			break;
		case "SmartPhone Controlled":
			game = new RemoteGame(noOfPads, arduino);
			break;

		}
	}

	public Game getMode() {
		return game;
	}

	public ScoreBoard getScoreBoard() {
		return scores;
	}

}

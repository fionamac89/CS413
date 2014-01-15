package games;

public class GameFactory {

	public static GameMode newRandomMode() {
		return null; // new RandomMode();
	}

	public static GameMode newSimonSaysMode() {
		return new SimonSaysMode();
	}

	public static GameMode newSmartPhoneMode() {
		return new SmartPhoneMode();
	}

}

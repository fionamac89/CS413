package comms.model;

public class Config {

	private String config;

	public Config() {
		config = "";
	}

	public Config appendChar(char c) {
		config += c;
		return this;
	}

	public String getCommand() {
		return config;
	}
}

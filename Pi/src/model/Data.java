package model;

public class Data implements Command, Response {

	private final int padNo;
	private final int responseTime;

	public Data(int padNo) {
		this.padNo = padNo;
		this.responseTime = 0;
	}

	public Data(int padNo, int responseTime) {
		this.padNo = padNo;
		this.responseTime = responseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see comms.arduino.Command#getPadNo()
	 */
	@Override
	public int getPadNo() {
		return padNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see comms.arduino.Response#getResponseTime()
	 */
	@Override
	public int getResponseTime() {
		return responseTime;
	}

	@Override
	public String toString() {
		return "PadNo: " + padNo + " RT: " + responseTime;

	}
}

package comms.model;

public class Data implements Command, Response {

	private final int padNo;
	private final Long responseTime;

	public Data(int padNo) {
		this.padNo = padNo;
		this.responseTime = null;
	}

	public Data(int padNo, Long responseTime) {
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
	public Long getResponseTime() {
		return responseTime;
	}

	@Override
	public String toString() {
		return "PadNo: " + padNo + " RT: " + responseTime;

	}
}

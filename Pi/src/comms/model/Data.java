package comms.model;

import java.util.Date;

import util.TimeUtil;


public class Data implements Command, Response {

	private static int idCounter;
	private final int id, padNo;
	private final Date sendTime, responseTime;

	public Data(int padNo) {
		this.id = idCounter;
		idCounter++;
		this.padNo = padNo;
		this.sendTime = TimeUtil.getCurrentTime();
		this.responseTime = null;
	}

	public Data(Command c) {
		this.id = c.getId();
		this.padNo = c.getPadNo();
		this.sendTime = c.getSendTime();
		this.responseTime = TimeUtil.getCurrentTime();

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
	 * @see comms.arduino.Command#getSendTime()
	 */
	@Override
	public Date getSendTime() {
		return sendTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see comms.arduino.Command#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	@Override
	public Date getResponseTime() {
		return responseTime;
	}

	@Override
	public int getScore() {
		Long diff = responseTime.getTime() - sendTime.getTime();
		System.out.println("RT:\t" + responseTime.getTime() + " ST:\t"
				+ sendTime.getTime() + " Diff:\t" + diff);
		return diff.intValue();
	}

	@Override
	public String toCommandString() {
		return id + ", " + padNo + ", " + TimeUtil.getStringFromDate(sendTime);
	}

	@Override
	public String toResponseString() {
		return toCommandString() + ", "
				+ TimeUtil.getStringFromDate(responseTime);
	}

	public static Command getCommandFromString(String command) {
		return null;
	}

	public static Response getResponeFromString(String response) {
		return null;
	}
}

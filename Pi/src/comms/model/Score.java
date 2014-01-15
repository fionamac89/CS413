package comms.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Score {

	private AtomicInteger totalPulses;
	private AtomicLong sumOfResponses;
	private String name;

	public Score() {
		totalPulses = new AtomicInteger();
		sumOfResponses = new AtomicLong();
	}

	public void increaseTotalPulse() {
		totalPulses.addAndGet(1);
	}

	public void increaseResponseTimes(long l) {
		sumOfResponses.addAndGet(l);
	}

	public Long getSumOfResponses() {
		return sumOfResponses.get();
	}

	public int getTotalPulses() {
		return totalPulses.get();
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAverageResponseTime() {
		return sumOfResponses.get() / new Long(totalPulses.get());
	}

}

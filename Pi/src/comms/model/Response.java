package comms.model;

import java.util.Date;

public interface Response extends IData {

	public String toResponseString();

	public Date getResponseTime();

	public int getScore();

}

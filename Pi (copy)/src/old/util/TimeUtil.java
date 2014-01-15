package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

	public static Date getCurrentTime() {
		return Calendar.getInstance().getTime();
	}

	public static String getStringFromDate(Date date) {
		return sdf.format(date);
	}

	public static Date getDateFromString(String date) throws ParseException {
		return sdf.parse(date);
	}
}

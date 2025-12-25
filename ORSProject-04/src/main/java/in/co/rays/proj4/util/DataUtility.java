package in.co.rays.proj4.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DataUtility class provides helper methods for converting
 * Strings, Dates, Timestamps and validating input data.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class DataUtility {

	public static final String APP_DATE_FORMAT = "dd-MM-yyyy";
	public static final String APP_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	private static final SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat(APP_TIME_FORMAT);

	/**
	 * Trims string if not null
	 */
	public static String getString(String val) {
		if (DataValidator.isNotNull(val)) {
			return val.trim();
		}
		return val;
	}

	/**
	 * Convert object into string safely
	 */
	public static String getStringData(Object val) {
		return (val != null) ? val.toString() : "";
	}
	public static double getDouble(String val) {

	    if (DataValidator.isNull(val)) {
	        return 0.0;
	    }

	    try {
	        return Double.parseDouble(val);
	    } catch (Exception e) {
	        return 0.0;
	    }
	}


	/**
	 * Convert string into int safely
	 */
	public static int getInt(String val) {
		if (DataValidator.isInteger(val)) {
			return Integer.parseInt(val);
		}
		return 0;
	}

	/**
	 * Convert string into long safely
	 */
	public static long getLong(String val) {
		if (DataValidator.isLong(val)) {
			return Long.parseLong(val);
		}
		return 0;
	}

	/**
	 * Convert string into Date
	 */
	public static Date getDate(String val) {
		try {
			if (DataValidator.isNotNull(val)) {
				return formatter.parse(val);
			}
		} catch (Exception e) {
			System.out.println("Date parsing error: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Convert Date to String
	 */
	public static String getDateString(Date date) {
		try {
			if (date != null) {
				return formatter.format(date);
			}
		} catch (Exception e) {
			System.out.println("Date format error: " + e.getMessage());
		}
		return "";
	}

	/**
	 * Convert String to Timestamp
	 */
	public static Timestamp getTimestamp(String val) {
		try {
			if (DataValidator.isNotNull(val)) {
				return new Timestamp(timeFormatter.parse(val).getTime());
			}
		} catch (Exception e) {
			System.out.println("Timestamp parsing error: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Convert long to Timestamp
	 */
	public static Timestamp getTimestamp(long time) {
		return new Timestamp(time);
	}

	/**
	 * Get current timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Convert Timestamp to long
	 */
	public static long getTimestamp(Timestamp tm) {
		if (tm != null) {
			return tm.getTime();
		}
		return 0;
	}

	/**
	 * Testing Method
	 */
	public static void main(String[] args) {

		System.out.println("---- DataUtility Test ----");

		// getString
		System.out.println("getString: " + getString("   Hello India   "));

		// getInt
		System.out.println("getInt: " + getInt("123"));

		// getLong
		System.out.println("getLong: " + getLong("999999"));

		// Correct format: dd-MM-yyyy
		String dateStr = "15-10-2024";
		Date date = getDate(dateStr);
		System.out.println("Date: " + date);

		// Date to String
		System.out.println("Formatted Date: " + getDateString(date));

		// Correct timestamp format
		String timestampStr = "15-10-2024 10:30:45";
		Timestamp ts = getTimestamp(timestampStr);
		System.out.println("Timestamp: " + ts);

		// Current Timestamp
		System.out.println("Current Timestamp: " + getCurrentTimestamp());
	}
}

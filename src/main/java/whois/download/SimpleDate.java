package whois.download;

import java.util.Calendar;

public class SimpleDate {
	static Calendar cal = Calendar.getInstance();
	
	static final public String SIMPLE_STRING_FMT = "%s-%02d-%02d";
	
	public static int getYear() {
	    return cal.get(Calendar.YEAR);
	}
	
	public static int getMonth() {
		return cal.get(Calendar.MONTH);
	}	
	
	public static int getDay() {
		return cal.get(Calendar.DATE);
	}
	
	public static Integer convertInt(String x) {
		try {
			return Integer.valueOf(x);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public static boolean validString(String date) {
		
		String [] r = splitDate(date);
		if (r.length != 3)
			return false;
		Integer year = convertInt(r[0]);
		Integer month = convertInt(r[1]);
		Integer day = convertInt(r[2]);
		boolean valid_month = month != null && 0 < month && month < 13;
		boolean valid_day = day != null && 0 < day && day < 32;
		boolean valid_year = year != null && 1970 < year && year < 3200;
		return valid_year && valid_month && valid_day;
	}
	
	
	public static String[] splitDate(String date) {
		return date.split("-", 3); 
	}
	
	public static int getDayFromString(String date) {
		if (validString(date)) {
			String[] r = splitDate(date);
			return convertInt(r[2]);
		}
		return -1;
	}
	public static int getMonthFromString(String date) {
		if (validString(date)) {
			String[] r = splitDate(date);
			return convertInt(r[1]);
		}
		return -1;
	}
	public static int getYearFromString(String date) {
		if (validString(date)) {
			String[] r = splitDate(date);
			return convertInt(r[0]);
		}
		return -1;
	}
	public static String getString() {
		return String.format(SIMPLE_STRING_FMT, getYear(), getMonth(), getDay());
	}
}

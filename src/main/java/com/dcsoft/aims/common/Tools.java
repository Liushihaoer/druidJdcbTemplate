package com.dcsoft.aims.common;

import java.util.Date;

public class Tools {

	public static String dateformat(Date date, String format) {
		if (format == null) {
			format = "yyyy-MM-dd hh:mm:ss";
		}
		java.text.DateFormat format1 = new java.text.SimpleDateFormat(format);
		return format1.format(date);
	}

}

package com.surgeryassist.util;

import java.util.Calendar;
import java.util.Date;

public class SurgeryAssistUtil {

	public static Calendar DateToCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

}

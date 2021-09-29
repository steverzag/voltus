package com.softit.voltus.app.classes;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {

	public Date getDateFromToDay(int calendarField, int value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(calendarField, gc.get(calendarField) + value);

		return gc.getTime();
	}

	public static int getDaysTo(Date date) {

		int days = 0;
		GregorianCalendar gcDate = new GregorianCalendar();
		GregorianCalendar toDay = new GregorianCalendar();
		gcDate.setTime(date);

		int compare = toDay.compareTo(gcDate);
		if (compare > 0) {
			while (!isToDay(gcDate.getTime())) {
				gcDate.set(Calendar.DAY_OF_YEAR, gcDate.get(Calendar.DAY_OF_YEAR) + 1);
				days++;
			}
		} else {
			while (!isToDay(gcDate.getTime())) {
				gcDate.set(Calendar.DAY_OF_YEAR, gcDate.get(Calendar.DAY_OF_YEAR) - 1);
				days--;
			}
		}
		return days;
	}

	public static boolean isToDay(Date time) {

		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar gcTime = new GregorianCalendar();
		gcTime.setTime(time);

		if (gc.get(Calendar.DAY_OF_YEAR) == gcTime.get(Calendar.DAY_OF_YEAR)
				&& gc.get(Calendar.YEAR) == gcTime.get(Calendar.YEAR))
			return true;
		return false;
	}

	public static Date addTime(Date date, int calendarField, int value) {

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(calendarField, gc.get(calendarField) + value);
		return gc.getTime();
	}

	public static Date restTime(Date date, int calendarField, int value) {

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(calendarField, gc.get(calendarField) - value);
		return gc.getTime();
	}

	public static String getMonthYearString(Date d) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		String s = df.format(d).replaceFirst(" de ", "");

		while (Character.isDigit(s.charAt(0))) {
			s = s.substring(1);
		}
		return s;
	}

	public static int getMonth(Date fecha) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);

		return gc.get(Calendar.MONTH);
	}

	public static int getDaysOfMonth(int calendarMonth) {
		GregorianCalendar gc = new GregorianCalendar();
		int calendarYear = gc.get(Calendar.YEAR);
		if (calendarMonth == 1) 
			return (calendarYear % 4 == 0) ? 29 : 28;
		else if(calendarMonth == 3 || calendarMonth == 5 || calendarMonth == 8 || 
				calendarMonth == 10)
			return 30;
		return 31;
	}

	public static int getDaysOfMonth(int calendarMonth, int calendarYear) {
		if( calendarMonth > 11 || calendarMonth < 0)
			return -1;
		if (calendarMonth == 1) 
			return (calendarYear % 4 == 0) ? 29 : 28;
		else if(calendarMonth == 3 || calendarMonth == 5 || calendarMonth == 8 || 
				calendarMonth == 10)
			return 30;
		return 31;
	}
	
	public static int getMonth(String month) {
		if(month.equals("enero"))
			return 0;
		else if(month.equals("febrero"))
			return 1;
		else if(month.equals("marzo"))
			return 2;
		else if(month.equals("abril"))
			return 3;
		else if(month.equals("mayo"))
			return 4;
		else if(month.equals("junio"))
			return 5;
		else if(month.equals("julio"))
			return 6;
		else if(month.equals("agosto"))
			return 7;
		else if(month.equals("septiembre"))
			return 8;
		else if(month.equals("octubre"))
			return 9;
		else if(month.equals("noviembre"))
			return 10;
		else if(month.equals("diciembre"))
			return 11;
		else
			return -1;
	}

	public static int getYear(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		return gc.get(Calendar.YEAR);
	}
}

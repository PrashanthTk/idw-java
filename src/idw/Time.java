package idw;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Time implements Comparable<Time> {
	static class TimeComparator implements Comparator<Time> {
		@Override
		public int compare(Time t1, Time t2) {
			if (t1.getYear() != t2.getYear())
				return t1.getYear() - t2.getYear();
			else if (t1.getMonth() != t2.getMonth())
				return t1.getMonth() - t2.getMonth();
			else if (t1.getDay() != t2.getDay())
				return t1.getDay() - t2.getDay();
			else
				return 0;
		}
	}

	public enum TimeFormat {
		DAY, MONTH, QUARTER, YEAR, JULIAN
	}

	public static Time createTimeDay(int y, int mq, int d, TimeFormat f) {
		return new Time(y, mq, d, f);
	}

	public static Time createTimeMonthOrQuarter(int y, int mq, TimeFormat f) {
		return new Time(y, mq, 1, f);
	}

	public static Time createTimeYear(int y, TimeFormat f) {
		return new Time(y, 1, 1, f);
	}

	public static Time createTimeJulian(int j, TimeFormat f) {
		return new Time(j, f);
	}

	private GregorianCalendar calendar = new GregorianCalendar();
	private int day;
	private TimeFormat format;
	private int month;
	private int quarter;
	private float timeCoordinate;
	private int year;

	private Time(int year, int monthOrQuarter, int day, TimeFormat format) {

		this.format = format;
		this.year = year;
		this.month = monthOrQuarter;
		this.quarter = monthOrQuarter;
		this.day = day;

		this.setTimeCoordinate();

	}

	private Time(int julian, TimeFormat format) {
		this.format = format;
		this.calendar.set(Calendar.YEAR, 2009);
		this.calendar.set(Calendar.DAY_OF_YEAR, julian);
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH);
		this.day = calendar.get(Calendar.DAY_OF_MONTH);

		this.setTimeCoordinate();
	}

	@Override
	public int compareTo(Time arg0) {
		if (arg0.getYear() != this.getYear())
			return this.getYear() - arg0.getYear();
		else if (arg0.getMonth() != this.getMonth())
			return this.getMonth() - arg0.getMonth();
		else if (arg0.getDay() != this.getDay())
			return this.getDay() - arg0.getDay();
		else
			return 0;
	}

	public GregorianCalendar getCalendar() {
		return calendar;
	}

	public int getDay() {
		return day;
	}

	public TimeFormat getFormat() {
		return format;
	}

	public int getMonth() {
		return month;
	}

	public int getQuarter() {
		return quarter;
	}

	public float getTimeCoordinate() {
		return timeCoordinate;
	}

	public int getYear() {
		return year;
	}

	private void setTimeCoordinate() {
		switch (format) {

		case YEAR:
			timeCoordinate = year;
			break;
		case QUARTER:
			timeCoordinate = (getYear() * 4) + getQuarter();
			break;
		case MONTH:
			timeCoordinate = (getYear() * 12) + getMonth();
			break;
		case DAY:
			calendar.set(Calendar.YEAR, getYear());
			calendar.set(Calendar.MONTH, getMonth());
			calendar.set(Calendar.DAY_OF_MONTH, getDay());
			timeCoordinate = calendar.get(Calendar.DAY_OF_YEAR);
			break;
		case JULIAN:
			timeCoordinate = calendar.get(Calendar.DAY_OF_YEAR);
			break;
		}
	}

	@Override
	public String toString() {

		String myTime = new String();

		switch (this.getFormat()) {

		case YEAR:
			return Integer.toString(getYear());

		case QUARTER:
			return Integer.toString(getYear()) + "\t"
					+ Integer.toString(getQuarter());

		case MONTH:
			return Integer.toString(getYear()) + "\t"
					+ Integer.toString(getMonth() + 1);

		case DAY:
			return Integer.toString(getYear()) + "\t"
					+ Integer.toString(getMonth()) + "\t"
					+ Integer.toString(getDay());

		case JULIAN:
			return Integer.toString(getYear()) + "\t"
					+ Integer.toString(getMonth() + 1) + "\t"
					+ Integer.toString(getDay());
		}
		return myTime;
	}

}

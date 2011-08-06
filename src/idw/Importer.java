package idw;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import idw.Time.TimeFormat;




public class Importer {

	public static Importer createImportMeasurements(File filename,
			TimeFormat format) throws IOException {
		return new Importer(filename, format, Delimiter.tab);
	}

	public static Importer createImportMeasurements(File filename,
			TimeFormat format, Delimiter d) throws IOException {
		return new Importer(filename, format, d);
	}

	public enum Delimiter {
		comma, tab
	}

	private HashSet<Integer> years = new HashSet<Integer>();
	private Measurement[] measurements;
	private int numberOfLines;
	private GregorianCalendar calendar = new GregorianCalendar();
	private TimeFormat format = null;
	private String delimiter;

	private Importer(File filename, TimeFormat format, Delimiter d)
			throws IOException {

		String id, s1;
		int year, month, quarter, day;
		String[] s;
		Time time;
		double x, y;
		float m;
		this.format = format;
		switch (d) {
		case comma:
			delimiter = ",";
			break;
		case tab:
			delimiter = "\t";
			break;
		}
		BufferedReader r = new BufferedReader(new FileReader(filename));
		numberOfLines = Utils.count(filename.toString()) - 1;
		measurements = new Measurement[numberOfLines];
		/*
		 * r.readLine() and the following switch all assume that the input file
		 * will be tab-delimited with a header row and that the input file will
		 * be of the form specified by the case.
		 */
		r.readLine();

		switch (format) {
		case YEAR:
			s1 = r.readLine();
			for (int i = 0; i < numberOfLines && s1 != null; i++) {
				// delimiter
				s = s1.split(delimiter);
				year = Integer.parseInt(s[1]);
				years.add(year);
				time = Time.createTimeYear(year, TimeFormat.YEAR);
				// parse the station ID, x,y coordinates, and measurement
				id = s[0];
				x = Double.parseDouble(s[2]);
				y = Double.parseDouble(s[3]);
				m = Float.parseFloat(s[4]);
				measurements[i] = new Measurement(id, x, y, m, time);
				// next!
				s1 = r.readLine();
			}

		case QUARTER:
			s1 = r.readLine();
			for (int i = 0; i < numberOfLines && s1 != null; i++) {
				// delimiter
				s = s1.split(delimiter);
				year = Integer.parseInt(s[1]);
				years.add(year);
				quarter = Integer.parseInt(s[2]);
				// parse the station ID, x,y coordinates, and measurement
				id = s[0];
				x = Double.parseDouble(s[3]);
				y = Double.parseDouble(s[4]);
				m = Float.parseFloat(s[5]);
				time = Time.createTimeMonthOrQuarter(year, quarter, TimeFormat.QUARTER);
				measurements[i] = new Measurement(id, x, y, m, time);
				// next!
				s1 = r.readLine();
			}

		case MONTH:
			s1 = r.readLine();
			for (int i = 0; i < numberOfLines && s1 != null; i++) {
				// delimiter
				s = s1.split(delimiter);
				year = Integer.parseInt(s[1]);
				years.add(year);
				month = Integer.parseInt(s[2]);
				// parse the station ID, x,y coordinates, and measurement
				id = s[0];
				x = Double.parseDouble(s[3]);
				y = Double.parseDouble(s[4]);
				m = Float.parseFloat(s[5]);
				time = Time.createTimeMonthOrQuarter(year, month, TimeFormat.MONTH);
				measurements[i] = new Measurement(id, x, y, m, time);
				// next!
				s1 = r.readLine();
			}

		case DAY:
			s1 = r.readLine();
			for (int i = 0; i < numberOfLines && s1 != null; i++) {
				// delimiter
				s = s1.split(delimiter);
				year = Integer.parseInt(s[1]);
				years.add(year);
				month = Integer.parseInt(s[2]);
				day = Integer.parseInt(s[3]);
				// parse the station ID, x,y coordinates, and measurement
				id = s[0];
				x = Double.parseDouble(s[4]);
				y = Double.parseDouble(s[5]);
				m = Float.parseFloat(s[6]);
				time = Time.createTimeDay(year, month, day, TimeFormat.DAY);
				measurements[i] = new Measurement(id, x, y, m, time);
				// next!
				s1 = r.readLine();
			}
		case JULIAN:
			s1 = r.readLine();
			for (int i = 0; i < numberOfLines && s1 != null; i++) {
				// delimiter
				s = s1.split(delimiter);
				years.add(2009);
				int j = Integer.parseInt(s[2]);
				// parse the station ID, x,y coordinates, and measurement
				id = "00000000";
				x = Double.parseDouble(s[0]);
				y = Double.parseDouble(s[1]);
				m = Float.parseFloat(s[3]);
				time = Time.createTimeJulian(j, TimeFormat.JULIAN);
				measurements[i] = new Measurement(id, x, y, m, time);
				// next!
				s1 = r.readLine();
			}
		}
		System.out.println("Import file successfully loaded");
	}

	public GregorianCalendar getCalendar() {
		return calendar;
	}

	public Measurement[] getMeasurements() {
		return measurements;
	}

	public TimeFormat getTimeFormat() {
		return format;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

}

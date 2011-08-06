package idw;

public class Measurement extends Location {

	private float measurement;
	private float interpolation;
	private Time time;
	private float timeCoordinate;

	public Measurement(String id, double x, double y, float m, Time t) {
		super(id, x, y);
		this.measurement = m;
		this.setTime(t);
		this.setTimeCoordinate(t.getTimeCoordinate());
	}

	public int compareTo(Measurement measurement2) {
		if (this.getID() == measurement2.getID()
				&& this.getTime().getTimeCoordinate() == measurement2.getTime()
						.getTimeCoordinate())
			return 1;
		return 0;
	}

	public double getDistance(Measurement anotherMeasurement) {

		double distance;
		double measurementX = anotherMeasurement.getX();
		double measurementY = anotherMeasurement.getY();
		double measurementTime = anotherMeasurement.getTime()
				.getTimeCoordinate();

		double xdiff = measurementX - getX();
		double ydiff = measurementY - getY();
		double zdiff = measurementTime - getTimeCoordinate();

		distance = Math.sqrt((xdiff * xdiff) + (ydiff * ydiff)
				+ (zdiff * zdiff));
		return distance;
	}

	public float getMeasurement() {
		return Float.valueOf(measurement);
	}

	public Time getTime() {
		return time;
	}

	public float getTimeCoordinate() {
		return timeCoordinate;
	}

	public void setMeasurement(float measurement) {
		this.measurement = measurement;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setTimeCoordinate(float f) {
		this.timeCoordinate = f;
	}

	@Override
	public String toString() {
		return getID() + "\t" + getX() + "\t" + getY() + "\t" + time + "\t"
				+ getInterpolation();
	}

	public void setInterpolation(float interpolation) {
		this.interpolation = interpolation;
	}

	public float getInterpolation() {
		return interpolation;
	}

}

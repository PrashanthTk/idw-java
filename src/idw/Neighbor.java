package idw;

import java.util.Comparator;

public class Neighbor implements Comparable<Neighbor> {
	static class NeighborComparator implements Comparator<Neighbor> {
		@Override
		public int compare(Neighbor n1, Neighbor n2) {
			return (int) Math.signum(n1.getDistance() - n2.getDistance());
		}
	}

	public static Neighbor createNeighbor(float measurement, Double distance) {
		return new Neighbor(measurement, distance);
	}

	private Double distance;

	private float measurement;

	private Neighbor(float measurement, Double distance) {
		super();
		this.measurement = measurement;
		this.distance = distance;
	}

	@Override
	public int compareTo(Neighbor o) {
		return (int) Math.signum(this.getDistance() - o.getDistance());
	}

	public Double getDistance() {
		return distance;
	}

	public float getMeasurement() {
		return measurement;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public void setMeasurement(float measurement) {
		this.measurement = measurement;
	}

	@Override
	public String toString() {
		return "Neighbor [distance=" + distance + ", measurement="
				+ measurement + "]";
	}

}

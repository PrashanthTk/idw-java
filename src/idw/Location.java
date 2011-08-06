package idw;

public class Location extends CoordinatePair {
	private String ID;

	public Location(String i, double x, double y) {
		this.ID = i;
		this.x = x;
		this.y = y;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return "Location [ID=" + ID + ", xCoordinate=" + x + ", yCoordinate="
				+ y + "]";
	}
}

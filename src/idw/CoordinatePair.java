package idw;

public abstract class CoordinatePair {
	String ID;
	double x;
	double y;

	public double getX() {
		return x;
	}

	public void setX(double xCoordinate) {
		this.x = xCoordinate;
	}

	public double getY() {
		return y;
	}

	public void setY(double yCoordinate) {
		this.y = yCoordinate;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}

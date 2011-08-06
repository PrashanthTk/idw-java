package idw;

class Interpolator implements Runnable {
	/**
	 * 
	 */
	private final LeaveOneOut leaveOneOut;
	private InterpolatorQueue interpolatorQueue;
	private int index;
	private NearestNeighbors neighbors;
	Interpolator(LeaveOneOut leaveOneOut, InterpolatorQueue iq) {
		this.leaveOneOut = leaveOneOut;
		interpolatorQueue = iq;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				neighbors = interpolatorQueue.take();
				index = neighbors.getIndex();
				float measurement = 0.0f;
				for (int i = 0; i < this.leaveOneOut.numNeighbors; i++) {
					Neighbor thisNeighbor = neighbors.get(i);
					measurement += thisNeighbor.getMeasurement()
							* weight(thisNeighbor, this.leaveOneOut.getExponent());
				}
				this.leaveOneOut.validations[index].setInterpolation(measurement);
				this.leaveOneOut.increment();
				neighbors = null;
			}
		}

		catch (InterruptedException e) {
			System.out.println("Interpolator interrupted, returning");
		}
	}

	private float weight(Neighbor neighbor, float exponent) {
		return weightNumerator(neighbor, exponent)
				/ weightDenominator(exponent);
	}

	private float weightDenominator(float exponent) {
		float sum = 0.0f;
		for (int i = 0; i < this.leaveOneOut.numNeighbors; i++) {
			sum += Math.pow(1 / neighbors.get(i).getDistance(), exponent);
		}
		return sum;
	}

	private float weightNumerator(Neighbor neighbor, float exponent) {
		float num = (float) Math
				.pow((1 / neighbor.getDistance()), exponent);
		return num;
	}

}
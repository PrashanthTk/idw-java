package idw;

class Searcher implements Runnable {
	/**
	 * 
	 */
	private final LeaveOneOut leaveOneOut;
	private SearcherQueue searcherQueue;
	private InterpolatorQueue interpolatorQueue;
	private NearestNeighbors neighbors;

	public Searcher(LeaveOneOut leaveOneOut, SearcherQueue sq, InterpolatorQueue iq) {
		this.leaveOneOut = leaveOneOut;
		searcherQueue = sq;
		interpolatorQueue = iq;
	}

	@Override
	public void run() {
		int index;
		try {
			while (!Thread.interrupted()) {
				index = searcherQueue.take();
				neighbors = new NearestNeighbors(this.leaveOneOut.numNeighbors);
				neighbors.setIndex(index);
				for (int j = 0; j < this.leaveOneOut.numberOfMeasurements; j++) {
					double distance = this.leaveOneOut.validations[index]
							.getDistance(this.leaveOneOut.validations[j]);
					if (distance < 0.001) {
						continue;
					} else if (!neighbors.isFull()) {
						neighbors.add(Neighbor.createNeighbor(
								this.leaveOneOut.validations[j].getMeasurement(), distance));
					} else if (distance < neighbors.maxDistance()) {
						neighbors.replaceMax(Neighbor.createNeighbor(
								this.leaveOneOut.validations[j].getMeasurement(), distance));
					}
				}
				interpolatorQueue.put(neighbors);
			}
		} catch (InterruptedException e) {
			System.out.println("Searcher interrupted, returning.");
		}
	}

}
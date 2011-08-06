package idw;

import java.util.ArrayList;
import java.util.Collections;

public class NearestNeighbors {
	private int index;
	private Neighbor maxNeighbor;
	private ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();
	private int numberOfNeighbors;

	public NearestNeighbors(int n) {
		numberOfNeighbors = n;
	}

	public boolean add(Neighbor n) {
		neighbors.add(n);
		Collections.sort(neighbors);
		maxNeighbor = neighbors.get(neighbors.size() - 1);
		return true;
	}

	public void clear() {
		neighbors.clear();
	}

	public Neighbor get(int index) {
		return neighbors.get(index);
	}

	public int getIndex() {
		return index;
	}

	public boolean isFull() {
		if (neighbors.size() == numberOfNeighbors)
			return true;
		return false;
	}

	public double maxDistance() {
		return maxNeighbor.getDistance();
	}

	public void replaceMax(Neighbor n) {
		neighbors.add(n);
		neighbors.remove(maxNeighbor);
		Collections.sort(neighbors);
		maxNeighbor = neighbors.get(neighbors.size() - 1);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int size() {
		return neighbors.size();
	}
}

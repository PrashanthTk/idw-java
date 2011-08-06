package idw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaveOneOut implements Runnable {

	class MAPE {
		private double error;
		private int ignored;

		MAPE(double d, int i) {
			error = d;
			ignored = i;
		}

		public MAPE value() {
			return this;
		}
	}

	public static LeaveOneOut createValidator(Importer measurementsImport,
			File output, int numNeighbors, int numberOfInterpolations,
			float exponent) {
		return new LeaveOneOut(measurementsImport, output, numNeighbors,
				numberOfInterpolations, exponent);
	}

	private int numberOfInterpolationsPerformed = 0;
	private float exponent;
	Measurement[] validations;
	private Importer measurementsImport;
	int numNeighbors;
	int numberOfMeasurements;
	private File outputFile;
	private Time.TimeFormat timeFormat;

	private LeaveOneOut(Importer measurementsImport, File output,
			int numNeighbors, int numberOfInterpolations, float exponent) {
		this.outputFile = output;
		this.validations = measurementsImport.getMeasurements();
		this.measurementsImport = measurementsImport;
		this.numberOfMeasurements = measurementsImport.getNumberOfLines();
		this.numNeighbors = numNeighbors;
		this.exponent = exponent;
		this.timeFormat = measurementsImport.getTimeFormat();
	}

	public float getExponent() {
		return exponent;
	}

	public Importer getMeasurementsImport() {
		return measurementsImport;
	}

	public int getNumNeighbors() {
		return numNeighbors;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public Time.TimeFormat getTimeFormat() {
		return timeFormat;
	}

	public Measurement[] getValidations() {
		return validations;
	}

	public synchronized void increment() {
		numberOfInterpolationsPerformed++;
	}

	private double meanAbsoluteError() {
		double error = 0;
		for (int i = 0; i < numberOfMeasurements; i++) {
			error += Math.abs(validations[i].getInterpolation()
					- validations[i].getMeasurement());
		}
		error /= numberOfMeasurements;
		return error;
	}

	public MAPE meanAbsolutePercentageError() {
		double error = 0;
		int ignored = 0;
		for (int i = 0; i < numberOfMeasurements; i++) {
			if (validations[i].getMeasurement() != 0)
				error += (Math.abs(validations[i].getInterpolation()
						- validations[i].getMeasurement()) / validations[i]
						.getMeasurement());
			else
				ignored++;
		}
		error /= numberOfMeasurements;
		MAPE mape = new MAPE(error, ignored);
		return mape;
	}

	private double meanBiasedError() {
		double error = 0;
		for (int i = 0; i < numberOfMeasurements; i++) {
			error += (validations[i].getInterpolation() - validations[i]
					.getMeasurement());
		}
		error /= numberOfMeasurements;
		return error;
	}

	private double rootMeanSquaredError() {
		double error = 0;
		for (int i = 0; i < numberOfMeasurements; i++) {
			error += Math.pow(
					(validations[i].getInterpolation() - validations[i]
							.getMeasurement()), 2);
		}
		error /= numberOfMeasurements;
		error = Math.sqrt(error);
		return error;
	}

	@Override
	public void run() {
		ExecutorService exec = Executors.newCachedThreadPool();
		try {
			SearcherQueue searchQueue = new SearcherQueue();
			InterpolatorQueue interpolateQueue = new InterpolatorQueue();
			for (int i = 0; i < numberOfMeasurements; i++) {
				searchQueue.put(i);
			}
			for (int i = 0; i < Runtime.getRuntime().availableProcessors() - 1; i++)
				exec.execute(new Searcher(this, searchQueue, interpolateQueue));
			exec.execute(new Interpolator(this, interpolateQueue));
			exec.execute(new Interpolator(this, interpolateQueue));
			while (numberOfInterpolationsPerformed < numberOfMeasurements)
				Thread.yield();
			exec.shutdownNow();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void writeInterpolationsToFile() {

		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new FileWriter(getOutputFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < numberOfMeasurements; i++) {
			try {
				w.write(validations[i].toString());
				w.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			MAPE m = meanAbsolutePercentageError();
			w.write("MAE= " + Double.toString(meanAbsoluteError()));
			w.newLine();
			w.write("MBE= " + Double.toString(meanBiasedError()));
			w.newLine();
			w.write("RMSE= " + Double.toString(rootMeanSquaredError()));
			w.newLine();
			w.write("MAPE= " + m.error + " with " + m.ignored
					+ " ignored 0.0 measurements");
			w.newLine();
			w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

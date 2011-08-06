package idw;

import java.io.File;
import java.io.IOException;
import idw.Time.TimeFormat;

public class LooTester {

	TimeFormat format;
	File importFile, output;
	static int[] numNeighbors = {3, 4, 5, 6, 7};
	static float[] exponents = {1.0f, 1.5f, 2.0f, 2.5f,
	                            3.0f, 3.5f, 4.0f, 4.5f, 5.0f};

	public static void main(final String[] args) throws IOException,
			InterruptedException {

		TimeFormat format = TimeFormat.DAY;
		File importFile = new File("pm25_2009_measured.txt");
		Importer measurementsImport = Importer
				.createImportMeasurements(importFile, format);
		
		for (int neighbor : numNeighbors) {
			for (float exponent : exponents) {
				File outputFile = new File("n" + neighbor + "e"
						+ exponent + ".txt");
				double start = System.currentTimeMillis();
				LeaveOneOut loo = LeaveOneOut.createValidator(
						measurementsImport, outputFile, neighbor,
						measurementsImport.getNumberOfLines(), exponent);
				Thread t = new Thread(loo);
				t.start();
				t.join();
				double end = System.currentTimeMillis();
				double duration = (end - start) / 1000;
				System.out.println("Interpolation took " + duration
						+ " seconds.");
				loo.writeInterpolationsToFile();
			}
		}
	}
}

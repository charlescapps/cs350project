package webcrawler;

import java.io.*;
import datastructures.*;

public class TestAlgorithmComplexity {

	private static String usage = "Usage: java TestAlgorithmComplexity -d <dir_with_datafiles> -o <file_to_output>";
	private static File datafileDir; 
	private static File outputFile; 
	private static BufferedWriter outputFileWriter;
	
	public static void main(String[] args) throws Exception {
		if (args.length!=4) {
			System.err.println(usage);
			throw new Exception("Exactly 4 options required.");
		}
		
		if (!args[0].equals("-d")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (!args[2].equals("-o")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		datafileDir = new File(args[1]);
		if (!datafileDir.exists() || !datafileDir.isDirectory()) {
			throw new Exception("Data file directory does not exist!"); 
		}
		
		outputFile = new File(args[3]); 
		if (outputFile.exists()) {
			throw new Exception("A file with the same name as output file already exists!"); 
		}
		
		outputFile.createNewFile();
		outputFileWriter = new BufferedWriter(new FileWriter(outputFile));
		
		testAllFiles();
	}
	
	private static void testAllFiles() {
		
		File[] allDataFiles = datafileDir.listFiles();
		BufferedReader fileReader = null;
		Graph currentGraph = null;
		SCCAlgorithm algo = null; 
		long startTime = 0; 
		long elapsedTime = 0;
		
		for (File f:allDataFiles) {
			try {
				fileReader = new BufferedReader(new FileReader(f));
				currentGraph = new Graph(fileReader);
			}
			catch (Exception e) {
				System.err.println("Data file " + f.toString() + " failed to be input:");
				System.err.println(e.getMessage());
			}
			
			algo = new SCCAlgorithm(currentGraph); 
			
			startTime = System.nanoTime();
			
			elapsedTime = System.nanoTime() - startTime; 
		}
	}
}

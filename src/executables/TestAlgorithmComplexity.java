package executables;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

import datastructures.*;
import algorithms.*;

public class TestAlgorithmComplexity {

	private static String usage = "Usage: java TestAlgorithmComplexity -d <dir_with_datafiles> -o <file_to_output> [-b] (for basic mode)";
	private static File datafileDir; 
	private static File outputFile; 
	private static BufferedWriter outputFileWriter;
	private static SCCAlgorithmInterface algo; 
	private static boolean isBasic; 
	
	public static void main(String[] args) throws Exception {
		if (args.length!=4 && args.length!=5) {
			System.err.println(usage);
			throw new Exception("Exactly 4 or 5 options are possible.");
		}
		
		if (!args[0].equals("-d")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (!args[2].equals("-o")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (args.length==5 && !args[4].equals("-b")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (args.length == 5 )
			isBasic = true; 
		else
			isBasic = false; 
		
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
		
		outputFileWriter.close();
	}
	
	private static void testAllFiles() throws IOException {
		
		outputFileWriter.write("DATA_FILE_NAME, NUM_EDGES, NUM_VERTICES, TIME(ns)");
		outputFileWriter.newLine();
		
		File[] allDataFiles = datafileDir.listFiles();
		BufferedReader fileReader = null;
		Graph currentGraph = null;
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
			
			if (isBasic)
				algo = new SCCAlgorithmBasic(currentGraph); 
			else
				algo = new SCCAlgorithm(currentGraph); 
			
			startTime = System.nanoTime(); //Record start time
			
			try { 
				algo.executeAlgorithm();  //Execute algorithm, clearly
			}
			catch (Exception e) {
				System.err.println("Something went horribly wrong when executing SCC algorithm:"); 
				e.printStackTrace();
			}
			
			elapsedTime = System.nanoTime() - startTime; //Record elapsed time
			
			try {  //Write to file, skip and continue on to next graph if somehow this fails
				outputFileWriter.write(f.getName()+","+ currentGraph.getNumEdges() + "," + currentGraph.getNumVertices() + "," + elapsedTime);
				outputFileWriter.newLine();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}

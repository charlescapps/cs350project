package executables;

import java.io.*;
import datastructures.*;
import algorithms.*;

public class TestAlgorithmComplexity {

	private static String usage = "Usage: java TestAlgorithmComplexity -d <dir_with_datafiles> -o <file_to_output> -n <num_tests_per_graph> [-b] (for basic mode)";
	private static File datafileDir; 
	private static File outputFile; 
	private static File verifyFile;
	private static BufferedWriter outputFileWriter;
	private static BufferedWriter verifyFileWriter;
	private static SCCAlgorithmInterface algo; 
	private static boolean isBasic; 
	
	private static int numRunsPerGraph;
	
	private static final int numVerticesToVerify = 100; 
	
	public static void main(String[] args) throws Exception {
		if (args.length!=6 && args.length!=7) {
			System.err.println(usage);
			throw new Exception("Exactly 6 or 7 options are possible.");
		}
		
		if (!args[0].equals("-d")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (!args[2].equals("-o")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (!args[4].equals("-n")){
		    System.err.println(usage); 
		    throw new Exception("Invalid Option.");
		}
		
		if (args.length==7 && !args[6].equals("-b")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		if (args.length == 7 )
			isBasic = true; 
		else
			isBasic = false; 
		
		numRunsPerGraph = Integer.parseInt(args[5]);
		
		datafileDir = new File(args[1]);
		if (!datafileDir.exists() || !datafileDir.isDirectory()) {
			throw new Exception("Data file directory does not exist!"); 
		}
		
		outputFile = new File(args[3]); 
		if (outputFile.exists()) {
			throw new Exception("A file with the same name as output file already exists!"); 
		}
		
		verifyFile = new File(args[3] + "_verified");
		if (verifyFile.exists()) {
			throw new Exception("A file with the same name as verification file already exists!"); 
		}
		
		outputFile.createNewFile();
		verifyFile.createNewFile();
		outputFileWriter = new BufferedWriter(new FileWriter(outputFile));
		verifyFileWriter = new BufferedWriter(new FileWriter(verifyFile));
		
		testAllFiles();
		
		outputFileWriter.close();
		verifyFileWriter.close();
	}
	
	private static void testAllFiles() throws Exception {
	    
	    verifyFileWriter.write(isBasic ? "BASIC ALGORITHM VERIFICATION INFO" : "JAVA API ALGORITHM VERIFICATION INFO");
        verifyFileWriter.newLine();
		
		outputFileWriter.write("DATA_FILE_NAME, NUM_EDGES, NUM_VERTICES, AVG_TIME(ns), STD_DEV(ns)");
		outputFileWriter.newLine();
		
		File[] allDataFiles = datafileDir.listFiles();
		BufferedReader fileReader = null;
		Graph currentGraph = null;
		long startTime = 0; 
		long[] elapsedTimes = new long[numRunsPerGraph];
		
		for (File f:allDataFiles) {
			try {
				fileReader = new BufferedReader(new FileReader(f));
				currentGraph = new Graph(fileReader);
			}
			catch (Exception e) {
				System.err.println("Data file " + f.toString() + " failed to be input:");
				System.err.println(e.getMessage());
			}
			
			for (int i = 0; i < numRunsPerGraph; i++) { //Run algorithm 'numRunsPerGraph' times on the same graph
			    
			    currentGraph.reInitGraph(); //Resets all nodes to UNVISITED
	
			    if (isBasic)
	                algo = new SCCAlgorithmBasic(currentGraph); //Resets Algorithm to start state
	            else
	                algo = new SCCAlgorithm(currentGraph); 
			
    			startTime = System.nanoTime(); //Record start time
    			
    			algo.executeAlgorithm();       //Execute algorithm, clearly
    			
    			elapsedTimes[i] = System.nanoTime() - startTime; //Record elapsed time
    			
			}
			
			try {  //Write to file, skip and continue on to next graph if somehow this fails
				outputFileWriter.write(f.getName()+","+ currentGraph.getNumEdges() + "," + currentGraph.getNumVertices() + "," + getAverage(elapsedTimes) + "," + getStdDev(elapsedTimes));
				outputFileWriter.newLine();
				
				
				verifyFileWriter.write("**********Verification Info for File '" + f.getName() + "'**********");
				if (currentGraph.getNumVertices() < numVerticesToVerify) {
					
					verifyFileWriter.newLine(); 
					currentGraph.printAdjacencyMatrix(verifyFileWriter);
				}
				
					verifyFileWriter.newLine();
					verifyFileWriter.write(algo.componentsToString());
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private static double getAverage(long[] times) {
	    double avg = 0; 
	    
	    for (int i = 0; i < times.length; i++) {
	        avg += times[i];
	    }
	    return avg / times.length;
	    
	}
	
	private static double getStdDev(long[] times) {
	    double stdDev = 0; 
	    double avg = getAverage(times); 
	    
	    for (int i = 0; i < times.length; i++) {
	        stdDev += (times[i] - avg)*(times[i] - avg); //Sum up the square difference from the average
	    }
	    
	    return Math.sqrt(stdDev / times.length); //Return square-root of average-difference-squared
	    
	}

}

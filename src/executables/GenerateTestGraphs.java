package executables;

import java.io.*;
import java.util.regex.*;
import java.net.URL;
import webcrawler.*;

public class GenerateTestGraphs {
	
	private static BufferedReader br; 
	private static File urlList; 
	private static String usage = "Usage: java GenerateTestGraphs -f <url_list_file_name>";
	private static Pattern urlLine = Pattern.compile("([^,]+),(\\d+)");
	private static File subDir = new File("datafiles");
	
	public static void main (String[] args) throws Exception {
		if (args.length!=2) {
			System.err.println(usage);
			throw new Exception("Exactly 2 options required.");
		}
		
		if (!args[0].equals("-f")) {
			System.err.println(usage);
			throw new Exception("Invalid option.");
		}
		
		String fileName = args[1];
		
		urlList = new File(fileName);
		br = new BufferedReader(new FileReader(urlList));
		
		subDir.mkdir();
		
		getAllTestData();
	}
	
	private static void getAllTestData() throws IOException, Exception {
		String line;
		Matcher m;
		int numNodes; 
		String nodesString; 
		String urlString;
		UrlGraph graph;
		URL theURL;
		String newFileName;
		File newFile; 
		
		while ( (line = br.readLine()) !=null ) {
			numNodes = 0; 
			urlString = "";
			
			m = urlLine.matcher(line);
			if (m.find()==false) {
				System.err.println("\n********Invalid line encountered when inputting list of URLs**********\n");
			}
			else {
				urlString = m.group(1);
				numNodes = Integer.parseInt(m.group(2));
			}
			
			if (numNodes > 0 && !urlString.equals("")) {
				graph = new UrlGraph();
				theURL = new URL(urlString);
				
				System.out.println("\n************CRAWLING WEB STARTING AT HOST " + urlString + "*****************\n");
				graph.getUrls(theURL, numNodes); 
				graph.generateGraphFromLinks();
				
				nodesString = Integer.toString(numNodes);
				while (nodesString.length() < 4)
					nodesString = "0".concat(nodesString);
				
				newFileName = subDir.toString() + "/" + nodesString + "_" + theURL.getHost() + ".dat";
				
				try {
					newFile = new File(newFileName);
					newFile.createNewFile();
					System.out.println("\n**************WRITING GRAPH TO FILE: " + theURL.getHost() + ".dat******************\n");
					graph.write(new BufferedWriter(new FileWriter(new File(newFileName))));
					System.out.println("\n**************FILE WRITTEN: " + newFileName + "********************\n");
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
	}
	
	

}

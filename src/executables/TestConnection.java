package executables;

import java.net.URL;
import java.util.TreeSet;
import java.io.*;
import datastructures.*;
import algorithms.*;
import webcrawler.*;

public class TestConnection {

		public static void main(String[] args) throws Exception {
			
			testGettingUrls();
			
		}
		
		private static void testGettingUrls() throws Exception {
			UrlGraph graph = new UrlGraph();
			graph.getUrls(new URL("http://www.msdn.com"), 30);
			System.out.println(graph.getListOfUrls());
			graph.generateAdjacencyMatrixFromLinks(); 
			graph.printAdjacencyMatrix();
			
			SCCAlgorithm algorithm = new SCCAlgorithm(graph); 
			algorithm.getSCCs();
			algorithm.printComponents(); 
			
			graph.write(new BufferedWriter(new FileWriter(new File("test.txt"))));
			
			Graph fromFileGraph = new Graph(new BufferedReader(new FileReader(new File("test.txt"))));
			fromFileGraph.printAdjacencyMatrix();
		}
		
		private static void testWebParser() throws Exception {
			
			WebPageParser wpp = new WebPageParser();
			wpp.setURL(new URL("http://www.google.com"));
			TreeSet<URL> links = wpp.getLinks();
			
			for (URL u: links)
				System.out.println(u);
			
		}
}

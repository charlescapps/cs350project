package webcrawler;

import java.net.URL;
import java.util.TreeSet;

public class TestConnection {

		public static void main(String[] args) throws Exception {
			
			testGettingUrls();
			
		}
		
		private static void testGettingUrls() throws Exception {
			UrlGraph graph = new UrlGraph();
			graph.getUrls(new URL("http://www.pdx.edu"), 20);
			System.out.println(graph.getListOfUrls());
			graph.generateAdjacencyMatrixFromLinks(); 
			graph.printAdjacencyMatrix();
			
			SCCAlgorithm algorithm = new SCCAlgorithm(graph); 
			algorithm.getSCCs();
			algorithm.printComponents(); 
			
		}
		
		private static void testWebParser() throws Exception {
			
			WebPageParser wpp = new WebPageParser();
			wpp.setURL(new URL("http://www.google.com"));
			TreeSet<URL> links = wpp.getLinks();
			
			for (URL u: links)
				System.out.println(u);
			
		}
}

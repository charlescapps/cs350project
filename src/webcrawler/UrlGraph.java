package webcrawler;

import java.util.TreeSet;
import java.util.ArrayList;
import java.net.URL;
import datastructures.*;

public class UrlGraph extends Graph{
	
	private ArrayList<TreeSet<URL>> allLinks; 
	
	private WebPageParser wpp; //Makes HTTP connections to URLs and uses REGEX's to search the contents for links 
	private static URLHostComparator urlComp = new URLHostComparator(); 
	
	public UrlGraph(){
		super();
		allLinks = new ArrayList<TreeSet<URL>>();
		wpp = new WebPageParser();
	}
	
	/*
	 * @author Charles L. Capps
	 * 
	 * Function to perform a breadth first search on the 'net starting at the seed URL (url)
	 * to get max_nodes nodes. 
	 * 
	 * Uses the WebPageParser class to get a list of all the links
	 * 
	 */
	public void getUrls(URL url, int max_nodes) throws Exception {
		//At least 1/2 of the given links should valid links, so don't allow the queue to get bigger than 2x(the # of nodes we need)
		//Worse comes to worst, this limit would prevent this algorithm from being a true "breadth first search" of the internet, 
		//But I am trying it, because I think it will greatly reduce # of connections made / increase performance
		int maxQueueSize = max_nodes*2; 
		
		UrlNode origin = new UrlNode(url);
		
		GeneralQueue<UrlNode> queue = new GeneralQueue<UrlNode>();
		queue.enQueue(origin);
		
		UrlNode node;
		TreeSet<URL> links;
		
		while (!queue.isEmpty() && allNodes.size() < max_nodes) {
			//Dequeue a node
			node = queue.deQueue();
			
			try {
				System.out.println("\n************Nodes added so far: " + allNodes.size() + "************\n");
				
				//Set URL for WebPageParser and connect
				wpp.setURL(node.getURL());
				links = wpp.getLinks();
				wpp.disconnect();
				System.out.println("\n*************Disconnecting from URL...*************");
				
				//Add URL to allNodes if we successfully connected to it
				allNodes.add(node);
				node.setIndex(allNodes.size()-1);
				
				allLinks.add(links);
				
				//Parse web page and Add nodes for each link to queue IF queue isn't over-loaded
				if (queue.getSize() < maxQueueSize) {
					
					for (URL link:links) {
						if (! (this.indexOfUrl(link) >=0 ) && !queue.contains(new UrlNode(link))) {          //If this URL isn't already in the graph
							queue.enQueue(new UrlNode(link)); //Enqueue a new node to be added to the graph
						}
					}
				}
				
			}
			catch (Exception e){
				System.out.println("Failed to connect to URL " + node.getURL());
				wpp.disconnect();
			}
		}
	}
	
	public void generateAdjacencyMatrixFromLinks() {
		
		UrlNode node = null; 
		int j = -1; 
		
		for (int i= 0; i < allNodes.size(); i++) {
			
			node = (UrlNode) allNodes.get(i);
			
			
			for (URL url: allLinks.get(i)) {
				if ( (j = indexOfUrl(url) ) >=0) {
					node.addEdge(allNodes.get(j));
				}
			}
		}
	}
	
	private int indexOfUrl(URL url) {
		for (int i = 0; i < allNodes.size(); i++) {
			if (urlComp.compare(url, ((UrlNode) allNodes.get(i)).getURL() ) == 0)
				return i; 
		}
		
		return -1; 
	}
	
	public String getListOfUrls() {
		StringBuilder urlList = new StringBuilder();
		for (GraphNode node:allNodes) {
			urlList.append( ((UrlNode)node).getURL().toString() + "\n");
		}
		
		return urlList.toString();
	}
	
}

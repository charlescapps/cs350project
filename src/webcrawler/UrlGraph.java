package webcrawler;

import java.util.TreeSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import datastructures.*;

public class UrlGraph {
	
	List<UrlNode> allNodes; //Use TreeSet so URL's with same host are considered the same (throw out URI)
	List<Set<URL>> allLinks; 
	
	WebPageParser wpp; //Makes HTTP connections to URLs and uses REGEX's to search the contents for links 
	
	public UrlGraph(){
		allNodes = new ArrayList<UrlNode>();
		allLinks = new ArrayList<Set<URL>>();
		wpp = new WebPageParser();
	}

	public List<UrlNode> getAllNodes() {
		return allNodes; 
	}
	
	public UrlGraph clone() {
		UrlGraph theClone = new UrlGraph(); 
		
		//Theta (|V|) to add all nodes to new graph
		for (UrlNode node:allNodes) {
			theClone.allNodes.add(node.clone());
		}
		
		for (UrlNode node:allNodes) {
			for (UrlNode adjacentNode: node.getAdjList() ) {
				theClone.allNodes.get(node.getIndex()).addEdge(theClone.allNodes.get(adjacentNode.getIndex()));
			}
		}
		
		return theClone;
	}
	
	public UrlGraph getTranspose() {
		UrlGraph theClone = new UrlGraph(); 
		
		//Theta (|V|) to add all nodes to new graph
		for (UrlNode node:allNodes) {
			theClone.allNodes.add(node.clone());
		}
		
		for (UrlNode node:allNodes) {
			for (UrlNode adjacentNode: node.getAdjList() ) {
				theClone.allNodes.get(adjacentNode.getIndex()).addEdge(theClone.allNodes.get(node.getIndex()));
			}
		}
		
		return theClone;
	}
	
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
				
				//Set URL for WebPageParser and connect
				wpp.setURL(node.getURL());
				links = wpp.getLinks();
				
				//Add URL to allNodes if we successfully connected to it
				allNodes.add(node);
				node.setIndex(allNodes.size()-1);
				
				allLinks.add(links);
				
				//Parse web page and Add nodes for each link to queue IF queue isn't over-loaded
				if (queue.getSize() < maxQueueSize) {
					
					
					
					for (URL link:links) {
						if (!this.containsURL(link) && !queue.contains(new UrlNode(link))) {          //If this URL isn't already in the graph
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
			
			node = allNodes.get(i);
			
			
			for (URL url: allLinks.get(i)) {
				if ( (j = indexOfUrl(url) ) >=0) {
					node.addEdge(allNodes.get(j));
				}
			}
		}
	}
	
	public boolean containsURL(URL u) {
		if (allNodes.contains(new UrlNode(u)))
			return true;
		
		return false;
	}
	
	private int indexOfUrl(URL url) {
		for (int i = 0; i < allNodes.size(); i++) {
			if (url.getHost().equals(allNodes.get(i).getURL().getHost()))
				return i; 
		}
		
		return -1; 
	}
	
	public String getListOfUrls() {
		StringBuilder urlList = new StringBuilder();
		for (UrlNode node:allNodes) {
			urlList.append(node.getURL().toString() + "\n");
		}
		
		return urlList.toString();
	}
	
	public void printAdjacencyMatrix() {
		int[][] adjMatrix = new int[allNodes.size()][allNodes.size()]; 
		
		for (int i = 0; i < allNodes.size(); i ++)
			for (int j = 0; j < allNodes.size(); j++) 
				adjMatrix[i][j] = 0; 
		
		
		for (UrlNode node: allNodes) {
			for (UrlNode adjNode: node.getAdjList()) {
				adjMatrix[node.getIndex()][adjNode.getIndex()] = 1; 
			}
		}
		
		for (int i = 0; i < allNodes.size(); i ++) {
			for (int j = 0; j < allNodes.size(); j++) 
				System.out.print(adjMatrix[i][j]);
			
			System.out.println();
		}
		
	}

	public void printDiscoveryTimes() {
		for (UrlNode node: allNodes)
			System.out.println("DC Time of " + node.getURL() + ": " + node.getDiscoverTime());
	}
}

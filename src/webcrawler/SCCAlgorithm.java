package webcrawler;

import java.util.Set; 
import java.util.List; 
import java.util.TreeSet; 
import java.util.ArrayList; 
import datastructures.*;

/*
 * @author Charles L Capps
 * Implementation of Kosaraju's algorithm
 * 
 */

public class SCCAlgorithm {
	private UrlGraph theGraph;
	private UrlGraph reversedGraph; 
	private int[] discoveredNodes;  //Array of node indexes in graph indexed by discovery time
	private List<TreeSet<UrlNode>> sCCs;
	
	public SCCAlgorithm(UrlGraph aGraph)  {
		theGraph = aGraph; 
		discoveredNodes = new int[theGraph.getAllNodes().size()];
		sCCs = new ArrayList<TreeSet<UrlNode>>();
	}
	
	public List<TreeSet<UrlNode>> getSCCs() throws Exception{
		performBFS();
		
		reversedGraph = theGraph.getTranspose(); //Clones the graph in O(E+V) time with direction of edges reversed
		
		performReversedBFS();
		
		return sCCs; 
	}
	
	//Performs a BFS and records discovery times in each node. Places each node in Array indexed by discovery time. 
	private void performBFS() throws Exception{
		UrlNode node; 
		int dTime = 0; //Discovery time starts at 0
		int minUnvisited = 0; //Lowest index of an unvisited node
		List<UrlNode> allNodes = theGraph.getAllNodes(); //get list of nodes from the graph
		GeneralQueue<UrlNode> queue = new GeneralQueue<UrlNode>(); //Queue for BFS
		
		while (dTime < allNodes.size()) {  //While we haven't discovered all nodes
		
			//Get the first remaining unvisited node and add it to the queue
			while (minUnvisited < allNodes.size() && allNodes.get(minUnvisited).getColor() != UrlNode.Color.UNVISITED )
				minUnvisited++; 
			
			if (minUnvisited < allNodes.size())
				queue.enQueue(allNodes.get(minUnvisited));
			else
				break; 
			
			while (!queue.isEmpty()) {
				node = queue.deQueue(); 
				discoveredNodes[dTime] = node.getIndex(); // Place index of node in array indexed by Discovery Time
				node.setDiscoverTime(dTime++);
				node.setColor(UrlNode.Color.VISITED);
				
				for (UrlNode adjNode: node.getAdjList()) {  //Add adjacent nodes to queue from Adjacency List
					if (adjNode.getColor() == UrlNode.Color.UNVISITED) {
						queue.enQueue(adjNode); 
						adjNode.setColor(UrlNode.Color.DISCOVERED);
					}			
				}
			}
		}
	}

	private void performReversedBFS() throws Exception {
		UrlNode node; 
		int maxDTime = discoveredNodes.length - 1; //Largest discovery time of an unvisited node
		List<UrlNode> allNodes = reversedGraph.getAllNodes(); //get list of nodes from the REVERSED graph
		GeneralQueue<UrlNode> queue = new GeneralQueue<UrlNode>(); //Queue for BFS
		
		while (maxDTime >= 0) {  //While we haven't visited all nodes
			
			//Get the first remaining unvisited node and add it to the queue
			while (maxDTime >= 0 && allNodes.get(discoveredNodes[maxDTime]).getColor() != UrlNode.Color.UNVISITED)
				maxDTime--; 
			
			if (maxDTime >= 0)
				queue.enQueue(allNodes.get(discoveredNodes[maxDTime]));  //Add the first node in the next component
			else
				break; //If maxDTime < 0, we have found every connected component, so break from the main loop
			
			sCCs.add(new TreeSet<UrlNode>()); //Add TreeSet for new Strongly Connected Component
			
			while (!queue.isEmpty()) {
				node = queue.deQueue(); 
				node.setColor(UrlNode.Color.VISITED);
				sCCs.get(sCCs.size() - 1).add(node); //Add the node to the current connected component
				
				for (UrlNode adjNode: node.getAdjList()) {  //Add adjacent nodes to queue from Adjacency List
					if (adjNode.getColor() == UrlNode.Color.UNVISITED) {
						queue.enQueue(adjNode); 
						adjNode.setColor(UrlNode.Color.DISCOVERED);
					}			
				}
			}
		}
		
	}
	
	public void printComponents() {
		for (int i = 0; i < sCCs.size(); i++) {
			System.out.println("SCC # " + i + ":");
			
			for (UrlNode n: sCCs.get(i)) {
				System.out.println("\t" + n);
			}
		}
		
		
	}
}

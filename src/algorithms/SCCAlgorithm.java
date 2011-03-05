package algorithms;

import java.util.Collections; //To sort the strongly connected components before writing them to disk
import java.util.List; 
import java.util.ArrayList; 
import datastructures.*;

/*
 * 
 * Implementation of Kosaraju's algorithm. <BR> <BR>
 * 
 * Uses java.util.ArrayList data structure and is slightly different than the basic algorithm in some other ways. 
 * 
 * @author Charles L Capps
 */

public class SCCAlgorithm implements SCCAlgorithmInterface{
	private Graph theGraph;
	private Graph reversedGraph; 
	private int[] nodeFinishedAt;  //Array of node indexes in graph indexed by finish time
	private ArrayList<ArrayList<GraphNode>> sCCs;
	private static GraphNodeComparator nodeCompare = new GraphNodeComparator(); 
	
	/** @param Graph to search for strongly connected components.*/
	public SCCAlgorithm(Graph aGraph) {
		theGraph = aGraph; 
		nodeFinishedAt = new int[theGraph.getAllNodes().size()];
		sCCs = new ArrayList<ArrayList<GraphNode>>(); 
	}
	
	/** All code considered part of the algorithm. */
	 public void executeAlgorithm() {
		performDFS();
		reversedGraph = theGraph.getTranspose(); //Clones the graph in O(|E|+|V|) time with direction of edges reversed
		performReversedDFS();
 
	}
	 
	 /**@return the ArrayList<ArrayList<GraphNode>> representation of the strongly connected components*/
	 public ArrayList<ArrayList<GraphNode>> getSCCs() {
		 return sCCs;
	 }
	
	/**Performs the 1st DFS and records discovery times in each node. Places each node in Array indexed by discovery time.*/
	private void performDFS() {
		int finishTime = 0; //Finish time starts at 0
		int minUnvisited = 0; //Lowest index of an unvisited node
		List<GraphNode> allNodes = theGraph.getAllNodes(); //get list of nodes from the graph
		
		while (finishTime < allNodes.size() && minUnvisited < allNodes.size()) {  //While we haven't discovered all nodes
		
			//Get the first remaining unvisited node and visit it
			while (minUnvisited < allNodes.size() && allNodes.get(minUnvisited).getColor() != GraphNode.Color.UNVISITED )
				minUnvisited++; 
			
			//If we haven't visited all nodes, then visit the next unvisited one
			if (minUnvisited < allNodes.size())
				finishTime = visitAndRecordFinish(allNodes.get(minUnvisited), finishTime);
			
		}
	}

	/**Performs the 2nd DFS and builds the strongly connected components*/
	private void performReversedDFS() {
		int maxFinishTime = nodeFinishedAt.length - 1;          //Highest finish time of an unvisited node
		List<GraphNode> allNodes = reversedGraph.getAllNodes(); //Get list of nodes from the REVERSED graph
		
		while (maxFinishTime >= 0) {  //While we haven't visited all nodes
			
			//Get the index of a remaining unvisited node with the highest finish time
			while (maxFinishTime >= 0 && allNodes.get(nodeFinishedAt[maxFinishTime]).getColor() != GraphNode.Color.UNVISITED)
				maxFinishTime--; 
			
			//If we haven't visited all nodes, start a new strongly connected component, and visit the next one
			if (maxFinishTime >= 0) {
				sCCs.add(new ArrayList<GraphNode>()); 
				visitAndAddToComponent(allNodes.get(nodeFinishedAt[maxFinishTime]));
			}
		}
	}
	
	/**Recursive visit function that records the finish times for the 1st DFS. <BR>
	 * Returns the finish time to avoid having more member variables than necessary. */
	private int visitAndRecordFinish(GraphNode node, int time) { //Returns new time value
		
		node.setColor(GraphNode.Color.DISCOVERED);
		
		for (GraphNode n: node.getAdjList()) {
			if (n.getColor()==GraphNode.Color.UNVISITED) {
				time = visitAndRecordFinish(n, time);
			}
		}
		
		node.setColor(GraphNode.Color.VISITED);
		nodeFinishedAt[time++] = node.getIndex(); //Record Finish time
		return time; 
	}
	
	/**Recursive visit function that builds the strongly connected components for the 2nd DFS*/
	private void visitAndAddToComponent(GraphNode node) {
		
		node.setColor(GraphNode.Color.DISCOVERED);
		
		for (GraphNode n: node.getAdjList()) {
			if (n.getColor()==GraphNode.Color.UNVISITED) {
				visitAndAddToComponent(n);
			}
		}
		
		node.setColor(GraphNode.Color.VISITED);
		sCCs.get(sCCs.size()-1).add(node);
	}
	
	/**Print the strongly connected components to screen*/
	public void printComponents() {
		for (int i = 0; i < sCCs.size(); i++) {
			System.out.println("SCC # " + i + ":");
			
			for (GraphNode n: sCCs.get(i)) {
				System.out.println("\t" + n);
			}
		}
	}
	
	/**Return a string representing the strongly connected components, sorted for readability.
	 * @return a String representing the strongly connected components of the graph. */
	public String componentsToString() {
		for (int i = 0; i < sCCs.size(); i++) { //Sort each Connected Component for easy viewing
			Collections.sort(sCCs.get(i), nodeCompare);
		}
		String s = ""; 
		for (int i = 0; i < sCCs.size(); i++) {
			s += ("SCC # " + i + ":\n");
			
			for (GraphNode n: sCCs.get(i)) {
				s += ("\t" + n + "\n");
			}
		}
		return s; 
	}

	/**Print the finish times to screen, for verifying purposes*/
	public void printFinishTimes() {
		System.out.println("Order nodes were visited:");
		for (int i = 0; i < nodeFinishedAt.length; i++)
			System.out.println("\tNode # " + nodeFinishedAt[i] );
	}
}

package algorithms;

import datastructures.*;

/*
 * 
 * Implementation of Kosaraju's algorithm
 * 
 * This is the BASIC version, which avoids using any Java data structures, other than for iterating through the Adjacency list of a node. 
 * Uses basic arrays[] and my LinkedList<T> data type. 
 * 
 * 
 * @author Charles L Capps
 * 
 * 
 */

public class SCCAlgorithmBasic implements SCCAlgorithmInterface {
	private GraphNode[] theGraph;
	private GraphNode[] reversedGraph; 
	private int[] nodeFinishedAt;  //Array of node indexes in graph indexed by finish time
	private LinkedList<GraphNode> sCCs[]; //Implement SCCs as an Array of Linked Lists
								//Not allowed to use Templates in Basic Arrays, but will only add GraphNodes
	
	private int indexOfLastSCC;
	private int currentTime; 
	
	@SuppressWarnings("unchecked")
	public SCCAlgorithmBasic(Graph aGraph)  {
		theGraph = aGraph.getAllNodes().toArray(new GraphNode[0]); 
		reversedGraph = new GraphNode[theGraph.length];
		
		for (int i = 0; i < reversedGraph.length; i++) {  //Initialize reversedGraph's nodes
			reversedGraph[i] = new GraphNode(i);		//Unvisited node with an empty adjacency list
		}
		
		
		nodeFinishedAt = new int[theGraph.length];
		
		//There are at most |V| Strongly Connected Components, so 
		//for simplicity we initialize sCCs to an Array of size |V|
		sCCs = new LinkedList[theGraph.length];
		
		//Initialize each Linked List of GraphNodes
		for (int i = 0; i < sCCs.length; i++) {
			sCCs[i] = new LinkedList<GraphNode>();
		}
		
		indexOfLastSCC = -1; //No SCC's added yet, so max index is -1
		currentTime = 0; 
	}
	
	public void executeAlgorithm() throws Exception{ //Function encapsulating the algorithm
													//Stores representation of Strongly-connected components in SCCs variable
		performDFS();
		
		getTranspose(); //Clones the graph in O(E+V) time with direction of edges reversed
		
		performReversedDFS();
	}
	
	public LinkedList<GraphNode>[] getSCCs() {
		return sCCs;
	}
	
	private void getTranspose() { //Simple function to get transpose without using any built-in Java types
								 //Except for the Adjacency lists, which are ArrayList<GraphNode>, but it just iterates through these
		
		for (int i = 0; i < theGraph.length; i++) {
			for (GraphNode adjNode: theGraph[i].getAdjList()) {
				 reversedGraph[adjNode.getIndex()].addEdge(reversedGraph[i]); //Add edge going in reverse direction
			}
		}
		
	}
	
	//Performs a DFS and records discovery times in each node. Places each node in Array indexed by discovery time. 
	private void performDFS() throws Exception{
		int finishTime = 0; //Finish time starts at 0
		int minUnvisited = 0; //Lowest index of an unvisited node
		
		while (finishTime < theGraph.length && minUnvisited < theGraph.length) {  //While we haven't discovered all nodes
		
			//Get the first remaining unvisited node and visit it
			while (minUnvisited < theGraph.length && theGraph[minUnvisited].getColor() != GraphNode.Color.UNVISITED )
				minUnvisited++; 
			
			//If we haven't visited everything
			if (minUnvisited < theGraph.length)
				visitAndRecordFinish(theGraph[minUnvisited]);
			
		}
	}

	private void performReversedDFS() throws Exception {
		int maxFinishTime = nodeFinishedAt.length - 1; //Largest finish time of an unvisited node
		
		while (maxFinishTime >= 0) {  //While we haven't visited all nodes
			
			//Get the first remaining unvisited node and add it to the stack
			while (maxFinishTime >= 0 && reversedGraph[nodeFinishedAt[maxFinishTime]].getColor() != GraphNode.Color.UNVISITED)
				maxFinishTime--; 
			
			if (maxFinishTime >= 0) {
				indexOfLastSCC++; //Increase the index we are looking at, i.e. start a new SCC
				visitAndAddToComponent(reversedGraph[nodeFinishedAt[maxFinishTime]]);
			}
		}
	}
	
	private void visitAndRecordFinish(GraphNode node) { //Discovers node, then discovers all nodes in Adj List, then visits node
		
		node.setColor(GraphNode.Color.DISCOVERED);
		
		for (GraphNode n: node.getAdjList()) {
			if (n.getColor()==GraphNode.Color.UNVISITED) {
				visitAndRecordFinish(n);
			}
		}
		
		node.setColor(GraphNode.Color.VISITED);
		nodeFinishedAt[currentTime++] = node.getIndex(); //Record Finish time
	
	}
	
	private void visitAndAddToComponent(GraphNode node) {
		
		node.setColor(GraphNode.Color.DISCOVERED);
		
		for (GraphNode n: node.getAdjList()) {
			if (n.getColor()==GraphNode.Color.UNVISITED) {
				visitAndAddToComponent(n);
			}
		}
		
		node.setColor(GraphNode.Color.VISITED);
		sCCs[indexOfLastSCC].addToFront(new LinkedListNode<GraphNode>(node)); //Add node to Linked List for current SCC
	}
	
	public void printComponents() {
		for (int i = 0; i < sCCs.length; i++) {
			System.out.println("SCC # " + i + ":");
			
			LinkedListNode<GraphNode> tmp = sCCs[i].getHead(); 
			
			while (tmp!=null) {
				System.out.println("\t" + tmp.getData());
				tmp = tmp.getNext();
			}
		}
	}
	
	public String componentsToString() {
		String s = "";
		for (int i = 0; i < sCCs.length; i++) {
			s += ("SCC # " + i + ":");
			
			LinkedListNode<GraphNode> tmp = sCCs[i].getHead(); 
			
			while (tmp!=null) {
				s += ("\t" + tmp.getData());
				tmp = tmp.getNext();
			}
		}
		
		return s; 
	}

	public void printFinishTimes() {
		System.out.println("Order nodes were visited:");
		for (int i = 0; i < nodeFinishedAt.length; i++)
			System.out.println("\tNode # " + nodeFinishedAt[i] );
	}
}

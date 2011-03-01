package algorithms;

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
	private Graph theGraph;
	private Graph reversedGraph; 
	private int[] nodeFinishedAt;  //Array of node indexes in graph indexed by finish time
	private ArrayList<TreeSet<GraphNode>> sCCs;
	
	public SCCAlgorithm(Graph aGraph)  {
		theGraph = aGraph; 
		nodeFinishedAt = new int[theGraph.getAllNodes().size()];
		sCCs = new ArrayList<TreeSet<GraphNode>>();
	}
	
	public ArrayList<TreeSet<GraphNode>> getSCCs() throws Exception{
		performDFS();
		//printFinishTimes();
		
		reversedGraph = theGraph.getTranspose(); //Clones the graph in O(E+V) time with direction of edges reversed
		
		performReversedDFS();
		
		return sCCs; 
	}
	
	//Performs a DFS and records discovery times in each node. Places each node in Array indexed by discovery time. 
	private void performDFS() throws Exception{
		int finishTime = 0; //Finish time starts at 0
		int minUnvisited = 0; //Lowest index of an unvisited node
		List<GraphNode> allNodes = theGraph.getAllNodes(); //get list of nodes from the graph
		
		while (finishTime < allNodes.size() && minUnvisited < allNodes.size()) {  //While we haven't discovered all nodes
		
			//Get the first remaining unvisited node and visit it
			while (minUnvisited < allNodes.size() && allNodes.get(minUnvisited).getColor() != GraphNode.Color.UNVISITED )
				minUnvisited++; 
			
			if (minUnvisited < allNodes.size())
				finishTime = visitAndRecordFinish(allNodes.get(minUnvisited), finishTime);
			
		}
	}

	private void performReversedDFS() throws Exception {
		int maxFinishTime = nodeFinishedAt.length - 1; //Largest finish time of an unvisited node
		List<GraphNode> allNodes = reversedGraph.getAllNodes(); //get list of nodes from the REVERSED graph
		
		while (maxFinishTime >= 0) {  //While we haven't visited all nodes
			
			//Get the first remaining unvisited node and add it to the stack
			while (maxFinishTime >= 0 && allNodes.get(nodeFinishedAt[maxFinishTime]).getColor() != GraphNode.Color.UNVISITED)
				maxFinishTime--; 
			
			if (maxFinishTime >= 0) {
				sCCs.add(new TreeSet<GraphNode>()); //Add TreeSet for new Strongly Connected Component
				visitAndAddToComponent(allNodes.get(nodeFinishedAt[maxFinishTime]));
			}
		}
	}
	
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
	
	public void printComponents() {
		for (int i = 0; i < sCCs.size(); i++) {
			System.out.println("SCC # " + i + ":");
			
			for (GraphNode n: sCCs.get(i)) {
				System.out.println("\t" + n);
			}
			/*for (GraphNode n: sCCs.get(i))
				System.out.println("\tNode #" + n.getIndex());
				*/
		}
		
		
	}

	public void printFinishTimes() {
		System.out.println("Order nodes were visited:");
		for (int i = 0; i < nodeFinishedAt.length; i++)
			System.out.println("\tNode # " + nodeFinishedAt[i] );
	}
}

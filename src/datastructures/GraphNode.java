package datastructures;

import java.util.List;
import java.util.ArrayList; 

public class GraphNode {
	
	public static enum Color {UNVISITED, DISCOVERED, VISITED};
	
	protected int index;  
	protected Color myColor;
	
	protected List<GraphNode> adjList; 
	
	public GraphNode() {
		myColor = Color.UNVISITED;
		 
		index = -1; 
		adjList = new ArrayList<GraphNode>();
	}
	
	public GraphNode(int _index) {
		myColor = Color.UNVISITED;
		 
		index = _index; 
		adjList = new ArrayList<GraphNode>();
	}
	
	public GraphNode clone() {
		GraphNode theClone = new GraphNode();
		theClone.index = this.index; 
		
		return theClone;
	}
	
	public int compareTo(GraphNode node) {
		return this.index - node.index; 
	}
	
	public void addEdge(GraphNode node){
		adjList.add(node);
	}
	
	public List<GraphNode> getAdjList() {
		return adjList;
	}

	public void setColor(Color color) {
		this.myColor = color;
	}

	public Color getColor() {
		return myColor;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i; 
	}
	
	public String toString() {
		return "Node # " + index + ", status= " + myColor.toString();
	}

}

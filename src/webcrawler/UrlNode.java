package webcrawler;

import java.net.URL; 
import java.util.TreeSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList; 

public class UrlNode implements Comparable<UrlNode> {
	
	public static enum Color {UNVISITED, DISCOVERED, VISITED};

	private static final URLHostComparator urlComp = new URLHostComparator();
	
	private int index; 
	private URL theURL; 
	private Color myColor;
	private int discoverTime; 
	
	private List<UrlNode> adjList; 
	
	public UrlNode(URL url) {
		theURL = url;
		myColor = Color.UNVISITED;
		setDiscoverTime(-1); 
		index = -1; 
		adjList = new ArrayList<UrlNode>();
	}
	
	public UrlNode clone() {
		UrlNode theClone = new UrlNode(theURL);
		theClone.index = this.index; 
		theClone.discoverTime = this.discoverTime; 
		
		return theClone;
	}
	
	public void addEdge(UrlNode node){
		adjList.add(node);
	}
	
	public List<UrlNode> getAdjList() {
		return adjList;
	}
	
	public int compareTo(UrlNode node) {
		return urlComp.compare(theURL, node.theURL);
	}
	
	public boolean equals(UrlNode node) {
		return urlComp.equals(node.theURL);
	}
	
	public int compareTo(URL url) {
		return urlComp.compare(theURL, url);
	}
	
	public boolean equals(URL url) {
		return urlComp.equals(theURL, url);
	}

	public void setColor(Color color) {
		this.myColor = color;
	}

	public Color getColor() {
		return myColor;
	}
	
	public URL getURL() {
		return theURL;
	}

	public void setDiscoverTime(int discoverTime) {
		this.discoverTime = discoverTime;
	}

	public int getDiscoverTime() {
		return discoverTime;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i; 
	}
	
	public String toString() {
		return theURL.toString();
	}

}

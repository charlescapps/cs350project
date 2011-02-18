package webcrawler;

import java.net.URL; 
import datastructures.GraphNode;

public class UrlNode extends GraphNode implements Comparable<UrlNode> {

	private static final URLHostComparator urlComp = new URLHostComparator();
	
	private URL theURL; 
	
	public UrlNode(URL url) {
		super();
		theURL = url;
		
	}
	
	public UrlNode clone() {
		UrlNode theClone = new UrlNode(this.theURL);
		theClone.index = this.index; 
		
		return theClone;
	}
	
	public GraphNode toBasicGraphNode() {
		GraphNode theClone = new GraphNode();
		theClone.setIndex(this.index);
		
		return theClone;
		
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

	
	public URL getURL() {
		return theURL;
	}
	
	public String toString() {
		return theURL.toString();
	}

}

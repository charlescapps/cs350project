package webcrawler;

import java.net.URL; 
import java.util.TreeSet;

public class UrlNode implements Comparable<UrlNode> {

	private static final URLHostComparator urlComp = new URLHostComparator();
	
	private URL theURL; 
	private boolean discovered;
	private boolean searched;
	private TreeSet<UrlNode> adjList; //Set of adjacent nodes--shouldn't have multiple copies of an adjacent node
	
	public UrlNode() {
		setDiscovered(setSearched(false)); 
		adjList = new TreeSet<UrlNode>();
	}
	
	public void addEdge(UrlNode node) {
		adjList.add(node);
	}
	
	@Override
	public int compareTo(UrlNode node) {
		// TODO Auto-generated method stub
		return urlComp.compare(theURL, node.theURL);
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public boolean setSearched(boolean searched) {
		this.searched = searched;
		return searched;
	}

	public boolean isSearched() {
		return searched;
	}

}

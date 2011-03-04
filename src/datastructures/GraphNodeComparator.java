package datastructures;

import java.util.Comparator;

public class GraphNodeComparator implements Comparator<GraphNode> {

	@Override
	public int compare(GraphNode n1, GraphNode n2) {
		// TODO Auto-generated method stub
		return (n1.getIndex() - n2.getIndex());
	}

}

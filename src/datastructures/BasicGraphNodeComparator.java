package datastructures;

import java.util.Comparator;

public class BasicGraphNodeComparator implements Comparator<BasicGraphNode> {

    @Override
    public int compare(BasicGraphNode n1, BasicGraphNode n2) {
        // TODO Auto-generated method stub
        return (n1.getIndex() - n2.getIndex());
    }

}

package datastructures;

public class BasicGraphNode {

    public static enum Color {
        UNVISITED, DISCOVERED, VISITED
    };

    protected int index;
    protected Color myColor;

    protected LinkedList<BasicGraphNode> adjList;

    public BasicGraphNode() {
        myColor = Color.UNVISITED;

        index = -1;
        adjList = new LinkedList<BasicGraphNode>();
    }

    public BasicGraphNode(int _index) {
        myColor = Color.UNVISITED;

        index = _index;
        adjList = new LinkedList<BasicGraphNode>();
    }

    public BasicGraphNode clone() {
        BasicGraphNode theClone = new BasicGraphNode();
        theClone.index = this.index;

        return theClone;
    }

    public int compareTo(GraphNode node) {
        return this.index - node.index;
    }

    public void addEdge(BasicGraphNode node) {
        adjList.addToFront(new LinkedListNode<BasicGraphNode>(node));
    }

    public LinkedList<BasicGraphNode> getAdjList() {
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

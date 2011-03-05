package algorithms;

import java.util.ArrayList; //Just used for sorting the SCC's for printing
import java.util.TreeSet; //Not used as part of algorithm

import datastructures.*;

/**
 * Implementation of Kosaraju's algorithm
 * 
 * This is the BASIC version, which avoids using any Java API data structures,
 * other than for iterating through the Adjacency list of a node. Uses basic
 * arrays[] and my LinkedList<T> data type from my custom datastructures
 * package.
 * 
 * @author Charles L Capps
 */

public class SCCAlgorithmBasic implements SCCAlgorithmInterface {
    private BasicGraphNode[] theGraph; // Graph just represented as an array of
    // GraphNode's
    private BasicGraphNode[] reversedGraph; // Structure that will contain the
    // reversed graph
    private int[] nodeFinishedAt; // nodeFinishedAt[i] contains the index of the
                                  // node that finished at time i
    private LinkedList<BasicGraphNode> sCCs[]; // Implement SCCs as an Array of
    // Linked Lists

    private int indexOfLastSCC; // Keep track of what SCC is currently being
                                // added to
    private int currentTime; // Current time for DFS.

    @SuppressWarnings("unchecked")
    // Otherwise compiler gives a warning, because basic arrays can't be
    // declared with a template
    // And classes with templates should always be passed a type --> warning is
    // unavoidable!
    /**
     * Constructor - takes the Graph to be searched for strongly connected components. 
     * @param - Graph to search for strongly connected components
     */
    public SCCAlgorithmBasic(Graph aGraph) {
        theGraph = aGraph.toBasicGraph();

        reversedGraph = new BasicGraphNode[theGraph.length]; //Init reversed graph

        for (int i = 0; i < reversedGraph.length; i++) {
            reversedGraph[i] = new BasicGraphNode(i);
        }

        nodeFinishedAt = new int[theGraph.length];

        // There are at most |V| Strongly Connected Components, so
        // for simplicity we initialize sCCs to an Array of size |V|
        // And keep track of the last index. Need O(1) access time, but
        // don't know in advance how many SCC's there will be!
        sCCs = new LinkedList[theGraph.length];

        // Initialize each Linked List of GraphNodes, technically an O(|V|)
        // operation, but
        // decided to not include in the algorithm proper since it doesn't
        // change the
        // asymptotic complexity.
        for (int i = 0; i < sCCs.length; i++) {
            sCCs[i] = new LinkedList<BasicGraphNode>();
        }
        indexOfLastSCC = -1; // No SCC's added yet, so max index is -1
        currentTime = 0;
    }

    /**
     * All code that is considered as part of the algorithm. Doesn't include
     * printing to screen / writing to file / and some initializations.
     */

    public void executeAlgorithm() {
        performDFS(); // Depth first search + record finish times
        getTranspose(); // Clones the graph in O(|E|+|V|) time with direction of
                        // edges reversed
        performReversedDFS(); // Depth first search + record strongly connected
                              // components
    }

    /**
     * @return the array-of-linked-lists representation of the SCC's. Only valid
     *         after calling executeAlgorithm().
     */
    public LinkedList<BasicGraphNode>[] getSCCs() {
        return sCCs;
    }

    /**
     * Builds the transpose graph in O(|E| + |V|) time, since it simply
     * traverses every node + every adjacency list.
     */
    private void getTranspose() { // Function to get transpose without using any
                                  // Java API data structures
        for (int i = 0; i < theGraph.length; i++) {
            for (BasicGraphNode adjNode : theGraph[i].getAdjList()) {
                reversedGraph[adjNode.getIndex()].addEdge(reversedGraph[i]); //Add edge in reverse direction
            }
        }
    }

    /**
     * Performs a DFS and records discovery times in each node. Places each node
     * index in nodeFinishedAt[] array
     */
    private void performDFS() {
        int finishTime = 0; // Finish time starts at 0
        int minUnvisited = 0; // Lowest index of an unvisited node

        //while we haven't discovered all nodes...
        while (finishTime < theGraph.length && minUnvisited < theGraph.length) {

            // Get the first remaining unvisited node and visit it
            while (minUnvisited < theGraph.length && theGraph[minUnvisited].getColor() != BasicGraphNode.Color.UNVISITED)
                minUnvisited++;

            // If we haven't visited everything, then visit the next unvisited
            // node
            if (minUnvisited < theGraph.length)
                visitAndRecordFinish(theGraph[minUnvisited]);
        }
    }

    /** Perform the 2nd DFS and record the strongly connected components. */
    private void performReversedDFS() {
        int maxFinishTime = nodeFinishedAt.length - 1; // Largest finish time of
                                                       // an unvisited node

        while (maxFinishTime >= 0) { // While we haven't visited all nodes

            // Get the first remaining unvisited node and add it to the stack
            while (maxFinishTime >= 0 && reversedGraph[nodeFinishedAt[maxFinishTime]].getColor() != BasicGraphNode.Color.UNVISITED)
                maxFinishTime--;

            if (maxFinishTime >= 0) {
                indexOfLastSCC++; // Increase the index we are looking at, i.e.
                                  // start a new SCC
                visitAndAddToComponent(reversedGraph[nodeFinishedAt[maxFinishTime]]);
            }
        }
    }

    /** Recursive visit function for the first DFS -- records finish times */
    private void visitAndRecordFinish(BasicGraphNode node) { // Discovers node, then

        node.setColor(BasicGraphNode.Color.DISCOVERED);

        for (BasicGraphNode n : node.getAdjList()) {
            if (n.getColor() == BasicGraphNode.Color.UNVISITED) {
                visitAndRecordFinish(n);
            }
        }

        node.setColor(BasicGraphNode.Color.VISITED);
        nodeFinishedAt[currentTime++] = node.getIndex(); // Record Finish time
    }

    /**
     * Recursive visit function for the second DFS -- records strongly connected
     * components
     */
    private void visitAndAddToComponent(BasicGraphNode node) {

        node.setColor(BasicGraphNode.Color.DISCOVERED);

        for (BasicGraphNode n : node.getAdjList()) {
            if (n.getColor() == BasicGraphNode.Color.UNVISITED) {
                visitAndAddToComponent(n);
            }
        }

        node.setColor(BasicGraphNode.Color.VISITED);

        sCCs[indexOfLastSCC].addToFront(new LinkedListNode<BasicGraphNode>(node));
    }

    /** Prints the strongly connected components to screen. */
    public void printComponents() {
        for (int i = 0; i < sCCs.length; i++) {
            System.out.println("SCC # " + i + ":");

            LinkedListNode<BasicGraphNode> tmp = sCCs[i].getHead();

            while (tmp != null) {
                System.out.println("\t" + tmp.getData());
                tmp = tmp.getNext();
            }
        }
    }

    /**
     * Returns a String with strongly connected components. Also sorts them for
     * readability
     */
    public String componentsToString() {
        // Begin code to convert array of LinkedList's into
        // ArrayList<TreeSet<GraphNode>> in order to sort
        LinkedListNode<BasicGraphNode> tmp;
        BasicGraphNodeComparator nodeCompare = new BasicGraphNodeComparator();
        ArrayList<TreeSet<BasicGraphNode>> sCCsForPrinting;
        sCCsForPrinting = new ArrayList<TreeSet<BasicGraphNode>>();

        for (int i = 0; i <= indexOfLastSCC; i++) {
            sCCsForPrinting.add(new TreeSet<BasicGraphNode>(nodeCompare));
            tmp = sCCs[i].getHead();

            while (tmp != null) {
                sCCsForPrinting.get(sCCsForPrinting.size() - 1).add(tmp.getData());
                tmp = tmp.getNext();
            }
        }
        // End code to convert array of LinkedList's into
        // ArrayList<TreeSet<GraphNode>>

        String s = "";
        for (int i = 0; i < sCCsForPrinting.size(); i++) {
            s += ("SCC # " + i + ":\n");

            for (BasicGraphNode n : sCCsForPrinting.get(i)) {
                s += ("\t" + n + "\n");
            }
        }
        return s;
    }

    /**
     * Used for debugging and verifying. Prints the order the nodes were visited
     * to screen.
     */
    public void printFinishTimes() {
        System.out.println("Order nodes were visited:");
        for (int i = 0; i < nodeFinishedAt.length; i++)
            System.out.println("\tNode # " + nodeFinishedAt[i]);
    }
}

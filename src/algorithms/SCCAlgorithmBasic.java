package algorithms;

import java.util.ArrayList;
import java.util.TreeSet;

import datastructures.Graph;
import datastructures.GraphNode;
import datastructures.GraphNodeComparator;
import datastructures.LinkedList;
import datastructures.LinkedListNode;
//This is only used for the sake of writing the SCC's to file, 
//NOT as part of the algorithm! 

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
    private GraphNode[] theGraph; // Graph just represented as an array of
                                  // GraphNode's
    private GraphNode[] reversedGraph; // Structure that will contain the
                                       // reversed graph
    private int[] nodeFinishedAt; // nodeFinishedAt[i] contains the index of the
                                  // node that finished at time i
    private LinkedList<GraphNode> sCCs[]; // Implement SCCs as an Array of
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
        theGraph = aGraph.getAllNodes().toArray(new GraphNode[0]); // Take in a
                                                                   // graph, and
                                                                   // put nodes
                                                                   // into the
                                                                   // basic
                                                                   // array
        reversedGraph = (new Graph(theGraph.length)).getAllNodes().toArray(
                new GraphNode[0]); // Instantiate reverse graph
                                   // as a graph with the same # of nodes
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
            sCCs[i] = new LinkedList<GraphNode>();
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
    public LinkedList<GraphNode>[] getSCCs() {
        return sCCs;
    }

    /**
     * Builds the transpose graph in O(|E| + |V|) time, since it simply
     * traverses every node + every adjacency list.
     */
    private void getTranspose() { // Function to get transpose without using any
                                  // Java API data structures
        for (int i = 0; i < theGraph.length; i++) {
            for (GraphNode adjNode : theGraph[i].getAdjList()) {
                reversedGraph[adjNode.getIndex()].addEdge(reversedGraph[i]); // Add
                                                                             // edge
                                                                             // going
                                                                             // in
                                                                             // reverse
                                                                             // direction
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

        while (finishTime < theGraph.length && minUnvisited < theGraph.length) { // While
                                                                                 // we
                                                                                 // haven't
                                                                                 // discovered
                                                                                 // all
                                                                                 // nodes

            // Get the first remaining unvisited node and visit it
            while (minUnvisited < theGraph.length
                    && theGraph[minUnvisited].getColor() != GraphNode.Color.UNVISITED)
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
            while (maxFinishTime >= 0
                    && reversedGraph[nodeFinishedAt[maxFinishTime]].getColor() != GraphNode.Color.UNVISITED)
                maxFinishTime--;

            if (maxFinishTime >= 0) {
                indexOfLastSCC++; // Increase the index we are looking at, i.e.
                                  // start a new SCC
                visitAndAddToComponent(reversedGraph[nodeFinishedAt[maxFinishTime]]);
            }
        }
    }

    /** Recursive visit function for the first DFS -- records finish times */
    private void visitAndRecordFinish(GraphNode node) { // Discovers node, then
                                                        // discovers all nodes
                                                        // in Adj List, then
                                                        // visits node

        node.setColor(GraphNode.Color.DISCOVERED);

        for (GraphNode n : node.getAdjList()) {
            if (n.getColor() == GraphNode.Color.UNVISITED) {
                visitAndRecordFinish(n);
            }
        }

        node.setColor(GraphNode.Color.VISITED);
        nodeFinishedAt[currentTime++] = node.getIndex(); // Record Finish time
    }

    /**
     * Recursive visit function for the second DFS -- records strongly connected
     * components
     */
    private void visitAndAddToComponent(GraphNode node) {

        node.setColor(GraphNode.Color.DISCOVERED);

        for (GraphNode n : node.getAdjList()) {
            if (n.getColor() == GraphNode.Color.UNVISITED) {
                visitAndAddToComponent(n);
            }
        }

        node.setColor(GraphNode.Color.VISITED);
        sCCs[indexOfLastSCC].addToFront(new LinkedListNode<GraphNode>(node)); // Add
                                                                              // node
                                                                              // to
                                                                              // Linked
                                                                              // List
                                                                              // for
                                                                              // current
                                                                              // SCC
    }

    /** Prints the strongly connected components to screen. */
    public void printComponents() {
        for (int i = 0; i < sCCs.length; i++) {
            System.out.println("SCC # " + i + ":");

            LinkedListNode<GraphNode> tmp = sCCs[i].getHead();

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
        LinkedListNode<GraphNode> tmp;
        GraphNodeComparator nodeCompare = new GraphNodeComparator();
        ArrayList<TreeSet<GraphNode>> sCCsForPrinting;
        sCCsForPrinting = new ArrayList<TreeSet<GraphNode>>(); // Need each SCC
                                                               // sorted by
                                                               // index of node,
                                                               // To verify the
                                                               // output from
                                                               // both
                                                               // algorithms is
                                                               // the same
        for (int i = 0; i <= indexOfLastSCC; i++) {
            sCCsForPrinting.add(new TreeSet<GraphNode>(nodeCompare));
            tmp = sCCs[i].getHead();

            while (tmp != null) {
                sCCsForPrinting.get(sCCsForPrinting.size() - 1).add(
                        tmp.getData());
                tmp = tmp.getNext();
            }
        }
        // End code to convert array of LinkedList's into
        // ArrayList<TreeSet<GraphNode>>

        String s = "";
        for (int i = 0; i < sCCsForPrinting.size(); i++) {
            s += ("SCC # " + i + ":\n");

            for (GraphNode n : sCCsForPrinting.get(i)) {
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

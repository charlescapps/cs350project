package datastructures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {
    protected List<GraphNode> allNodes;

    public Graph() {
        allNodes = new ArrayList<GraphNode>();
    }

    public Graph(int size) { // For when we know the size in advance

        allNodes = new ArrayList<GraphNode>();

        for (int i = 0; i < size; i++) { // Initialize empty nodes with proper
            // indices
            allNodes.add(new GraphNode(i)); // New node with index = i recorded
            // inside node
        }
    }

    public Graph(BufferedReader br) throws Exception { // Read a graph from file
        // / string of proper
        // format
        allNodes = new ArrayList<GraphNode>();

        Pattern firstLine = Pattern.compile("Num Nodes:(\\d+)");

        Pattern validLine = Pattern.compile("Node (\\d+):(\\d+(?:,\\d+)*)\\s*");

        Matcher m;
        String line;
        int index;
        int totalNodes;

        String[] adjList;

        // Parse first line to get total # of nodes

        m = firstLine.matcher(br.readLine());
        if (m.find() == false)
            throw new Exception("FIRST LINE OF FILE IS INVALID, SHOULD BE OF FORM: 'Num Nodes:<some_number>'");

        totalNodes = Integer.parseInt(m.group(1));

        // Create all the nodes since we know how many there are

        for (int i = 0; i < totalNodes; i++)
            allNodes.add(new GraphNode(i));

        // Parse the rest:

        while ((line = br.readLine()) != null) {
            m = validLine.matcher(line);
            if (m.find() == false)
                throw new Exception("INVALID LINE ENCOUNTERED WHEN PARSING FiLE TO A GRAPH OBJECT");

            index = Integer.parseInt(m.group(1));

            adjList = m.group(2).split(",");

            for (int i = 0; i < adjList.length; i++) {
                // Add an edge for each node index listed in the file to the
                // adjacency list
                allNodes.get(index).addEdge(allNodes.get(Integer.parseInt(adjList[i])));
            }
        }
    }

    public Graph clone() {
        Graph theClone = new Graph();

        // Theta (|V|) to add all nodes to new graph
        for (GraphNode node : allNodes) {
            theClone.allNodes.add(node.clone());
        }

        for (GraphNode node : allNodes) {
            for (GraphNode adjacentNode : node.getAdjList()) {
                theClone.allNodes.get(node.getIndex()).addEdge(theClone.allNodes.get(adjacentNode.getIndex()));
            }
        }

        return theClone;
    }

    public void reInitGraph() { //Reset all nodes to UNVISITED
        for (GraphNode gn: allNodes) {
            gn.setColor(GraphNode.Color.UNVISITED);
        }
        
    }
    
    public BasicGraphNode[] toBasicGraph() {
        BasicGraphNode[] basicNodes = new BasicGraphNode[allNodes.size()];

        for (int i = 0; i < basicNodes.length; i++) {
            basicNodes[i] = new BasicGraphNode(i);
        }

        List<GraphNode> adjList = null;

        for (GraphNode n : allNodes) {
            adjList = n.getAdjList();
            for (int i = adjList.size() - 1; i >= 0; i--) { //Reverse order since LinkedList adds to front
                basicNodes[n.getIndex()].addEdge(basicNodes[adjList.get(i).getIndex()]);
            }
        }

        return basicNodes;

    }

    public List<GraphNode> getAllNodes() {
        return allNodes;
    }

    public Graph getTranspose() {
        Graph theClone = new Graph();

        // Theta (|V|) to add all nodes to new graph
        for (GraphNode node : allNodes) {
            theClone.allNodes.add(node.clone());
        }

        for (GraphNode node : allNodes) {
            for (GraphNode adjacentNode : node.getAdjList()) {
                theClone.allNodes.get(adjacentNode.getIndex()).addEdge(theClone.allNodes.get(node.getIndex()));
            }
        }

        return theClone;
    }

    public void printAdjacencyMatrix() {
        int[][] adjMatrix = new int[allNodes.size()][allNodes.size()];

        for (int i = 0; i < allNodes.size(); i++)
            for (int j = 0; j < allNodes.size(); j++)
                adjMatrix[i][j] = 0;

        for (GraphNode node : allNodes) {
            for (GraphNode adjNode : node.getAdjList()) {
                adjMatrix[node.getIndex()][adjNode.getIndex()] = 1;
            }
        }

        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = 0; j < allNodes.size(); j++)
                System.out.print(adjMatrix[i][j]);

            System.out.println();
        }
    }

    public void printAdjacencyMatrix(BufferedWriter bw) throws IOException {

        int[][] adjMatrix = new int[allNodes.size()][allNodes.size()];

        for (int i = 0; i < allNodes.size(); i++)
            for (int j = 0; j < allNodes.size(); j++)
                adjMatrix[i][j] = 0;

        for (GraphNode node : allNodes) {
            for (GraphNode adjNode : node.getAdjList()) {
                adjMatrix[node.getIndex()][adjNode.getIndex()] = 1;
            }
        }

        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = 0; j < allNodes.size(); j++)
                bw.write(adjMatrix[i][j] == 1 ? "1 " : "0 ");

            bw.newLine();
        }
    }

    public void printAllNodes() {
        System.out.println("All nodes in graph:");
        for (GraphNode n : allNodes) {
            System.out.println("\t" + n);
        }
    }

    public void write(BufferedWriter b) throws IOException { // Write to String
        // / file /whatever
        // in parseable
        // format
        boolean isFirst;
        b.write("Num Nodes:" + allNodes.size());
        b.newLine();
        for (GraphNode n : allNodes) {

            if (n.getAdjList().size() > 0) {
                b.write("Node " + n.getIndex() + ":");

                isFirst = true;

                for (GraphNode adj : n.getAdjList()) {
                    b.write((isFirst ? Integer.toString(adj.getIndex()) : "," + Integer.toString(adj.getIndex())));
                    isFirst = false;
                }

                b.newLine();
            }
        }

        b.close();
    }

    public int getNumVertices() {
        return allNodes.size();
    }

    public int getNumEdges() {
        int numEdges = 0;
        for (GraphNode n : allNodes) {
            numEdges += n.getAdjList().size(); // Add the number of edges in
            // each node's Adjacency list
        }

        return numEdges;
    }
}

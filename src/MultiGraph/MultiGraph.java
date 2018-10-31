package MultiGraph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MultiGraph implements IMultiGraph {

    private Set<Edge> edges; // the set of edges
    private Set<Node> nodes; // the set of nodes
    private Map<Node, Set<Edge>> nodeToEdges; // mapping of nodes to their edges

    MultiGraph () {
        edges = new HashSet<Edge>();
        nodes = new HashSet<Node>();
        nodeToEdges = new HashMap<Node, Set<Edge>>();
    }

    // requires: n != null
    // modifies: this
    //  effects: n in Nodes� && if n !in Nodes return true, else return false
    @Override
    public boolean addNode(Node n) {
        if (nodes.add(n)) { // n was not in the set already
            Set<Edge> nodeEdges = new HashSet<Edge>();
            nodeToEdges.put(n, nodeEdges);
            return true;
        }
        return false;
    }

    // requires: e != null && e.firstNode != null && e.secondNode != null
    // modifies: this
    //  effects: e in Edges� && e.firstNode in Nodes� && e.secondNode in Nodes� && if e !in Edges return true, else return false
    @Override
    public boolean addEdge(Edge e) {
        Set<Edge> nodeEdges=null;

        if (edges.add(e)) { // e was not in the set already

            // if either of the nodes are not in the set then add them!

            if (nodes.add(e.origin())) { // node was not in the set already
                nodeEdges = new HashSet<Edge>();
                nodeEdges.add(e);
                nodeToEdges.put(e.origin(), nodeEdges);
            } else {
                nodeEdges = nodeToEdges.get(e.origin());
                nodeEdges.add(e);
            }

            if (nodes.add(e.destination())) { // node was not in the set already
                nodeEdges = new HashSet<Edge>();
                nodeEdges.add(e);
                nodeToEdges.put(e.destination(), nodeEdges);
            } else {
                nodeEdges = nodeToEdges.get(e.destination());
                nodeEdges.add(e);
            }

            return true;
        }
        return false;
    }

    //  effects: if exists n in Nodes | n.id = id return n, else return null
    @Override
    public Node getNode(int id) {

        for (Node n : nodes) {
            if (n.getId() == id) // node found!
                return n;
        }
        return null;
    }

    //requires: start != null && destination != null
    // effects: if start !in Nodes || destination !in Nodes returns null
    // 			else if no path is found then return empty Deque
    //			else return P = {e1, �, eN | (e1.firstNode = start && eN.secondNode = destination) &&
    //			forAll i in {2, �, N-1}, ei.firstNode = e(i-1).secondNode && ei.secondNode = e(i+1).firstNode}
    @Override
    public Deque<Edge> findPath (Node start, Node destination) {

        if (!nodes.contains(start) || !nodes.contains(destination)) {
            return null;
        }

        Deque<Edge> path = new LinkedList<Edge>();

        if (start.equals(destination)) {
            return path;
        }

        // Maps for node and edge labels
        Map<Node, String> nodeLabels = new HashMap<Node, String>();
        Map<Edge, String> edgeLabels = new HashMap<Edge, String>();

        for (Node n: nodes) nodeLabels.put(n,  "Unexplored");
        for (Edge e: edges) edgeLabels.put(e,  "Unexplored");

        // Map to track which edge you reached a node from
        Map<Node, Edge> from = new HashMap<Node, Edge>();

        // start node visited
        nodeLabels.remove(start);
        nodeLabels.put(start, "Visited");

        // track visited nodes, whose edges have not been explored yet
        List<Node> visitedNodes = new ArrayList<Node>();
        visitedNodes.add(start);

        // while there are more nodes whose edge we have not explored
        while (!visitedNodes.isEmpty()) {

            // discovered nodes whose edge will be explored next
            List<Node>  tobeVisited = new ArrayList<Node>();

            for (Node n: visitedNodes) {
                for (Edge e: nodeToEdges.get(n)) { // for each edge of each visited node do

                    if (edgeLabels.get(e).equals("Unexplored")) {
                        Node opposite = e.origin().equals(n) ? e.destination() : e.origin();

                        if (nodeLabels.get(opposite).equals("Unexplored")) {
                            // found a new node

                            // current edge led to node discovery
                            edgeLabels.remove(e);
                            edgeLabels.put(e, "Discovery");

                            // node was reached from the current edge
                            from.put(opposite, e);

                            // newly discovered node edges are still to be explored
                            tobeVisited.add(opposite);

                            // new discovered node is now visited
                            nodeLabels.remove(opposite);
                            nodeLabels.put(opposite, "Visited");

                        } else { // node has already been visited by another edge

                            // try to stay on edge on same colour edge if possible
                            if (!n.equals(start)) {	// start has no previous node
                                String nLabel = from.get(n).getLabel();
                                String oppositeLabel = from.get(opposite).getLabel();

                                // if the current edge colour is different from the previous one
                                // or the current node was reached from an edge of the same colour as the previous one
                                // then the current edge is just a cross, i.e. ignore
                                if(nLabel.equals(oppositeLabel) || !nLabel.equals(e.getLabel())) {
                                    edgeLabels.remove(e);
                                    edgeLabels.put(e, "Cross");

                                } else { // we just found an edge that can keep us on the same colour

                                    // replace the previous edge with the current one
                                    Edge previous = from.get(opposite);
                                    edgeLabels.remove(previous);
                                    edgeLabels.put(previous, "Cross");
                                    from.remove(opposite);
                                    from.put(opposite, e);
                                    edgeLabels.remove(e);
                                    edgeLabels.put(e, "Discovery");
                                }

                            } else {
                                edgeLabels.remove(e);
                                edgeLabels.put(e, "Cross");
                            }
                        }
                        if (opposite.equals(destination)) { // we reached the destination!

                            Edge currentFrom = from.get(destination);
                            Node currentNode = destination.equals(currentFrom.origin()) ? currentFrom.destination() : currentFrom.origin();

                            // the last edge is added to the path
                            path.addFirst(currentFrom);

                            // follow the edges back from the destination to the start adding them to the path
                            while (!start.equals(currentNode)) {
                                currentFrom = from.get(currentNode);
                                path.addFirst(currentFrom);
                                currentNode = currentNode.equals(currentFrom.origin()) ? currentFrom.destination() : currentFrom.origin();
                            }

                            return path;
                        }
                    } // if edge has not been explored
                } // for all edges of the current node
            } // for all nodes whose edges yet to be explored

            // move the next level of node to be followed
            visitedNodes.clear();
            visitedNodes.addAll(tobeVisited);
        } // while there are more node whose edges yet to be explored

        return path;
    }
}
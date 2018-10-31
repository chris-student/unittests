package MultiGraph;

import java.util.Deque;


public interface IMultiGraph {

    public boolean addNode(Node n);

    public boolean addEdge(Edge e);

    public Node getNode(int id);

    public Deque<Edge> findPath (Node start, Node destination);
}
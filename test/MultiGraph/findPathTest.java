package MultiGraph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Chris Brown
 * @version 0.1
 * @since 30/10/2018
 */
class findPathTest {

    private IMultiGraph multigraph;
    private Node node0;
    private Node node1;
    private Edge edge0;

    @BeforeEach
    void setUp() {
        multigraph = new MultiGraph();
        // node0 and 1 are valid nodes within Nodes
        node0 = new Station(0);
        node1 = new Station(1);
        // edge0 is a valid edge between node0 and 1 within Edges
        edge0 = new Line("Line1", (Station)node0, (Station)node1);
        multigraph.addEdge(edge0);
    }

    @AfterEach
    void tearDown() {
        multigraph = null;
        node0 = node1 = null;
        edge0 = null;
    }


    /*
     * Testing strategy for findPath
     *
     * Input partitions:
     * start:       !in Nodes, in Nodes
     * destination: !in Nodes, in Nodes
     *
     * P = {} (start == destination), P = {} (no path between start & destination),
     * P = {e1}, P = {e1,..,eN}
     *
     * Also test whether findPath creates paths which stay on edges with the same label where possible.
     */

    /**
     * Covers start !in Nodes
     *        destination in Nodes
     */
    @Test
    void startNodeNotInNodes() {
        Station station2 = new Station(2);
        assertNull(multigraph.findPath(station2, node0));
    }

    /**
     * Covers start in Nodes
     *        destination in Nodes
     *        P = {}                (start == destination)
     */
    @Test
    void startEqualsDestination() {
        assertEquals(new LinkedList<Edge>(), multigraph.findPath(node0, node0));
    }

    /**
     * Covers start in Nodes
     *        destination in Nodes
     *        P = {e1}
     */
    @Test
    void singleEdgePath() {
        Deque<Edge> expectedPath = new LinkedList<Edge>();
        expectedPath.addFirst(edge0);
        assertEquals(expectedPath, multigraph.findPath(node0, node1));
    }

    /**
     * Covers start in Nodes
     *        destination in Nodes
     *        P = {}                (no path between start and destination
     */
    @Test
    void noPathBetweenStartAndDestination() {
        Station station2 = new Station(2);
        multigraph.addNode(station2);
        assertEquals(new LinkedList<Edge>(), multigraph.findPath(node0, station2));
    }

    /**
     * Covers start in Nodes
     *        destination in Nodes
     *        P = {e1,..,eN}
     */
    @Test
    void multipleEdgePath() {
        Station station2 = new Station(2);
        Edge edge1 = new Line("Line1", (Station)node1, station2);
        multigraph.addEdge(edge1);

        Deque<Edge> expectedPath = new LinkedList<Edge>();
        expectedPath.addFirst(edge1);
        expectedPath.addFirst(edge0);

        assertEquals(expectedPath, multigraph.findPath(node0, station2));
    }

    /**
     * Covers start in Nodes
     *        destination in Nodes
     *        P = {e1,..,eN}
     *        Path stays on edges with same label?
     */
    @Test
    void multipleEdgePathWithSameLabel() {
        Station station2 = new Station(2);
        Station station3 = new Station(3);
        Station station4 = new Station(4);
        Station station5 = new Station(5);

        Edge edge1 = new Line("Line1", (Station)node1, station2);
        Edge edge2 = new Line("Line1", station2, station3);
        Edge edge3 = new Line("Line2", station4, station3);
        Edge edge4 = new Line("Line2", (Station)node1, station4);
        Edge edge5 = new Line("Line2", station3, station5);

        multigraph.addEdge(edge1);
        multigraph.addEdge(edge2);
        multigraph.addEdge(edge3);
        multigraph.addEdge(edge4);
        multigraph.addEdge(edge5);


        Deque<Edge> expectedPath1 = new LinkedList<Edge>();
        expectedPath1.addFirst(edge0);
        expectedPath1.addFirst(edge4);
        expectedPath1.addFirst(edge3);
        expectedPath1.addFirst(edge5);

        Deque<Edge> expectedPath2 = new LinkedList<Edge>();
        expectedPath2.addFirst(edge0);
        expectedPath2.addFirst(edge4);
        expectedPath2.addFirst(edge3);

        Deque<Edge> expectedPath3 = new LinkedList<Edge>();
        expectedPath3.addFirst(edge5);
        expectedPath3.addFirst(edge2);
        expectedPath3.addFirst(edge1);
        expectedPath3.addFirst(edge0);

        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath1, multigraph.findPath(station5, node0)),
                () -> assertEquals(expectedPath2, multigraph.findPath(station3, node0)),
                () -> assertEquals(expectedPath3, multigraph.findPath(node0, station5))
        );
    }
}

package MultiGraph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

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
     * Test for outputs:
     * P = {} (start == destination), P = {} (no path between start & destination),
     * P = {e1}, P = {e1,..,eN}
     * null (start !in Nodes || destination !in Nodes)
     *
     * Also test whether findPath creates paths which stay on edges with the same label where possible.
     */

    /**
     * Covers start !in Nodes
     *        destination in Nodes
     *        null (start !in Nodes || destination !in Nodes)
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
        assertTrue(multigraph.findPath(node0, node0).isEmpty());
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
        assertTrue(multigraph.findPath(node0, station2).isEmpty());
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
        // See graph1.jpg

        // Builds on top of the already-existing multigraph with node0, node1, and edge0 between them.

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
        expectedPath3.addFirst(edge3);
        expectedPath3.addFirst(edge4);
        expectedPath3.addFirst(edge0);

        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath1, multigraph.findPath(station5, node0)),
                () -> assertEquals(expectedPath2, multigraph.findPath(station3, node0)),
                () -> assertEquals(expectedPath3, multigraph.findPath(node0, station5))
        );
    }

    @Test
    void multipleEdgePathOnTwoLinesWithOneIntersection() {
        // See graph2.jpg

        multigraph = new MultiGraph();

        Station station0 = new Station(0);
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Station station3 = new Station(3);
        Station station4 = new Station(4);

        Edge black0 = new Line("Black", station0, station1);
        Edge black1 = new Line("Black", station1, station3);
        Edge blue0 = new Line("Blue", station1, station2);
        Edge blue1 = new Line("Blue", station1, station4);

        multigraph.addEdge(black0);
        multigraph.addEdge(black1);
        multigraph.addEdge(blue0);
        multigraph.addEdge(blue1);

        // Expected path from station3 to station0
        Deque<Edge> expectedPath3to0 = new LinkedList<>();
        expectedPath3to0.add(black1);
        expectedPath3to0.add(black0);

        Deque<Edge> expectedPath0to3 = new LinkedList<>();
        expectedPath0to3.add(black0);
        expectedPath0to3.add(black1);

        // Expected path from station4 to station2
        Deque<Edge> expectedPath4to2 = new LinkedList<>();
        expectedPath4to2.add(blue1);
        expectedPath4to2.add(blue0);

        Deque<Edge> expectedPath2to4 = new LinkedList<>();
        expectedPath2to4.add(blue0);
        expectedPath2to4.add(blue1);

        // Expected path from station0 to station2 (should cause a line change)
        Deque<Edge> expectedPath0to2 = new LinkedList<>();
        expectedPath0to2.add(black0);
        expectedPath0to2.add(blue0);

        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath3to0, multigraph.findPath(station3, station0)),
                () -> assertEquals(expectedPath0to3, multigraph.findPath(station0, station3)),
                () -> assertEquals(expectedPath4to2, multigraph.findPath(station4, station2)),
                () -> assertEquals(expectedPath2to4, multigraph.findPath(station2, station4)),
                () -> assertEquals(expectedPath0to2, multigraph.findPath(station0, station2))
        );
    }

    @Test
    void multipleEdgePathOnTwoLinesWithTwoIntersectionsAndOneParallelEdge() {
        // See graph3.jpg

        multigraph = new MultiGraph();

        Station station0 = new Station(0);
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Station station3 = new Station(3);
        Station station4 = new Station(4);
        Station station5 = new Station(5);

        Edge blue0 = new Line("Blue", station0, station1);
        Edge blue1 = new Line("Blue", station1, station2);
        Edge blue2 = new Line("Blue", station2, station3);
        Edge red0 = new Line("Red", station4, station1);
        Edge red1 = new Line("Red", station1, station2);
        Edge red2 = new Line("Red", station2, station5);

        multigraph.addEdge(blue0);
        multigraph.addEdge(blue1);
        multigraph.addEdge(blue2);
        multigraph.addEdge(red0);
        multigraph.addEdge(red1);
        multigraph.addEdge(red2);

        // Expected path from station4 to station5
        Deque<Edge> expectedPath4to5 = new LinkedList<>();
        expectedPath4to5.add(red0);
        expectedPath4to5.add(red1);
        expectedPath4to5.add(red2);

        // Expected path from station0 to station3
        Deque<Edge> expectedPath0to3 = new LinkedList<>();
        expectedPath0to3.add(blue0);
        expectedPath0to3.add(blue1);
        expectedPath0to3.add(blue2);


        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath4to5, multigraph.findPath(station4, station5)),
                () -> assertEquals(expectedPath0to3, multigraph.findPath(station0, station3))
        );
    }

    @Test
    void pathsOnTwoSubgraphsWithOneEdgeEach() {
        // 2 unconnected edges

        multigraph = new MultiGraph();

        Station station0 = new Station(0);
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Station station5 = new Station(5);

        Edge blue0 = new Line("Blue", station0, station1);

        Edge red2 = new Line("Red", station2, station5);

        multigraph.addEdge(blue0);

        multigraph.addEdge(red2);

        // Expected path from station2 to station5
        Deque<Edge> expectedPath2to5 = new LinkedList<>();
        expectedPath2to5.add(red2);

        // Expected path from station0 to station1
        Deque<Edge> expectedPath0to1 = new LinkedList<>();
        expectedPath0to1.add(blue0);


        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath2to5, multigraph.findPath(station2, station5)),
                () -> assertEquals(expectedPath0to1, multigraph.findPath(station0, station1)),
                () -> assertTrue(multigraph.findPath(station0, station2).isEmpty()),
                () -> assertTrue(multigraph.findPath(station1, station5).isEmpty())
        );
    }

    /**
     * This test uses graph3.jpg with some extra edges added and also duplicates some ID numbers. Provides almost full
     * coverage on the findPath method (except for {} paths).
     */
    @Test
    void multipleEdgePathOnGraphWithDuplicateNodeIds() {
        // See graph3.jpg

        multigraph = new MultiGraph();

        Station station0 = new Station(0);
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Station station3 = new Station(3);
        Station station4 = new Station(4);
        Station station5 = new Station(5);
        Station station6 = new Station(6);
        Station station7 = new Station(7);
        Station station8 = new Station(0);
        Station station9 = new Station(1);

        Edge blue0 = new Line("Blue", station0, station1);
        Edge blue1 = new Line("Blue", station1, station2);
        Edge blue2 = new Line("Blue", station2, station3);
        Edge red0 = new Line("Red", station4, station1);
        Edge red1 = new Line("Red", station1, station2);
        Edge red2 = new Line("Red", station2, station5);
        Edge extra0 = new Line("Extra", station6, station3);
        Edge extra1 = new Line("Extra", station3, station7);
        Edge misc = new Line("Misc", station8, station9);

        multigraph.addEdge(blue0);
        multigraph.addEdge(blue1);
        multigraph.addEdge(blue2);
        multigraph.addEdge(red0);
        multigraph.addEdge(red1);
        multigraph.addEdge(red2);
        multigraph.addEdge(extra0);
        multigraph.addEdge(extra1);
        multigraph.addEdge(misc);

        // Expected path from station4 to station5
        Deque<Edge> expectedPath4to5 = new LinkedList<>();
        expectedPath4to5.add(red0);
        expectedPath4to5.add(red1);
        expectedPath4to5.add(red2);

        Deque<Edge> expectedPath5to4 = new LinkedList<>();
        expectedPath5to4.add(red2);
        expectedPath5to4.add(red1);
        expectedPath5to4.add(red0);

        // Expected path from station0 to station3
        Deque<Edge> expectedPath0to3 = new LinkedList<>();
        expectedPath0to3.add(blue0);
        expectedPath0to3.add(blue1);
        expectedPath0to3.add(blue2);

        // Appears to be an issue here, see below test: 'pathBetweenTwoNodesWithSameIdOnUnconnectedSubGraphs' for detail
        Deque<Edge> expectedEmptyPath = multigraph.findPath(station1, station8);
        System.out.println(expectedEmptyPath.toString());

        assertAll("Test various start/destination pairs and compare with expected path",
                () -> assertEquals(expectedPath4to5, multigraph.findPath(station4, station5)),
                () -> assertEquals(expectedPath5to4, multigraph.findPath(station5, station4)),
                () -> assertEquals(expectedPath0to3, multigraph.findPath(station0, station3)),
                () -> assertTrue(expectedEmptyPath.isEmpty())
        );
    }

    /**
     * This test demonstrates an issue whens searching for a path between 2 nodes on unconnected subgraphs where both
     * subgraphs contain a node with the same ID #. Even though the staring node is specified with an object reference
     * the findPath method appears to use a different node (that has the same ID #).
     *
     * This seems to relate to findPath's use of equality checks from the Station implementation. Station overrides
     * equals and hashCode and uses station ID # to compare. If we comment out those overridden methods and use default
     * equality the problem described above doesn't occur.
     */
    @Test
    void pathBetweenTwoNodesWithSameIdOnUnconnectedSubGraphs() {
        // This is the red line from graph3.jpg with an extra edge between 2 nodes which are unconnected to the rest of
        // the graph. One of the unconnected nodes has the same id number as one of the nodes on the red line segment.

        multigraph = new MultiGraph();

        Station station1 = new Station(0);
        Station station2 = new Station(2);
        Station station4 = new Station(4);
        Station station5 = new Station(5);

        // Separate nodes
        Station station6 = new Station(0);
        Station station7 = new Station(1);

        Edge red0 = new Line("Red", station4, station1);
        Edge red1 = new Line("Red", station1, station2);
        Edge red2 = new Line("Red", station2, station5);

        // Separate edge
        Edge unconnected = new Line("Unconnected", station6, station7);

        multigraph.addEdge(red0);
        multigraph.addEdge(red1);
        multigraph.addEdge(red2);

        multigraph.addEdge(unconnected);

        // There should be no path between nodes on the red line and unconnected segments.
        Deque<Edge> path = multigraph.findPath(station1, station7);
        assertTrue(path.isEmpty(), path.toString());
    }
}

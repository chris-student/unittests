package MultiGraph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Chris Brown
 * @version 0.1
 * @since 30/10/2018
 */
class addEdgeTest {

    private IMultiGraph multigraph;
    private Station station0;
    private Station station1;
    private Edge edge1;

    @BeforeEach
    void setUp() {
        multigraph = new MultiGraph();
        // station0 and 1 are valid nodes within Nodes
        station0 = new Station(0);
        station1 = new Station(1);
        multigraph.addNode(station0);
        multigraph.addNode(station1);
    }

    @AfterEach
    void tearDown() {
        multigraph = null;
        station0 = station1 = null;
        edge1 = null;
    }


    /*
     * Testing strategy for addEdge
     *
     * Input partitions:
     * e:            !in Edges, in Edges
     * e.firstNode:  in Nodes, !in Nodes
     * e.secondNode: in Nodes, !in Nodes
     *
     */

    /**
     * Covers e !in Edges
     *        e.firstNode in Nodes
     *        e.secondNode in Nodes
     */
    @Test
    void addNewEdgeWithOriginDestinationInNodes() {
        edge1 = new Line("Line1", station0, station1);
        assertTrue(multigraph.addEdge(edge1));
    }

    /**
     * Covers e in Edges
     *        e.firstNode in Nodes
     *        e.secondNode in Nodes
     */
    @Test
    void addEdgeAlreadyInEdgesWithOriginDestinationInNodes() {
        edge1 = new Line("Line1", station0, station1);
        multigraph.addEdge(edge1);
        assertFalse(multigraph.addEdge(edge1));
    }

    /**
     * Covers e !in Edges
     *        e.firstNode !in Nodes
     *        e.secondNode !in Nodes
     */
    @Test
    void addNewEdgeWithOriginDestinationNotInNodes() {
        // station2 and 3 are valid nodes !in Nodes
        Station station2 = new Station(2);
        Station station3 = new Station(3);
        edge1 = new Line("Line1", station2, station3);
        boolean result = multigraph.addEdge(edge1);
        assertAll("Should add e to Edges (untestable) and e.firstNode, e.secondNode to Nodes. Returns true.",
                () -> Assertions.assertEquals(station2, multigraph.getNode(2)),
                () -> Assertions.assertEquals(station3, multigraph.getNode(3)),
                () -> Assertions.assertTrue(result));
    }

    /*
        Should also test the following for full cartesian product?

        e: !in Edges,    e.firstNode:  in Nodes,   e.secondNode: !in Nodes
            adds e.secondNode to Nodes and returns true
        e: !in Edges,    e.firstNode:  !in Nodes   e.secondNode: in Nodes
            adds e.firstNode to Nodes and returns true

        Can't test e in Edges alongside e.firstNode/secondNode !in Nodes as that is an invalid scenario since e
        couldn't possibly be in Edges if 1 or both of firstNode/secondNode weren't in Nodes (3 cases):

        e: in Edges    e.firstNode:  !in Nodes   e.secondNode: !in Nodes
        e: in Edges    e.firstNode:  in Nodes,   e.secondNode: !in Nodes
        e: in Edges    e.firstNode:  !in Nodes   e.secondNode: in Nodes

        TEMPLATE:
        e: !in Edges, in Edges    e.firstNode:  in Nodes, !in Nodes   e.secondNode: in Nodes, !in Nodes
     */
}

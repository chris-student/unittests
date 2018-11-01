package MultiGraph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class addNodeTest {

    private IMultiGraph multigraph;
    private Node node1;
    boolean result;

    @BeforeEach
    void setUp() {
        multigraph = new MultiGraph();
    }

    @AfterEach
    void tearDown() {
        multigraph = null;
        node1 = null;
    }


    /*
     * Testing strategy for addNode
     *
     * Input partitions:
     * n:   !in Nodes, in Nodes
     *
     */

    /**
     * Covers n !in Nodes
     */
    @Test
    void addNewNode() {
        node1 = new Station(0);
        result = multigraph.addNode(node1);
        assertAll("Should add n to Nodes and return true",
                () -> Assertions.assertEquals(node1, multigraph.getNode(0)),
                () -> assertTrue(result));
    }

    /**
     * Covers n !in Nodes using Station's overloaded constructor. This is probably unnecessary and more suited to
     * testing the Station implementation.
     */
    @Test
    void addNewNodeWithIdAndName() {
        node1 = new Station(0, "Station 0");
        result = multigraph.addNode(node1);
        assertAll("Should add n to Nodes and return true",
                () -> Assertions.assertEquals(node1, multigraph.getNode(0)),
                () -> assertTrue(result));
    }

    /**
     * Covers n in Nodes
     */
    @Test
    void addNodeAlreadyInNodes() {
        node1 = new Station(0, "Station 0");
        multigraph.addNode(node1);
        result = multigraph.addNode(node1);
        assertAll("n should already be in nodes so return false when adding",
                () -> Assertions.assertEquals(node1, multigraph.getNode(0)),
                () -> Assertions.assertFalse(result));
    }
}
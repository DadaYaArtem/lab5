package com.grapheditor.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {

    private Graph graph;
    private Node node1, node2, node3;

    @Before
    public void setUp() {
        graph = new Graph("Test Graph");
        node1 = new Node(0, 0);
        node2 = new Node(100, 100);
        node3 = new Node(200, 200);
        node1.setName("A");
        node2.setName("B");
        node3.setName("C");
    }

    @Test
    public void testGraphCreation() {
        assertNotNull(graph);
        assertEquals("Test Graph", graph.getName());
        assertTrue(graph.getNodes().isEmpty());
        assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    public void testAddNode() {
        graph.addNode(node1);
        assertEquals(1, graph.getNodes().size());
        assertTrue(graph.getNodes().contains(node1));
    }

    @Test
    public void testAddMultipleNodes() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        assertEquals(3, graph.getNodes().size());
    }

    @Test
    public void testRemoveNode() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.removeNode(node1);
        assertEquals(1, graph.getNodes().size());
        assertFalse(graph.getNodes().contains(node1));
    }

    @Test
    public void testRemoveNodeWithEdges() {
        graph.addNode(node1);
        graph.addNode(node2);
        Edge edge = new Edge(node1, node2, false);
        graph.addEdge(edge);

        assertEquals(1, graph.getEdges().size());
        graph.removeNode(node1);
        assertEquals(0, graph.getEdges().size());
        assertEquals(1, graph.getNodes().size());
    }

    @Test
    public void testAddEdge() {
        graph.addNode(node1);
        graph.addNode(node2);
        Edge edge = new Edge(node1, node2, false);
        graph.addEdge(edge);

        assertEquals(1, graph.getEdges().size());
        assertTrue(graph.getEdges().contains(edge));
    }

    @Test
    public void testRemoveEdge() {
        graph.addNode(node1);
        graph.addNode(node2);
        Edge edge = new Edge(node1, node2, false);
        graph.addEdge(edge);
        graph.removeEdge(edge);

        assertEquals(0, graph.getEdges().size());
    }

    @Test
    public void testGetNodeAt() {
        graph.addNode(node1);
        Node found = graph.getNodeAt(0, 0);
        assertEquals(node1, found);
    }

    @Test
    public void testGetNodeAtMiss() {
        graph.addNode(node1);
        Node found = graph.getNodeAt(1000, 1000);
        assertNull(found);
    }

    @Test
    public void testGetDegreeUndirected() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addEdge(new Edge(node1, node2, false));
        graph.addEdge(new Edge(node1, node3, false));

        assertEquals(2, graph.getDegree(node1));
        assertEquals(1, graph.getDegree(node2));
        assertEquals(1, graph.getDegree(node3));
    }

    @Test
    public void testGetDegreeDirected() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(new Edge(node1, node2, true));

        assertEquals(1, graph.getDegree(node1));
        assertEquals(1, graph.getDegree(node2));
    }

    @Test
    public void testInOutDegree() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(new Edge(node1, node2, true));

        assertEquals(1, graph.getOutDegree(node1));
        assertEquals(0, graph.getInDegree(node1));
        assertEquals(0, graph.getOutDegree(node2));
        assertEquals(1, graph.getInDegree(node2));
    }

    @Test
    public void testSetGraphName() {
        graph.setName("New Name");
        assertEquals("New Name", graph.getName());
    }

    @Test
    public void testEmptyGraphDegree() {
        graph.addNode(node1);
        assertEquals(0, graph.getDegree(node1));
        assertEquals(0, graph.getInDegree(node1));
        assertEquals(0, graph.getOutDegree(node1));
    }

    @Test
    public void testSelfLoop() {
        graph.addNode(node1);
        graph.addEdge(new Edge(node1, node1, false));
        assertEquals(2, graph.getDegree(node1));
    }
}

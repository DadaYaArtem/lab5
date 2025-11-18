package com.grapheditor.model;

import org.junit.Test;
import java.awt.Color;
import static org.junit.Assert.*;

public class EdgeTest {

    @Test
    public void testUndirectedEdge() {
        Node from = new Node(0, 0);
        Node to = new Node(100, 100);
        Edge edge = new Edge(from, to, false);

        assertFalse(edge.isDirected());
        assertEquals(from, edge.getFrom());
        assertEquals(to, edge.getTo());
        assertEquals(Color.BLACK, edge.getColor());
    }

    @Test
    public void testDirectedEdge() {
        Node from = new Node(0, 0);
        Node to = new Node(100, 100);
        Edge edge = new Edge(from, to, true);

        assertTrue(edge.isDirected());
        assertEquals(from, edge.getFrom());
        assertEquals(to, edge.getTo());
    }

    @Test
    public void testSetDirected() {
        Node from = new Node(0, 0);
        Node to = new Node(100, 100);
        Edge edge = new Edge(from, to, false);

        edge.setDirected(true);
        assertTrue(edge.isDirected());

        edge.setDirected(false);
        assertFalse(edge.isDirected());
    }

    @Test
    public void testSetColor() {
        Node from = new Node(0, 0);
        Node to = new Node(100, 100);
        Edge edge = new Edge(from, to, false);

        edge.setColor(Color.RED);
        assertEquals(Color.RED, edge.getColor());
    }

    @Test
    public void testSelfLoop() {
        Node node = new Node(0, 0);
        Edge edge = new Edge(node, node, false);

        assertEquals(node, edge.getFrom());
        assertEquals(node, edge.getTo());
    }
}

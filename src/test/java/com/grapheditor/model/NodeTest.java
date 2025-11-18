package com.grapheditor.model;

import org.junit.Test;
import java.awt.Color;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Node(100, 200);
        assertNotNull(node);
        assertEquals(100, node.getX());
        assertEquals(200, node.getY());
        assertNotNull(node.getName());
        assertEquals(Color.BLUE, node.getColor());
    }

    @Test
    public void testNodeSetters() {
        Node node = new Node(0, 0);
        node.setName("TestNode");
        node.setX(50);
        node.setY(75);
        node.setColor(Color.RED);

        assertEquals("TestNode", node.getName());
        assertEquals(50, node.getX());
        assertEquals(75, node.getY());
        assertEquals(Color.RED, node.getColor());
    }

    @Test
    public void testNodeContains() {
        Node node = new Node(100, 100);
        assertTrue(node.contains(100, 100));
        assertTrue(node.contains(110, 110));
        assertFalse(node.contains(200, 200));
    }

    @Test
    public void testNodeId() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(0, 0);
        assertNotEquals(node1.getId(), node2.getId());
    }

    @Test
    public void testNodeRadius() {
        Node node = new Node(0, 0);
        assertEquals(25, node.getRadius());
    }

    @Test
    public void testNodeContainsBoundary() {
        Node node = new Node(100, 100);
        int radius = node.getRadius();
        assertTrue(node.contains(100 + radius, 100));
        assertTrue(node.contains(100 - radius, 100));
        assertTrue(node.contains(100, 100 + radius));
        assertTrue(node.contains(100, 100 - radius));
    }
}

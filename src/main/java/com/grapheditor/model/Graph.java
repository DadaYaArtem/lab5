package com.grapheditor.model;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Node> getNodes() { return nodes; }
    public List<Edge> getEdges() { return edges; }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        edges.removeIf(e -> e.getFrom() == node || e.getTo() == node);
        nodes.remove(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public Node getNodeAt(int x, int y) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public int getDegree(Node node) {
        int degree = 0;
        for (Edge edge : edges) {
            if (edge.getFrom() == node) degree++;
            if (edge.getTo() == node) degree++;
            if (!edge.isDirected() && edge.getFrom() == node && edge.getTo() == node) {
                degree--; // Self-loop counted twice, need to subtract once
            }
        }
        return degree;
    }

    public int getInDegree(Node node) {
        int degree = 0;
        for (Edge edge : edges) {
            if (edge.getTo() == node) degree++;
        }
        return degree;
    }

    public int getOutDegree(Node node) {
        int degree = 0;
        for (Edge edge : edges) {
            if (edge.getFrom() == node) degree++;
        }
        return degree;
    }
}

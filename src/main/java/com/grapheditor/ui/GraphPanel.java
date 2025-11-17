package com.grapheditor.ui;

import com.grapheditor.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private Graph graph;
    private Node selectedNode;
    private Node edgeStartNode;
    private String mode = "SELECT"; // SELECT, ADD_NODE, ADD_EDGE, DELETE
    private boolean directed = false;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int dragOffsetX, dragOffsetY;

            @Override
            public void mousePressed(MouseEvent e) {
                if (mode.equals("ADD_NODE")) {
                    Node node = new Node(e.getX(), e.getY());
                    graph.addNode(node);
                    repaint();
                } else if (mode.equals("SELECT") || mode.equals("ADD_EDGE")) {
                    selectedNode = graph.getNodeAt(e.getX(), e.getY());
                    if (selectedNode != null && mode.equals("SELECT")) {
                        dragOffsetX = e.getX() - selectedNode.getX();
                        dragOffsetY = e.getY() - selectedNode.getY();
                    } else if (selectedNode != null && mode.equals("ADD_EDGE")) {
                        if (edgeStartNode == null) {
                            edgeStartNode = selectedNode;
                        } else {
                            graph.addEdge(new Edge(edgeStartNode, selectedNode, directed));
                            edgeStartNode = null;
                            repaint();
                        }
                    }
                } else if (mode.equals("DELETE")) {
                    Node node = graph.getNodeAt(e.getX(), e.getY());
                    if (node != null) {
                        graph.removeNode(node);
                        repaint();
                    } else {
                        // Try to delete edge
                        Edge edgeToDelete = findEdgeAt(e.getX(), e.getY());
                        if (edgeToDelete != null) {
                            graph.removeEdge(edgeToDelete);
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mode.equals("SELECT") && selectedNode != null) {
                    selectedNode.setX(e.getX() - dragOffsetX);
                    selectedNode.setY(e.getY() - dragOffsetY);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedNode = null;
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private Edge findEdgeAt(int x, int y) {
        for (Edge edge : graph.getEdges()) {
            if (distanceToLine(x, y, edge) < 5) {
                return edge;
            }
        }
        return null;
    }

    private double distanceToLine(int x, int y, Edge edge) {
        int x1 = edge.getFrom().getX();
        int y1 = edge.getFrom().getY();
        int x2 = edge.getTo().getX();
        int y2 = edge.getTo().getY();

        double lineLength = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if (lineLength == 0) return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

        double t = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / (lineLength * lineLength);
        t = Math.max(0, Math.min(1, t));

        double projX = x1 + t * (x2 - x1);
        double projY = y1 + t * (y2 - y1);

        return Math.sqrt((x - projX) * (x - projX) + (y - projY) * (y - projY));
    }

    public void setMode(String mode) {
        this.mode = mode;
        edgeStartNode = null;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем ребра
        for (Edge edge : graph.getEdges()) {
            edge.draw(g2d);
        }

        // Рисуем узлы
        for (Node node : graph.getNodes()) {
            node.draw(g2d);
        }

        // Подсвечиваем начальный узел при добавлении ребра
        if (edgeStartNode != null) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            int r = edgeStartNode.getRadius();
            g2d.drawOval(edgeStartNode.getX() - r, edgeStartNode.getY() - r, r * 2, r * 2);
        }
    }
}

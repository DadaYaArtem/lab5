package com.grapheditor.model;

import java.awt.*;
import java.io.Serializable;

public class Edge implements Serializable {
    private static final long serialVersionUID = 1L;

    private Node from;
    private Node to;
    private boolean directed;
    private Color color;

    public Edge(Node from, Node to, boolean directed) {
        this.from = from;
        this.to = to;
        this.directed = directed;
        this.color = Color.BLACK;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public boolean isDirected() { return directed; }
    public void setDirected(boolean directed) { this.directed = directed; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));

        int x1 = from.getX();
        int y1 = from.getY();
        int x2 = to.getX();
        int y2 = to.getY();

        g2d.drawLine(x1, y1, x2, y2);

        if (directed) {
            drawArrow(g2d, x1, y1, x2, y2);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 10;

        // Adjust endpoint to node boundary
        double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double ratio = (dist - to.getRadius()) / dist;
        int endX = (int) (x1 + (x2 - x1) * ratio);
        int endY = (int) (y1 + (y2 - y1) * ratio);

        int x3 = (int) (endX - arrowSize * Math.cos(angle - Math.PI / 6));
        int y3 = (int) (endY - arrowSize * Math.sin(angle - Math.PI / 6));
        int x4 = (int) (endX - arrowSize * Math.cos(angle + Math.PI / 6));
        int y4 = (int) (endY - arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(new int[]{endX, x3, x4}, new int[]{endY, y3, y4}, 3);
    }
}

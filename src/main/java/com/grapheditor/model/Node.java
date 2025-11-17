package com.grapheditor.model;

import java.awt.*;
import java.io.Serializable;

public class Node implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String name;
    private int x, y;
    private Color color;
    private int radius = 25;

    public Node(int x, int y) {
        this.id = nextId++;
        this.name = "V" + id;
        this.x = x;
        this.y = y;
        this.color = Color.BLUE;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public int getRadius() { return radius; }

    public boolean contains(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int textHeight = fm.getAscent();
        g2d.drawString(name, x - textWidth / 2, y + textHeight / 3);
    }
}

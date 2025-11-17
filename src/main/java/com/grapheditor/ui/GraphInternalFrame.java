package com.grapheditor.ui;

import com.grapheditor.model.*;
import com.grapheditor.algorithms.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class GraphInternalFrame extends JInternalFrame {
    private Graph graph;
    private GraphPanel graphPanel;

    public GraphInternalFrame(String title) {
        super(title, true, true, true, true);
        this.graph = new Graph(title);
        initUI();
    }

    public GraphInternalFrame(Graph graph) {
        super(graph.getName(), true, true, true, true);
        this.graph = graph;
        initUI();
    }

    private void initUI() {
        setSize(600, 500);
        setLocation(30, 30);

        graphPanel = new GraphPanel(graph);
        add(new JScrollPane(graphPanel), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        JButton selectBtn = new JButton("Выбор");
        selectBtn.addActionListener(e -> graphPanel.setMode("SELECT"));
        toolBar.add(selectBtn);

        JButton addNodeBtn = new JButton("+ Узел");
        addNodeBtn.addActionListener(e -> graphPanel.setMode("ADD_NODE"));
        toolBar.add(addNodeBtn);

        JButton addEdgeBtn = new JButton("+ Ребро");
        addEdgeBtn.addActionListener(e -> {
            graphPanel.setMode("ADD_EDGE");
            graphPanel.setDirected(false);
        });
        toolBar.add(addEdgeBtn);

        JButton addDirectedEdgeBtn = new JButton("+ Дуга");
        addDirectedEdgeBtn.addActionListener(e -> {
            graphPanel.setMode("ADD_EDGE");
            graphPanel.setDirected(true);
        });
        toolBar.add(addDirectedEdgeBtn);

        JButton deleteBtn = new JButton("Удалить");
        deleteBtn.addActionListener(e -> graphPanel.setMode("DELETE"));
        toolBar.add(deleteBtn);

        toolBar.addSeparator();

        JButton renameNodeBtn = new JButton("Переименовать");
        renameNodeBtn.addActionListener(e -> renameNode());
        toolBar.add(renameNodeBtn);

        JButton colorNodeBtn = new JButton("Цвет узла");
        colorNodeBtn.addActionListener(e -> changeNodeColor());
        toolBar.add(colorNodeBtn);

        JButton colorEdgeBtn = new JButton("Цвет ребра");
        colorEdgeBtn.addActionListener(e -> changeEdgeColor());
        toolBar.add(colorEdgeBtn);

        add(toolBar, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();

        JMenu graphMenu = new JMenu("Граф");

        JMenuItem renameGraphItem = new JMenuItem("Переименовать граф");
        renameGraphItem.addActionListener(e -> renameGraph());
        graphMenu.add(renameGraphItem);

        JMenuItem saveItem = new JMenuItem("Сохранить");
        saveItem.addActionListener(e -> saveGraph());
        graphMenu.add(saveItem);

        JMenuItem infoItem = new JMenuItem("Информация");
        infoItem.addActionListener(e -> showGraphInfo());
        graphMenu.add(infoItem);

        menuBar.add(graphMenu);

        JMenu algoMenu = new JMenu("Алгоритмы");

        JMenuItem isTreeItem = new JMenuItem("Проверить дерево");
        isTreeItem.addActionListener(e -> checkIsTree());
        algoMenu.add(isTreeItem);

        JMenuItem hamiltonItem = new JMenuItem("Гамильтонов цикл");
        hamiltonItem.addActionListener(e -> findHamiltonianCycle());
        algoMenu.add(hamiltonItem);

        JMenuItem diameterItem = new JMenuItem("Диаметр, радиус, центр");
        diameterItem.addActionListener(e -> showDiameterRadiusCenter());
        algoMenu.add(diameterItem);

        menuBar.add(algoMenu);

        setJMenuBar(menuBar);
    }

    private void renameNode() {
        String nodeName = JOptionPane.showInputDialog(this, "Введите имя узла для переименования:");
        if (nodeName == null) return;

        Node node = findNodeByName(nodeName);
        if (node == null) {
            JOptionPane.showMessageDialog(this, "Узел не найден");
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "Новое имя:", node.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            node.setName(newName.trim());
            graphPanel.repaint();
        }
    }

    private void changeNodeColor() {
        String nodeName = JOptionPane.showInputDialog(this, "Введите имя узла:");
        if (nodeName == null) return;

        Node node = findNodeByName(nodeName);
        if (node == null) {
            JOptionPane.showMessageDialog(this, "Узел не найден");
            return;
        }

        Color color = JColorChooser.showDialog(this, "Выберите цвет узла", node.getColor());
        if (color != null) {
            node.setColor(color);
            graphPanel.repaint();
        }
    }

    private void changeEdgeColor() {
        if (graph.getEdges().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Нет рёбер в графе");
            return;
        }

        Edge edge = graph.getEdges().get(0); // Для простоты берем первое ребро
        Color color = JColorChooser.showDialog(this, "Выберите цвет ребра", edge.getColor());
        if (color != null) {
            for (Edge e : graph.getEdges()) {
                e.setColor(color);
            }
            graphPanel.repaint();
        }
    }

    private Node findNodeByName(String name) {
        for (Node node : graph.getNodes()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    private void renameGraph() {
        String newName = JOptionPane.showInputDialog(this, "Новое имя графа:", graph.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            graph.setName(newName.trim());
            setTitle(newName.trim());
        }
    }

    private void saveGraph() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(fc.getSelectedFile()))) {
                oos.writeObject(graph);
                JOptionPane.showMessageDialog(this, "Граф сохранён");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private void showGraphInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Граф: ").append(graph.getName()).append("\n");
        info.append("Вершин: ").append(graph.getNodes().size()).append("\n");
        info.append("Рёбер: ").append(graph.getEdges().size()).append("\n\n");
        info.append("Степени вершин:\n");

        for (Node node : graph.getNodes()) {
            info.append(node.getName()).append(": ").append(graph.getDegree(node));
            info.append(" (in: ").append(graph.getInDegree(node));
            info.append(", out: ").append(graph.getOutDegree(node)).append(")\n");
        }

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
            "Информация о графе", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkIsTree() {
        boolean isTree = GraphAlgorithms.isTree(graph);
        JOptionPane.showMessageDialog(this,
            isTree ? "Граф является деревом" : "Граф не является деревом");
    }

    private void findHamiltonianCycle() {
        List<Node> cycle = GraphAlgorithms.findHamiltonianCycle(graph);
        if (cycle == null) {
            JOptionPane.showMessageDialog(this, "Гамильтонов цикл не найден");
        } else {
            StringBuilder sb = new StringBuilder("Гамильтонов цикл: ");
            for (Node node : cycle) {
                sb.append(node.getName()).append(" -> ");
            }
            sb.append(cycle.get(0).getName());
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    private void showDiameterRadiusCenter() {
        if (graph.getNodes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Граф пуст");
            return;
        }

        int diameter = GraphAlgorithms.getDiameter(graph);
        int radius = GraphAlgorithms.getRadius(graph);
        List<Node> center = GraphAlgorithms.getCenter(graph);

        StringBuilder sb = new StringBuilder();
        sb.append("Диаметр: ").append(diameter).append("\n");
        sb.append("Радиус: ").append(radius).append("\n");
        sb.append("Центр: ");
        for (Node node : center) {
            sb.append(node.getName()).append(" ");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    public Graph getGraph() {
        return graph;
    }
}

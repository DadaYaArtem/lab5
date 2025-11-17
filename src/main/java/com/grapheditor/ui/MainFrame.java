package com.grapheditor.ui;

import com.grapheditor.model.*;
import com.grapheditor.algorithms.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainFrame extends JFrame {
    private JDesktopPane desktopPane;
    private int graphCounter = 1;

    public MainFrame() {
        setTitle("Графовый редактор - Лаб 5");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        add(desktopPane);

        createMenuBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Меню Файл
        JMenu fileMenu = new JMenu("Файл");

        JMenuItem newGraphItem = new JMenuItem("Новый граф");
        newGraphItem.addActionListener(e -> createNewGraph());
        fileMenu.add(newGraphItem);

        JMenuItem openItem = new JMenuItem("Открыть");
        openItem.addActionListener(e -> openGraph());
        fileMenu.add(openItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // Меню Операции
        JMenu operationsMenu = new JMenu("Операции");

        JMenuItem cartesianProductItem = new JMenuItem("Декартово произведение");
        cartesianProductItem.addActionListener(e -> performCartesianProduct());
        operationsMenu.add(cartesianProductItem);

        JMenuItem tensorProductItem = new JMenuItem("Тензорное произведение");
        tensorProductItem.addActionListener(e -> performTensorProduct());
        operationsMenu.add(tensorProductItem);

        JMenuItem convertToTreeItem = new JMenuItem("Преобразовать в дерево");
        convertToTreeItem.addActionListener(e -> convertToTree());
        operationsMenu.add(convertToTreeItem);

        JMenuItem convertToBinaryTreeItem = new JMenuItem("Преобразовать в бинарное дерево");
        convertToBinaryTreeItem.addActionListener(e -> convertToBinaryTree());
        operationsMenu.add(convertToBinaryTreeItem);

        menuBar.add(operationsMenu);

        // Меню Окна
        JMenu windowMenu = new JMenu("Окна");

        JMenuItem cascadeItem = new JMenuItem("Каскадом");
        cascadeItem.addActionListener(e -> cascadeWindows());
        windowMenu.add(cascadeItem);

        JMenuItem tileItem = new JMenuItem("Плиткой");
        tileItem.addActionListener(e -> tileWindows());
        windowMenu.add(tileItem);

        menuBar.add(windowMenu);

        // Меню Помощь
        JMenu helpMenu = new JMenu("Помощь");

        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createNewGraph() {
        GraphInternalFrame frame = new GraphInternalFrame("Граф " + graphCounter++);
        desktopPane.add(frame);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGraph() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fc.getSelectedFile()))) {
                Graph graph = (Graph) ois.readObject();
                GraphInternalFrame frame = new GraphInternalFrame(graph);
                desktopPane.add(frame);
                frame.setVisible(true);
                frame.setSelected(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private GraphInternalFrame[] getSelectedGraphs(int count) {
        JInternalFrame[] allFrames = desktopPane.getAllFrames();
        if (allFrames.length < count) {
            JOptionPane.showMessageDialog(this,
                "Необходимо открыть минимум " + count + " графа");
            return null;
        }

        GraphInternalFrame[] selected = new GraphInternalFrame[count];
        String[] options = new String[allFrames.length];
        for (int i = 0; i < allFrames.length; i++) {
            options[i] = allFrames[i].getTitle();
        }

        for (int i = 0; i < count; i++) {
            String choice = (String) JOptionPane.showInputDialog(this,
                "Выберите граф " + (i + 1) + ":",
                "Выбор графа",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

            if (choice == null) return null;

            for (JInternalFrame frame : allFrames) {
                if (frame.getTitle().equals(choice)) {
                    selected[i] = (GraphInternalFrame) frame;
                    break;
                }
            }
        }

        return selected;
    }

    private void performCartesianProduct() {
        GraphInternalFrame[] graphs = getSelectedGraphs(2);
        if (graphs == null) return;

        Graph result = GraphAlgorithms.cartesianProduct(
            graphs[0].getGraph(), graphs[1].getGraph());
        GraphInternalFrame frame = new GraphInternalFrame(result);
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void performTensorProduct() {
        GraphInternalFrame[] graphs = getSelectedGraphs(2);
        if (graphs == null) return;

        Graph result = GraphAlgorithms.tensorProduct(
            graphs[0].getGraph(), graphs[1].getGraph());
        GraphInternalFrame frame = new GraphInternalFrame(result);
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void convertToTree() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof GraphInternalFrame) {
            GraphInternalFrame gf = (GraphInternalFrame) frame;
            Graph graph = gf.getGraph();

            if (GraphAlgorithms.isTree(graph)) {
                JOptionPane.showMessageDialog(this, "Граф уже является деревом");
                return;
            }

            // Простое преобразование: строим остовное дерево через DFS
            Graph tree = buildSpanningTree(graph);
            GraphInternalFrame newFrame = new GraphInternalFrame(tree);
            desktopPane.add(newFrame);
            newFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Выберите граф");
        }
    }

    private void convertToBinaryTree() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof GraphInternalFrame) {
            GraphInternalFrame gf = (GraphInternalFrame) frame;
            Graph graph = gf.getGraph();

            // Простое преобразование: оставляем максимум 2 ребра из каждой вершины
            Graph binaryTree = buildBinaryTree(graph);
            GraphInternalFrame newFrame = new GraphInternalFrame(binaryTree);
            desktopPane.add(newFrame);
            newFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Выберите граф");
        }
    }

    private Graph buildSpanningTree(Graph original) {
        Graph tree = new Graph(original.getName() + " (Дерево)");
        if (original.getNodes().isEmpty()) return tree;

        // Копируем узлы
        for (Node node : original.getNodes()) {
            Node newNode = new Node(node.getX(), node.getY());
            newNode.setName(node.getName());
            newNode.setColor(node.getColor());
            tree.addNode(newNode);
        }

        // Строим остовное дерево через DFS
        java.util.Set<Node> visited = new java.util.HashSet<>();
        dfsSpanningTree(original.getNodes().get(0), original, tree, visited, null);

        return tree;
    }

    private void dfsSpanningTree(Node node, Graph original, Graph tree,
                                  java.util.Set<Node> visited, Node parent) {
        visited.add(node);

        for (Edge edge : original.getEdges()) {
            Node neighbor = null;
            if (edge.getFrom() == node) neighbor = edge.getTo();
            else if (edge.getTo() == node) neighbor = edge.getFrom();

            if (neighbor != null && !visited.contains(neighbor)) {
                // Добавляем ребро в дерево
                int fromIdx = original.getNodes().indexOf(node);
                int toIdx = original.getNodes().indexOf(neighbor);
                tree.addEdge(new Edge(tree.getNodes().get(fromIdx),
                                     tree.getNodes().get(toIdx), false));
                dfsSpanningTree(neighbor, original, tree, visited, node);
            }
        }
    }

    private Graph buildBinaryTree(Graph original) {
        Graph binaryTree = new Graph(original.getName() + " (Бинарное)");
        if (original.getNodes().isEmpty()) return binaryTree;

        // Копируем узлы
        for (Node node : original.getNodes()) {
            Node newNode = new Node(node.getX(), node.getY());
            newNode.setName(node.getName());
            newNode.setColor(node.getColor());
            binaryTree.addNode(newNode);
        }

        // Добавляем максимум 2 ребра из каждой вершины
        java.util.Map<Node, Integer> edgeCount = new java.util.HashMap<>();

        for (Edge edge : original.getEdges()) {
            Node from = edge.getFrom();
            Node to = edge.getTo();

            int fromCount = edgeCount.getOrDefault(from, 0);
            int toCount = edgeCount.getOrDefault(to, 0);

            if (fromCount < 2 && toCount < 2) {
                int fromIdx = original.getNodes().indexOf(from);
                int toIdx = original.getNodes().indexOf(to);
                binaryTree.addEdge(new Edge(binaryTree.getNodes().get(fromIdx),
                                           binaryTree.getNodes().get(toIdx),
                                           edge.isDirected()));
                edgeCount.put(from, fromCount + 1);
                edgeCount.put(to, toCount + 1);
            }
        }

        return binaryTree;
    }

    private void cascadeWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        int offset = 30;
        for (int i = 0; i < frames.length; i++) {
            frames[i].setLocation(offset * i, offset * i);
            frames[i].setSize(600, 500);
        }
    }

    private void tileWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        if (frames.length == 0) return;

        int cols = (int) Math.ceil(Math.sqrt(frames.length));
        int rows = (int) Math.ceil((double) frames.length / cols);

        int width = desktopPane.getWidth() / cols;
        int height = desktopPane.getHeight() / rows;

        int index = 0;
        for (int row = 0; row < rows && index < frames.length; row++) {
            for (int col = 0; col < cols && index < frames.length; col++) {
                frames[index].setLocation(col * width, row * height);
                frames[index].setSize(width, height);
                index++;
            }
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Графовый редактор\n" +
            "Лабораторная работа №5\n\n" +
            "Функции:\n" +
            "- Создание и редактирование графов\n" +
            "- Работа с несколькими графами (MDI)\n" +
            "- Сохранение/загрузка графов\n" +
            "- Проверка на дерево\n" +
            "- Поиск гамильтоновых циклов\n" +
            "- Вычисление диаметра, радиуса, центра\n" +
            "- Декартово и тензорное произведение\n" +
            "- Преобразование в деревья",
            "О программе",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

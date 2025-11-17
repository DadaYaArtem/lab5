package com.grapheditor.algorithms;

import com.grapheditor.model.*;
import java.util.*;

public class GraphAlgorithms {

    // Проверка является ли граф деревом
    public static boolean isTree(Graph graph) {
        List<Node> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();

        if (nodes.isEmpty()) return true;
        if (edges.size() != nodes.size() - 1) return false;

        // Проверка связности через DFS
        Set<Node> visited = new HashSet<>();
        dfs(nodes.get(0), graph, visited);

        return visited.size() == nodes.size();
    }

    private static void dfs(Node node, Graph graph, Set<Node> visited) {
        visited.add(node);
        for (Edge edge : graph.getEdges()) {
            Node neighbor = null;
            if (edge.getFrom() == node) neighbor = edge.getTo();
            else if (edge.getTo() == node) neighbor = edge.getFrom();

            if (neighbor != null && !visited.contains(neighbor)) {
                dfs(neighbor, graph, visited);
            }
        }
    }

    // Нахождение гамильтонова цикла (простой backtracking)
    public static List<Node> findHamiltonianCycle(Graph graph) {
        List<Node> nodes = graph.getNodes();
        if (nodes.isEmpty()) return null;

        List<Node> path = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        if (hamiltonianUtil(nodes.get(0), graph, path, visited, nodes.size())) {
            return path;
        }
        return null;
    }

    private static boolean hamiltonianUtil(Node current, Graph graph, List<Node> path,
                                          Set<Node> visited, int totalNodes) {
        path.add(current);
        visited.add(current);

        if (path.size() == totalNodes) {
            // Проверяем есть ли ребро обратно к началу
            Node start = path.get(0);
            for (Edge edge : graph.getEdges()) {
                if ((edge.getFrom() == current && edge.getTo() == start) ||
                    (!edge.isDirected() && edge.getFrom() == start && edge.getTo() == current)) {
                    return true;
                }
            }
            path.remove(path.size() - 1);
            visited.remove(current);
            return false;
        }

        for (Edge edge : graph.getEdges()) {
            Node next = null;
            if (edge.getFrom() == current) next = edge.getTo();
            else if (!edge.isDirected() && edge.getTo() == current) next = edge.getFrom();

            if (next != null && !visited.contains(next)) {
                if (hamiltonianUtil(next, graph, path, visited, totalNodes)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        visited.remove(current);
        return false;
    }

    // Вычисление кратчайших расстояний между всеми парами вершин (Floyd-Warshall)
    public static int[][] allPairsShortestPath(Graph graph) {
        List<Node> nodes = graph.getNodes();
        int n = nodes.size();
        int[][] dist = new int[n][n];

        // Инициализация
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = (i == j) ? 0 : Integer.MAX_VALUE / 2;
            }
        }

        // Заполнение существующими ребрами
        for (Edge edge : graph.getEdges()) {
            int i = nodes.indexOf(edge.getFrom());
            int j = nodes.indexOf(edge.getTo());
            dist[i][j] = 1;
            if (!edge.isDirected()) {
                dist[j][i] = 1;
            }
        }

        // Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    // Вычисление диаметра графа
    public static int getDiameter(Graph graph) {
        int[][] dist = allPairsShortestPath(graph);
        int diameter = 0;
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (dist[i][j] != Integer.MAX_VALUE / 2) {
                    diameter = Math.max(diameter, dist[i][j]);
                }
            }
        }
        return diameter;
    }

    // Вычисление радиуса графа
    public static int getRadius(Graph graph) {
        int[][] dist = allPairsShortestPath(graph);
        int n = dist.length;
        int radius = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            int eccentricity = 0;
            for (int j = 0; j < n; j++) {
                if (dist[i][j] != Integer.MAX_VALUE / 2) {
                    eccentricity = Math.max(eccentricity, dist[i][j]);
                }
            }
            radius = Math.min(radius, eccentricity);
        }

        return radius == Integer.MAX_VALUE ? 0 : radius;
    }

    // Нахождение центра графа
    public static List<Node> getCenter(Graph graph) {
        int[][] dist = allPairsShortestPath(graph);
        List<Node> nodes = graph.getNodes();
        int n = dist.length;
        int radius = getRadius(graph);
        List<Node> center = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int eccentricity = 0;
            for (int j = 0; j < n; j++) {
                if (dist[i][j] != Integer.MAX_VALUE / 2) {
                    eccentricity = Math.max(eccentricity, dist[i][j]);
                }
            }
            if (eccentricity == radius) {
                center.add(nodes.get(i));
            }
        }

        return center;
    }

    // Декартово произведение графов
    public static Graph cartesianProduct(Graph g1, Graph g2) {
        Graph result = new Graph("Cartesian(" + g1.getName() + ", " + g2.getName() + ")");
        Map<String, Node> nodeMap = new HashMap<>();

        int xOffset = 0, yOffset = 0;
        int spacing = 80;

        // Создаем узлы
        for (int i = 0; i < g1.getNodes().size(); i++) {
            for (int j = 0; j < g2.getNodes().size(); j++) {
                Node n1 = g1.getNodes().get(i);
                Node n2 = g2.getNodes().get(j);
                Node newNode = new Node(xOffset + j * spacing, yOffset + i * spacing);
                newNode.setName(n1.getName() + "," + n2.getName());
                result.addNode(newNode);
                nodeMap.put(i + "," + j, newNode);
            }
        }

        // Создаем ребра
        for (int i = 0; i < g1.getNodes().size(); i++) {
            for (int j = 0; j < g2.getNodes().size(); j++) {
                // Ребра из g1
                for (Edge e1 : g1.getEdges()) {
                    int i1 = g1.getNodes().indexOf(e1.getFrom());
                    int i2 = g1.getNodes().indexOf(e1.getTo());
                    if (i1 == i) {
                        Node from = nodeMap.get(i1 + "," + j);
                        Node to = nodeMap.get(i2 + "," + j);
                        result.addEdge(new Edge(from, to, e1.isDirected()));
                    }
                }

                // Ребра из g2
                for (Edge e2 : g2.getEdges()) {
                    int j1 = g2.getNodes().indexOf(e2.getFrom());
                    int j2 = g2.getNodes().indexOf(e2.getTo());
                    if (j1 == j) {
                        Node from = nodeMap.get(i + "," + j1);
                        Node to = nodeMap.get(i + "," + j2);
                        result.addEdge(new Edge(from, to, e2.isDirected()));
                    }
                }
            }
        }

        return result;
    }

    // Тензорное произведение графов
    public static Graph tensorProduct(Graph g1, Graph g2) {
        Graph result = new Graph("Tensor(" + g1.getName() + ", " + g2.getName() + ")");
        Map<String, Node> nodeMap = new HashMap<>();

        int xOffset = 0, yOffset = 0;
        int spacing = 80;

        // Создаем узлы
        for (int i = 0; i < g1.getNodes().size(); i++) {
            for (int j = 0; j < g2.getNodes().size(); j++) {
                Node n1 = g1.getNodes().get(i);
                Node n2 = g2.getNodes().get(j);
                Node newNode = new Node(xOffset + j * spacing, yOffset + i * spacing);
                newNode.setName(n1.getName() + "," + n2.getName());
                result.addNode(newNode);
                nodeMap.put(i + "," + j, newNode);
            }
        }

        // Создаем ребра (только если есть ребра в обоих графах)
        for (Edge e1 : g1.getEdges()) {
            for (Edge e2 : g2.getEdges()) {
                int i1 = g1.getNodes().indexOf(e1.getFrom());
                int i2 = g1.getNodes().indexOf(e1.getTo());
                int j1 = g2.getNodes().indexOf(e2.getFrom());
                int j2 = g2.getNodes().indexOf(e2.getTo());

                Node from = nodeMap.get(i1 + "," + j1);
                Node to = nodeMap.get(i2 + "," + j2);
                boolean directed = e1.isDirected() || e2.isDirected();
                result.addEdge(new Edge(from, to, directed));
            }
        }

        return result;
    }
}

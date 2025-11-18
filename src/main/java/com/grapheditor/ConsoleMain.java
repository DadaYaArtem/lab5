package com.grapheditor;

import com.grapheditor.model.*;
import com.grapheditor.algorithms.*;
import java.util.*;
import java.io.*;

public class ConsoleMain {
    private static Map<String, Graph> graphs = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Графовый редактор (консольная версия) ===\n");

        // Автоматическое создание примеров при запуске
        createExampleGraphs();
        System.out.println("✓ Созданы примеры графов для демонстрации\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": createGraph(); break;
                case "2": addNode(); break;
                case "3": addEdge(); break;
                case "4": removeNode(); break;
                case "5": removeEdge(); break;
                case "6": showGraphInfo(); break;
                case "7": checkIsTree(); break;
                case "8": findHamiltonianCycle(); break;
                case "9": showDiameterRadiusCenter(); break;
                case "10": cartesianProduct(); break;
                case "11": tensorProduct(); break;
                case "12": saveGraph(); break;
                case "13": loadGraph(); break;
                case "14": listGraphs(); break;
                case "15": runTests(); break;
                case "16": createExampleGraphs(); System.out.println("Примеры созданы!"); break;
                case "0": running = false; break;
                default: System.out.println("Неверный выбор!");
            }
            System.out.println();
        }

        System.out.println("Выход из программы.");
    }

    private static void printMenu() {
        System.out.println("=== МЕНЮ ===");
        System.out.println("1. Создать новый граф");
        System.out.println("2. Добавить узел");
        System.out.println("3. Добавить ребро/дугу");
        System.out.println("4. Удалить узел");
        System.out.println("5. Удалить ребро");
        System.out.println("6. Показать информацию о графе");
        System.out.println("7. Проверить является ли граф деревом");
        System.out.println("8. Найти гамильтонов цикл");
        System.out.println("9. Диаметр, радиус, центр");
        System.out.println("10. Декартово произведение");
        System.out.println("11. Тензорное произведение");
        System.out.println("12. Сохранить граф");
        System.out.println("13. Загрузить граф");
        System.out.println("14. Список графов");
        System.out.println("15. Запустить тесты");
        System.out.println("16. Создать примеры графов");
        System.out.println("0. Выход");
        System.out.print("Выбор: ");
    }

    private static void createGraph() {
        System.out.print("Имя графа: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ошибка: имя не может быть пустым!");
            return;
        }
        graphs.put(name, new Graph(name));
        System.out.println("Граф '" + name + "' создан.");
    }

    private static void addNode() {
        Graph graph = selectGraph();
        if (graph == null) return;

        System.out.print("Имя узла: ");
        String name = scanner.nextLine().trim();

        Node node = new Node(0, 0);
        if (!name.isEmpty()) {
            node.setName(name);
        }
        graph.addNode(node);
        System.out.println("Узел '" + node.getName() + "' добавлен.");
    }

    private static void addEdge() {
        Graph graph = selectGraph();
        if (graph == null) return;

        if (graph.getNodes().size() < 2) {
            System.out.println("Недостаточно узлов! Нужно минимум 2.");
            return;
        }

        System.out.println("Узлы в графе:");
        for (int i = 0; i < graph.getNodes().size(); i++) {
            System.out.println(i + ". " + graph.getNodes().get(i).getName());
        }

        System.out.print("Индекс начального узла: ");
        int fromIdx = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Индекс конечного узла: ");
        int toIdx = Integer.parseInt(scanner.nextLine().trim());

        if (fromIdx < 0 || fromIdx >= graph.getNodes().size() ||
            toIdx < 0 || toIdx >= graph.getNodes().size()) {
            System.out.println("Ошибка: неверные индексы!");
            return;
        }

        System.out.print("Ориентированное? (y/n): ");
        boolean directed = scanner.nextLine().trim().equalsIgnoreCase("y");

        Node from = graph.getNodes().get(fromIdx);
        Node to = graph.getNodes().get(toIdx);
        graph.addEdge(new Edge(from, to, directed));

        System.out.println("Ребро добавлено: " + from.getName() + " -> " + to.getName());
    }

    private static void removeNode() {
        Graph graph = selectGraph();
        if (graph == null) return;

        if (graph.getNodes().isEmpty()) {
            System.out.println("Граф пуст!");
            return;
        }

        System.out.println("Узлы в графе:");
        for (int i = 0; i < graph.getNodes().size(); i++) {
            System.out.println(i + ". " + graph.getNodes().get(i).getName());
        }

        System.out.print("Индекс узла для удаления: ");
        int idx = Integer.parseInt(scanner.nextLine().trim());

        if (idx >= 0 && idx < graph.getNodes().size()) {
            Node node = graph.getNodes().get(idx);
            graph.removeNode(node);
            System.out.println("Узел удалён.");
        } else {
            System.out.println("Неверный индекс!");
        }
    }

    private static void removeEdge() {
        Graph graph = selectGraph();
        if (graph == null) return;

        if (graph.getEdges().isEmpty()) {
            System.out.println("Нет рёбер!");
            return;
        }

        System.out.println("Рёбра в графе:");
        for (int i = 0; i < graph.getEdges().size(); i++) {
            Edge e = graph.getEdges().get(i);
            System.out.println(i + ". " + e.getFrom().getName() +
                             (e.isDirected() ? " -> " : " - ") + e.getTo().getName());
        }

        System.out.print("Индекс ребра для удаления: ");
        int idx = Integer.parseInt(scanner.nextLine().trim());

        if (idx >= 0 && idx < graph.getEdges().size()) {
            graph.removeEdge(graph.getEdges().get(idx));
            System.out.println("Ребро удалено.");
        } else {
            System.out.println("Неверный индекс!");
        }
    }

    private static void showGraphInfo() {
        Graph graph = selectGraph();
        if (graph == null) return;

        System.out.println("\n=== Информация о графе '" + graph.getName() + "' ===");
        System.out.println("Вершин: " + graph.getNodes().size());
        System.out.println("Рёбер: " + graph.getEdges().size());

        if (!graph.getNodes().isEmpty()) {
            System.out.println("\nСтепени вершин:");
            for (Node node : graph.getNodes()) {
                System.out.println("  " + node.getName() + ": " +
                                 graph.getDegree(node) +
                                 " (in: " + graph.getInDegree(node) +
                                 ", out: " + graph.getOutDegree(node) + ")");
            }
        }

        System.out.println("\nРёбра:");
        for (Edge e : graph.getEdges()) {
            System.out.println("  " + e.getFrom().getName() +
                             (e.isDirected() ? " -> " : " - ") + e.getTo().getName());
        }
    }

    private static void checkIsTree() {
        Graph graph = selectGraph();
        if (graph == null) return;

        boolean isTree = GraphAlgorithms.isTree(graph);
        System.out.println("Граф '" + graph.getName() + "' " +
                         (isTree ? "ЯВЛЯЕТСЯ" : "НЕ ЯВЛЯЕТСЯ") + " деревом.");
    }

    private static void findHamiltonianCycle() {
        Graph graph = selectGraph();
        if (graph == null) return;

        System.out.println("Поиск гамильтонова цикла...");
        List<Node> cycle = GraphAlgorithms.findHamiltonianCycle(graph);

        if (cycle == null) {
            System.out.println("Гамильтонов цикл не найден.");
        } else {
            System.out.print("Гамильтонов цикл: ");
            for (Node node : cycle) {
                System.out.print(node.getName() + " -> ");
            }
            System.out.println(cycle.get(0).getName());
        }
    }

    private static void showDiameterRadiusCenter() {
        Graph graph = selectGraph();
        if (graph == null) return;

        if (graph.getNodes().isEmpty()) {
            System.out.println("Граф пуст!");
            return;
        }

        int diameter = GraphAlgorithms.getDiameter(graph);
        int radius = GraphAlgorithms.getRadius(graph);
        List<Node> center = GraphAlgorithms.getCenter(graph);

        System.out.println("Диаметр: " + diameter);
        System.out.println("Радиус: " + radius);
        System.out.print("Центр: ");
        for (Node node : center) {
            System.out.print(node.getName() + " ");
        }
        System.out.println();
    }

    private static void cartesianProduct() {
        if (graphs.size() < 2) {
            System.out.println("Нужно минимум 2 графа!");
            return;
        }

        System.out.println("Выберите первый граф:");
        Graph g1 = selectGraph();
        if (g1 == null) return;

        System.out.println("Выберите второй граф:");
        Graph g2 = selectGraph();
        if (g2 == null) return;

        Graph result = GraphAlgorithms.cartesianProduct(g1, g2);
        graphs.put(result.getName(), result);
        System.out.println("Создан граф '" + result.getName() + "'");
        System.out.println("Вершин: " + result.getNodes().size());
        System.out.println("Рёбер: " + result.getEdges().size());
    }

    private static void tensorProduct() {
        if (graphs.size() < 2) {
            System.out.println("Нужно минимум 2 графа!");
            return;
        }

        System.out.println("Выберите первый граф:");
        Graph g1 = selectGraph();
        if (g1 == null) return;

        System.out.println("Выберите второй граф:");
        Graph g2 = selectGraph();
        if (g2 == null) return;

        Graph result = GraphAlgorithms.tensorProduct(g1, g2);
        graphs.put(result.getName(), result);
        System.out.println("Создан граф '" + result.getName() + "'");
        System.out.println("Вершин: " + result.getNodes().size());
        System.out.println("Рёбер: " + result.getEdges().size());
    }

    private static void saveGraph() {
        Graph graph = selectGraph();
        if (graph == null) return;

        System.out.print("Имя файла: ");
        String filename = scanner.nextLine().trim();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(graph);
            System.out.println("Граф сохранён в " + filename);
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private static void loadGraph() {
        System.out.print("Имя файла: ");
        String filename = scanner.nextLine().trim();

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            Graph graph = (Graph) ois.readObject();
            graphs.put(graph.getName(), graph);
            System.out.println("Граф '" + graph.getName() + "' загружен.");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }

    private static void listGraphs() {
        if (graphs.isEmpty()) {
            System.out.println("Нет созданных графов.");
            return;
        }

        System.out.println("\n=== Список графов ===");
        for (String name : graphs.keySet()) {
            Graph g = graphs.get(name);
            System.out.println("  " + name + " (вершин: " + g.getNodes().size() +
                             ", рёбер: " + g.getEdges().size() + ")");
        }
    }

    private static Graph selectGraph() {
        if (graphs.isEmpty()) {
            System.out.println("Нет созданных графов! Сначала создайте граф.");
            return null;
        }

        System.out.println("Доступные графы:");
        List<String> names = new ArrayList<>(graphs.keySet());
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + ". " + names.get(i));
        }

        System.out.print("Выберите граф (индекс): ");
        int idx = Integer.parseInt(scanner.nextLine().trim());

        if (idx >= 0 && idx < names.size()) {
            return graphs.get(names.get(idx));
        } else {
            System.out.println("Неверный индекс!");
            return null;
        }
    }

    private static void runTests() {
        System.out.println("\n=== Запуск тестов ===\n");

        // Тест 1: Пустой граф
        System.out.println("Тест 1: Пустой граф");
        Graph empty = new Graph("Empty");
        System.out.println("  Является деревом: " + GraphAlgorithms.isTree(empty));
        System.out.println("  Диаметр: " + GraphAlgorithms.getDiameter(empty));
        System.out.println("  Радиус: " + GraphAlgorithms.getRadius(empty));

        // Тест 2: Один узел
        System.out.println("\nТест 2: Один узел");
        Graph single = new Graph("Single");
        single.addNode(new Node(0, 0));
        System.out.println("  Является деревом: " + GraphAlgorithms.isTree(single));

        // Тест 3: Простое дерево
        System.out.println("\nТест 3: Простое дерево (3 узла)");
        Graph tree = createSimpleTree();
        System.out.println("  Является деревом: " + GraphAlgorithms.isTree(tree));
        System.out.println("  Вершин: " + tree.getNodes().size());
        System.out.println("  Рёбер: " + tree.getEdges().size());

        // Тест 4: Граф с циклом
        System.out.println("\nТест 4: Треугольник (граф с циклом)");
        Graph triangle = createTriangle();
        System.out.println("  Является деревом: " + GraphAlgorithms.isTree(triangle));
        System.out.println("  Гамильтонов цикл: " +
                         (GraphAlgorithms.findHamiltonianCycle(triangle) != null ? "найден" : "не найден"));

        // Тест 5: Полный граф K4
        System.out.println("\nТест 5: Полный граф K4");
        Graph k4 = createCompleteGraph(4);
        System.out.println("  Вершин: " + k4.getNodes().size());
        System.out.println("  Рёбер: " + k4.getEdges().size());
        System.out.println("  Диаметр: " + GraphAlgorithms.getDiameter(k4));
        System.out.println("  Радиус: " + GraphAlgorithms.getRadius(k4));

        // Тест 6: Декартово произведение
        System.out.println("\nТест 6: Декартово произведение P2 × P2");
        Graph p2_1 = createPath(2);
        Graph p2_2 = createPath(2);
        Graph cartesian = GraphAlgorithms.cartesianProduct(p2_1, p2_2);
        System.out.println("  Результат: " + cartesian.getNodes().size() + " вершин, " +
                         cartesian.getEdges().size() + " рёбер");
        System.out.println("  Ожидается: 4 вершины, 4 ребра");

        // Тест 7: Тензорное произведение
        System.out.println("\nТест 7: Тензорное произведение K3 ⊗ K2");
        Graph k3 = createCompleteGraph(3);
        Graph k2 = createCompleteGraph(2);
        Graph tensor = GraphAlgorithms.tensorProduct(k3, k2);
        System.out.println("  Результат: " + tensor.getNodes().size() + " вершин, " +
                         tensor.getEdges().size() + " рёбер");

        System.out.println("\n=== Тесты завершены ===");
    }

    private static Graph createSimpleTree() {
        Graph g = new Graph("Tree");
        Node n1 = new Node(0, 0); n1.setName("A");
        Node n2 = new Node(1, 0); n2.setName("B");
        Node n3 = new Node(2, 0); n3.setName("C");
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addEdge(new Edge(n1, n2, false));
        g.addEdge(new Edge(n2, n3, false));
        return g;
    }

    private static Graph createTriangle() {
        Graph g = new Graph("Triangle");
        Node n1 = new Node(0, 0); n1.setName("A");
        Node n2 = new Node(1, 0); n2.setName("B");
        Node n3 = new Node(2, 0); n3.setName("C");
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addEdge(new Edge(n1, n2, false));
        g.addEdge(new Edge(n2, n3, false));
        g.addEdge(new Edge(n3, n1, false));
        return g;
    }

    private static Graph createCompleteGraph(int n) {
        Graph g = new Graph("K" + n);
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Node node = new Node(i, 0);
            node.setName("V" + (i + 1));
            g.addNode(node);
            nodes.add(node);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                g.addEdge(new Edge(nodes.get(i), nodes.get(j), false));
            }
        }
        return g;
    }

    private static Graph createPath(int n) {
        Graph g = new Graph("P" + n);
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Node node = new Node(i, 0);
            node.setName("V" + (i + 1));
            g.addNode(node);
            nodes.add(node);
        }
        for (int i = 0; i < n - 1; i++) {
            g.addEdge(new Edge(nodes.get(i), nodes.get(i + 1), false));
        }
        return g;
    }

    // Создание примеров графов для демонстрации
    private static void createExampleGraphs() {
        // 1. Дерево (звезда)
        Graph tree = new Graph("Дерево");
        Node center = new Node(100, 100);
        center.setName("Центр");
        tree.addNode(center);
        for (int i = 1; i <= 4; i++) {
            Node leaf = new Node(100 + i * 50, 100 + i * 30);
            leaf.setName("Лист" + i);
            tree.addNode(leaf);
            tree.addEdge(new Edge(center, leaf, false));
        }
        graphs.put("Дерево", tree);

        // 2. Треугольник (простой цикл)
        Graph triangle = new Graph("Треугольник");
        Node t1 = new Node(0, 0);
        Node t2 = new Node(100, 0);
        Node t3 = new Node(50, 100);
        t1.setName("A");
        t2.setName("B");
        t3.setName("C");
        triangle.addNode(t1);
        triangle.addNode(t2);
        triangle.addNode(t3);
        triangle.addEdge(new Edge(t1, t2, false));
        triangle.addEdge(new Edge(t2, t3, false));
        triangle.addEdge(new Edge(t3, t1, false));
        graphs.put("Треугольник", triangle);

        // 3. Полный граф K4
        Graph k4 = new Graph("K4");
        Node[] k4nodes = new Node[4];
        for (int i = 0; i < 4; i++) {
            k4nodes[i] = new Node(i * 60, i * 60);
            k4nodes[i].setName("V" + (i + 1));
            k4.addNode(k4nodes[i]);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                k4.addEdge(new Edge(k4nodes[i], k4nodes[j], false));
            }
        }
        graphs.put("K4", k4);

        // 4. Путь P5
        Graph p5 = new Graph("Путь P5");
        Node[] p5nodes = new Node[5];
        for (int i = 0; i < 5; i++) {
            p5nodes[i] = new Node(i * 70, 100);
            p5nodes[i].setName("P" + (i + 1));
            p5.addNode(p5nodes[i]);
        }
        for (int i = 0; i < 4; i++) {
            p5.addEdge(new Edge(p5nodes[i], p5nodes[i + 1], false));
        }
        graphs.put("Путь P5", p5);

        // 5. Ориентированный граф
        Graph directed = new Graph("Ориентированный");
        Node d1 = new Node(0, 0);
        Node d2 = new Node(100, 0);
        Node d3 = new Node(100, 100);
        Node d4 = new Node(0, 100);
        d1.setName("Start");
        d2.setName("Mid1");
        d3.setName("Mid2");
        d4.setName("End");
        directed.addNode(d1);
        directed.addNode(d2);
        directed.addNode(d3);
        directed.addNode(d4);
        directed.addEdge(new Edge(d1, d2, true));
        directed.addEdge(new Edge(d2, d3, true));
        directed.addEdge(new Edge(d3, d4, true));
        directed.addEdge(new Edge(d1, d4, true));
        graphs.put("Ориентированный", directed);

        // 6. Маленький граф для произведений
        Graph k2 = new Graph("K2");
        Node k21 = new Node(0, 0);
        Node k22 = new Node(100, 0);
        k21.setName("X");
        k22.setName("Y");
        k2.addNode(k21);
        k2.addNode(k22);
        k2.addEdge(new Edge(k21, k22, false));
        graphs.put("K2", k2);

        // 7. Квадрат (для демонстрации гамильтонова цикла)
        Graph square = new Graph("Квадрат");
        Node s1 = new Node(0, 0);
        Node s2 = new Node(100, 0);
        Node s3 = new Node(100, 100);
        Node s4 = new Node(0, 100);
        s1.setName("A");
        s2.setName("B");
        s3.setName("C");
        s4.setName("D");
        square.addNode(s1);
        square.addNode(s2);
        square.addNode(s3);
        square.addNode(s4);
        square.addEdge(new Edge(s1, s2, false));
        square.addEdge(new Edge(s2, s3, false));
        square.addEdge(new Edge(s3, s4, false));
        square.addEdge(new Edge(s4, s1, false));
        graphs.put("Квадрат", square);

        System.out.println("Созданы примеры:");
        System.out.println("  1. Дерево (звезда с 5 узлами)");
        System.out.println("  2. Треугольник (цикл из 3 узлов)");
        System.out.println("  3. K4 (полный граф на 4 узлах)");
        System.out.println("  4. Путь P5 (путь из 5 узлов)");
        System.out.println("  5. Ориентированный (граф с дугами)");
        System.out.println("  6. K2 (для произведений)");
        System.out.println("  7. Квадрат (гамильтонов цикл)");
    }
}

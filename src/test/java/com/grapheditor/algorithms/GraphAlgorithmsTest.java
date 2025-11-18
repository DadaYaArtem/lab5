package com.grapheditor.algorithms;

import com.grapheditor.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class GraphAlgorithmsTest {

    private Graph emptyGraph;
    private Graph singleNodeGraph;
    private Graph treeGraph;
    private Graph cycleGraph;
    private Graph completeGraph;

    @Before
    public void setUp() {
        emptyGraph = new Graph("Empty");

        singleNodeGraph = new Graph("Single");
        singleNodeGraph.addNode(new Node(0, 0));

        treeGraph = createTree();
        cycleGraph = createCycle();
        completeGraph = createCompleteGraph(4);
    }

    // === Тесты для isTree ===

    @Test
    public void testIsTree_EmptyGraph() {
        assertTrue("Пустой граф является деревом", GraphAlgorithms.isTree(emptyGraph));
    }

    @Test
    public void testIsTree_SingleNode() {
        assertTrue("Один узел - это дерево", GraphAlgorithms.isTree(singleNodeGraph));
    }

    @Test
    public void testIsTree_ValidTree() {
        assertTrue("Граф-дерево должен определяться как дерево", GraphAlgorithms.isTree(treeGraph));
    }

    @Test
    public void testIsTree_GraphWithCycle() {
        assertFalse("Граф с циклом не является деревом", GraphAlgorithms.isTree(cycleGraph));
    }

    @Test
    public void testIsTree_DisconnectedGraph() {
        Graph disconnected = new Graph("Disconnected");
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 0);
        Node n3 = new Node(2, 0);
        Node n4 = new Node(3, 0);
        disconnected.addNode(n1);
        disconnected.addNode(n2);
        disconnected.addNode(n3);
        disconnected.addNode(n4);
        disconnected.addEdge(new Edge(n1, n2, false));
        disconnected.addEdge(new Edge(n3, n4, false));

        assertFalse("Несвязный граф не является деревом", GraphAlgorithms.isTree(disconnected));
    }

    @Test
    public void testIsTree_TooManyEdges() {
        Graph tooMany = new Graph("TooMany");
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 0);
        Node n3 = new Node(2, 0);
        tooMany.addNode(n1);
        tooMany.addNode(n2);
        tooMany.addNode(n3);
        tooMany.addEdge(new Edge(n1, n2, false));
        tooMany.addEdge(new Edge(n2, n3, false));
        tooMany.addEdge(new Edge(n3, n1, false));

        assertFalse("Граф с n рёбрами при n узлах не дерево", GraphAlgorithms.isTree(tooMany));
    }

    // === Тесты для гамильтонова цикла ===

    @Test
    public void testHamiltonianCycle_EmptyGraph() {
        assertNull("В пустом графе нет гамильтонова цикла",
                  GraphAlgorithms.findHamiltonianCycle(emptyGraph));
    }

    @Test
    public void testHamiltonianCycle_SingleNode() {
        assertNull("Один узел не образует цикл",
                  GraphAlgorithms.findHamiltonianCycle(singleNodeGraph));
    }

    @Test
    public void testHamiltonianCycle_Triangle() {
        List<Node> cycle = GraphAlgorithms.findHamiltonianCycle(cycleGraph);
        assertNotNull("Треугольник имеет гамильтонов цикл", cycle);
        assertEquals(3, cycle.size());
    }

    @Test
    public void testHamiltonianCycle_CompleteGraph() {
        List<Node> cycle = GraphAlgorithms.findHamiltonianCycle(completeGraph);
        assertNotNull("Полный граф K4 имеет гамильтонов цикл", cycle);
        assertEquals(4, cycle.size());
    }

    @Test
    public void testHamiltonianCycle_Path() {
        Graph path = new Graph("Path");
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 0);
        Node n3 = new Node(2, 0);
        path.addNode(n1);
        path.addNode(n2);
        path.addNode(n3);
        path.addEdge(new Edge(n1, n2, false));
        path.addEdge(new Edge(n2, n3, false));

        assertNull("Путь без замыкания не имеет гамильтонова цикла",
                  GraphAlgorithms.findHamiltonianCycle(path));
    }

    // === Тесты для диаметра, радиуса, центра ===

    @Test
    public void testDiameter_EmptyGraph() {
        assertEquals(0, GraphAlgorithms.getDiameter(emptyGraph));
    }

    @Test
    public void testDiameter_SingleNode() {
        assertEquals(0, GraphAlgorithms.getDiameter(singleNodeGraph));
    }

    @Test
    public void testDiameter_Path() {
        Graph path = createPath(4);
        assertEquals("Диаметр пути из 4 узлов = 3", 3, GraphAlgorithms.getDiameter(path));
    }

    @Test
    public void testDiameter_CompleteGraph() {
        assertEquals("Диаметр полного графа K4 = 1", 1, GraphAlgorithms.getDiameter(completeGraph));
    }

    @Test
    public void testRadius_EmptyGraph() {
        assertEquals(0, GraphAlgorithms.getRadius(emptyGraph));
    }

    @Test
    public void testRadius_SingleNode() {
        assertEquals(0, GraphAlgorithms.getRadius(singleNodeGraph));
    }

    @Test
    public void testRadius_Path() {
        Graph path = createPath(5);
        assertEquals("Радиус пути из 5 узлов = 2", 2, GraphAlgorithms.getRadius(path));
    }

    @Test
    public void testRadius_CompleteGraph() {
        assertEquals("Радиус полного графа K4 = 1", 1, GraphAlgorithms.getRadius(completeGraph));
    }

    @Test
    public void testCenter_EmptyGraph() {
        List<Node> center = GraphAlgorithms.getCenter(emptyGraph);
        assertNotNull(center);
        assertEquals(0, center.size());
    }

    @Test
    public void testCenter_SingleNode() {
        List<Node> center = GraphAlgorithms.getCenter(singleNodeGraph);
        assertEquals(1, center.size());
    }

    @Test
    public void testCenter_Path() {
        Graph path = createPath(5);
        List<Node> center = GraphAlgorithms.getCenter(path);
        assertEquals("Центр пути из 5 узлов - средний узел", 1, center.size());
    }

    @Test
    public void testCenter_CompleteGraph() {
        List<Node> center = GraphAlgorithms.getCenter(completeGraph);
        assertEquals("В полном графе все узлы - центр", 4, center.size());
    }

    // === Тесты для произведений графов ===

    @Test
    public void testCartesianProduct_EmptyGraphs() {
        Graph result = GraphAlgorithms.cartesianProduct(emptyGraph, emptyGraph);
        assertEquals(0, result.getNodes().size());
        assertEquals(0, result.getEdges().size());
    }

    @Test
    public void testCartesianProduct_P2xP2() {
        Graph p2_1 = createPath(2);
        Graph p2_2 = createPath(2);
        Graph result = GraphAlgorithms.cartesianProduct(p2_1, p2_2);

        assertEquals("P2 x P2 имеет 4 вершины", 4, result.getNodes().size());
        assertEquals("P2 x P2 имеет 4 ребра", 4, result.getEdges().size());
    }

    @Test
    public void testCartesianProduct_K2xK2() {
        Graph k2_1 = createCompleteGraph(2);
        Graph k2_2 = createCompleteGraph(2);
        Graph result = GraphAlgorithms.cartesianProduct(k2_1, k2_2);

        assertEquals("K2 x K2 имеет 4 вершины", 4, result.getNodes().size());
        assertEquals("K2 x K2 имеет 5 рёбер", 6, result.getEdges().size());
    }

    @Test
    public void testTensorProduct_EmptyGraphs() {
        Graph result = GraphAlgorithms.tensorProduct(emptyGraph, emptyGraph);
        assertEquals(0, result.getNodes().size());
        assertEquals(0, result.getEdges().size());
    }

    @Test
    public void testTensorProduct_K2xK2() {
        Graph k2_1 = createCompleteGraph(2);
        Graph k2_2 = createCompleteGraph(2);
        Graph result = GraphAlgorithms.tensorProduct(k2_1, k2_2);

        assertEquals("K2 ⊗ K2 имеет 4 вершины", 4, result.getNodes().size());
        assertEquals("K2 ⊗ K2 имеет 1 ребро", 1, result.getEdges().size());
    }

    @Test
    public void testTensorProduct_K3xK2() {
        Graph k3 = createCompleteGraph(3);
        Graph k2 = createCompleteGraph(2);
        Graph result = GraphAlgorithms.tensorProduct(k3, k2);

        assertEquals("K3 ⊗ K2 имеет 6 вершин", 6, result.getNodes().size());
        assertEquals("K3 ⊗ K2 имеет 3 ребра", 3, result.getEdges().size());
    }

    @Test
    public void testTensorProduct_PathAndPath() {
        Graph p2 = createPath(2);
        Graph p3 = createPath(3);
        Graph result = GraphAlgorithms.tensorProduct(p2, p3);

        assertEquals(6, result.getNodes().size());
        assertEquals("P2 ⊗ P3 имеет 2 ребра", 2, result.getEdges().size());
    }

    // === Граничные случаи ===

    @Test
    public void testAllPairsShortestPath_EmptyGraph() {
        int[][] dist = GraphAlgorithms.allPairsShortestPath(emptyGraph);
        assertEquals(0, dist.length);
    }

    @Test
    public void testAllPairsShortestPath_SingleNode() {
        int[][] dist = GraphAlgorithms.allPairsShortestPath(singleNodeGraph);
        assertEquals(1, dist.length);
        assertEquals(0, dist[0][0]);
    }

    @Test
    public void testAllPairsShortestPath_Path() {
        Graph path = createPath(4);
        int[][] dist = GraphAlgorithms.allPairsShortestPath(path);

        assertEquals(4, dist.length);
        assertEquals(0, dist[0][0]);
        assertEquals(1, dist[0][1]);
        assertEquals(2, dist[0][2]);
        assertEquals(3, dist[0][3]);
    }

    @Test
    public void testAllPairsShortestPath_CompleteGraph() {
        int[][] dist = GraphAlgorithms.allPairsShortestPath(completeGraph);

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (i == j) {
                    assertEquals(0, dist[i][j]);
                } else {
                    assertEquals(1, dist[i][j]);
                }
            }
        }
    }

    @Test
    public void testCartesianProduct_SingleNodes() {
        Graph result = GraphAlgorithms.cartesianProduct(singleNodeGraph, singleNodeGraph);
        assertEquals(1, result.getNodes().size());
        assertEquals(0, result.getEdges().size());
    }

    @Test
    public void testTensorProduct_WithEmptyGraph() {
        Graph result = GraphAlgorithms.tensorProduct(treeGraph, emptyGraph);
        assertEquals(0, result.getNodes().size());
        assertEquals(0, result.getEdges().size());
    }

    // === Вспомогательные методы ===

    private Graph createTree() {
        Graph g = new Graph("Tree");
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 0);
        Node n3 = new Node(2, 0);
        Node n4 = new Node(3, 0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addEdge(new Edge(n1, n2, false));
        g.addEdge(new Edge(n1, n3, false));
        g.addEdge(new Edge(n1, n4, false));
        return g;
    }

    private Graph createCycle() {
        Graph g = new Graph("Cycle");
        Node n1 = new Node(0, 0);
        Node n2 = new Node(1, 0);
        Node n3 = new Node(2, 0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addEdge(new Edge(n1, n2, false));
        g.addEdge(new Edge(n2, n3, false));
        g.addEdge(new Edge(n3, n1, false));
        return g;
    }

    private Graph createCompleteGraph(int n) {
        Graph g = new Graph("K" + n);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, 0);
            g.addNode(nodes[i]);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                g.addEdge(new Edge(nodes[i], nodes[j], false));
            }
        }
        return g;
    }

    private Graph createPath(int n) {
        Graph g = new Graph("P" + n);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, 0);
            g.addNode(nodes[i]);
        }
        for (int i = 0; i < n - 1; i++) {
            g.addEdge(new Edge(nodes[i], nodes[i + 1], false));
        }
        return g;
    }
}

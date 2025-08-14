// === CS400 File Header Information ===
// Name: Ethan
// Email: ewobrien4@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referenced by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represented at the end of the shortest path that is found: it's cost is the cost of that
   * shortest path, and the nodes linked together through predecessor references represent all of
   * the nodes along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start or end node does not exist.");
    }

    //Queue of nodes to be explored
    PriorityQueue<SearchNode> candidates =
        new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));

    //Set to keep track of visited nodes
    Set<NodeType> visited = new HashSet<>();

    //Add start node with weight of 0
    candidates.add(new SearchNode(nodes.get(start), 0, null));

    while (!candidates.isEmpty()) {
      //Get  node with lowest cost
      SearchNode currentNode = candidates.poll();
      Node currentGraphNode = currentNode.node;

      //Skip node if already visited
      if (visited.contains(currentGraphNode.data)) {
        continue;
      }

      visited.add(currentGraphNode.data);

      //Return current node if the end has been reached
      if (currentGraphNode.data.equals(end)) {
        return currentNode;
      }

      for (Edge edge : currentGraphNode.edgesLeaving) {
        Node neighbor = edge.successor;
        double newCost = currentNode.cost + edge.data.doubleValue();

        //If  neighbor hasn't been visited yet, add to candidates list
        if (!visited.contains(neighbor.data)) {
          candidates.add(new SearchNode(neighbor, newCost, currentNode));
        }
      }
    }

    //NoSuchElementException if path isn't found
    throw new NoSuchElementException("No path found from start to end.");
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    //Get shortest path end node
    SearchNode endNode = computeShortestPath(start, end);

    List<NodeType> pathData = new ArrayList<>();

    //Work backwards to get path
    SearchNode currentNode = endNode;
    while (currentNode != null) {
      pathData.add(currentNode.node.data);
      currentNode = currentNode.predecessor;
    }

    // Reverse to get: start -> end
    Collections.reverse(pathData);

    return pathData;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    //Get shortest path end node
    SearchNode endNode = computeShortestPath(start, end);

    // Return cost of path
    return endNode.cost;
  }

  /**
   * Tests results from an in class example from A to E
   */
  @Test
  public void test1() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");

    graph.insertEdge("A", "E", 15);
    graph.insertEdge("A", "B", 4);
    graph.insertEdge("A", "C", 2);
    graph.insertEdge("B", "E", 10);
    graph.insertEdge("B", "D", 1);
    graph.insertEdge("C", "D", 5);
    graph.insertEdge("D", "E", 3);
    graph.insertEdge("D", "F", 0);
    graph.insertEdge("F", "D", 2);
    graph.insertEdge("F", "H", 4);
    graph.insertEdge("G", "H", 4);

    assertEquals(8, graph.shortestPathCost("A", "E"));
    List<String> expectedPath = List.of("A", "B", "D", "E");
    assertEquals(expectedPath, graph.shortestPathData("A", "E"));
  }

  /**
   * Tests results from an in class example from A to F
   */
  @Test
  public void test2() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");

    graph.insertEdge("A", "E", 15);
    graph.insertEdge("A", "B", 4);
    graph.insertEdge("A", "C", 2);
    graph.insertEdge("B", "E", 10);
    graph.insertEdge("B", "D", 1);
    graph.insertEdge("C", "D", 5);
    graph.insertEdge("D", "E", 3);
    graph.insertEdge("D", "F", 0);
    graph.insertEdge("F", "D", 2);
    graph.insertEdge("F", "H", 4);
    graph.insertEdge("G", "H", 4);

    assertEquals(5, graph.shortestPathCost("A", "F"));
    List<String> expectedPath = List.of("A", "B", "D", "F");
    assertEquals(expectedPath, graph.shortestPathData("A", "F"));
  }

  /**
   * Tests that a NoSuchElementException is thrown when a node is unreachable
   */
  @Test
  public void test3() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");

    graph.insertEdge("A", "E", 15);
    graph.insertEdge("A", "B", 4);
    graph.insertEdge("A", "C", 2);
    graph.insertEdge("B", "E", 10);
    graph.insertEdge("B", "D", 1);
    graph.insertEdge("C", "D", 5);
    graph.insertEdge("D", "E", 3);
    graph.insertEdge("D", "F", 0);
    graph.insertEdge("F", "D", 2);
    graph.insertEdge("F", "H", 4);
    graph.insertEdge("G", "H", 4);

    assertThrows(NoSuchElementException.class, () -> {
      graph.shortestPathCost("A", "G");
    });

    assertThrows(NoSuchElementException.class, () -> {
      graph.shortestPathData("A", "G");
    });
  }
}

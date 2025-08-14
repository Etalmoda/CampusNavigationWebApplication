import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class BackendTests {

  /**
   * Tests that the loadGraphData() method works correctly
   *
   * @throws IOException
   */
  @Test
  public void backendTest1() throws IOException {
    GraphADT<String, Double> graph = new Graph_Placeholder();
    Backend backend = new Backend(graph);

    backend.loadGraphData("campus.dot");
    assertTrue(graph.containsNode("Memorial Union"));
  }

  /**
   * Tests that the getListOfAllLocations() method works correctly
   */
  @Test
  public void backendTest2() {
    GraphADT<String, Double> graph = new Graph_Placeholder();
    Backend backend = new Backend(graph);

    List<String> locations = backend.getListOfAllLocations();

    assertTrue(locations.contains("Union South"));
    assertTrue(locations.contains("Computer Sciences and Statistics"));
    assertTrue(locations.contains("Weeks Hall for Geological Sciences"));
  }

  /**
   * Tests that the findLocationsOnShortestPath() method works correctly
   */
  @Test
  public void backendTest3() {
    GraphADT<String, Double> graph = new Graph_Placeholder();
    Backend backend = new Backend(graph);

    List<String> list =
        backend.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics");
    assertTrue(list.contains("Union South"));
    assertTrue(list.contains("Computer Sciences and Statistics"));
  }

  /**
   * Tests that the findTimesOnShortestPath() method works correctly
   */
  @Test
  public void backendTest4() {
    GraphADT<String, Double> graph = new Graph_Placeholder();
    Backend backend = new Backend(graph);

    List<Double> list =
        backend.findTimesOnShortestPath("Union South", "Computer Sciences and Statistics");

    assertTrue(list.contains(1.0));
    assertTrue(list.size() == 1);
  }

  /**
   * Tests that the getLongestLocationListFrom() method works correctly
   */
  @Test
  public void backendTest5() {
    GraphADT<String, Double> graph = new Graph_Placeholder();
    Backend backend = new Backend(graph);

    List<String> list = backend.getLongestLocationListFrom("Union South");

    assertTrue(list.contains("Union South"));
    assertTrue(list.contains("Computer Sciences and Statistics"));
    assertTrue(list.contains("Weeks Hall for Geological Sciences"));
    assertTrue((list.size() == 3), "size: " + list.size());
  }

  /**
   * Tests that the frontend correctly displays the shortest path between two connected locations
   */
  @Test
  public void integrationTest1() throws IOException {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);
    backend.loadGraphData("campus.dot");
    FrontendInterface frontend = new Frontend(backend);

    String html = frontend.generateShortestPathResponseHTML("Memorial Union", "Science Hall");

    // Assert that it mentions both endpoints
    assertTrue(html.contains("Memorial Union"));
    assertTrue(html.contains("Science Hall"));
  }

  /**
   * Tests that the system handles invalid location input properly
   */
  @Test
  public void integrationTest2() throws IOException {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);
    backend.loadGraphData("campus.dot");
    FrontendInterface frontend = new Frontend(backend);

    // Try to generate a path from a made up location
    String html = frontend.generateShortestPathResponseHTML("asdfasdf", "Memorial Union");

    assertTrue(html.toLowerCase().contains("error"));
  }

  /**
   * Tests that the frontend returns a longest location list from a valid node
   */
  @Test
  public void integrationTest3() throws IOException {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);
    backend.loadGraphData("campus.dot");
    FrontendInterface frontend = new Frontend(backend);

    String html = frontend.generateLongestLocationListFromResponseHTML("Memorial Union");

    assertTrue(html.contains("Memorial Union"));
    assertTrue(html.contains("<ul>") || html.contains("li"));
  }

  /**
   * Tests that the frontend correctly handles shortest path requests
   * where the start and end locations are the same
   */
  @Test
  public void integrationTest4() throws IOException {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);
    backend.loadGraphData("campus.dot");
    FrontendInterface frontend = new Frontend(backend);

    String html = frontend.generateShortestPathResponseHTML("Memorial Union", "Memorial Union");

    assertTrue(html.contains("Memorial Union"));
    assertTrue(html.contains("0.0"));
  }
}

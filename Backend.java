import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.NoSuchElementException;

/**
 * Backend - CS400 Project 2
 */
public class Backend implements BackendInterface {
  private GraphADT<String, Double> graph;

  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file.  If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filename));
      graph.getAllNodes().forEach(node -> graph.removeNode(node)); //Clears existing graph

      //Insert data into graph using enhanced for loop
      for (String line : lines) {
        line = line.trim();
        if (line.isEmpty() || !line.contains("->") || !line.contains("[seconds=")) {
          continue; // Skip invalid lines
        }

        // Extract node names
        String[] parts = line.split("->");
        String location1 = parts[0].trim().replaceAll("^\"|\"$", ""); // Removes quotes
        String[] rightParts = parts[1].trim().split("\\[seconds=");

        if (rightParts.length < 2)
          continue;

        String location2 = rightParts[0].trim().replaceAll("^\"|\"$", ""); // Removes quotes
        double time = Double.parseDouble(rightParts[1].replace("];", "").trim()); // Saves the time

        // Insert nodes and edge
        graph.insertNode(location1);
        graph.insertNode(location2);
        graph.insertEdge(location1, location2, time);
      }
    } catch (IOException e) {
      throw new IOException(
          "Error reading file: '" + filename + "'. Please check that the file path is correct.", e);
    }
  }

  /**
   * Returns a list of all locations (node data) available in the graph.
   *
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
    return new ArrayList<>(graph.getAllNodes());
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or an
   * empty list if no such path exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    try {
      // Attempt to retrieve the shortest path between the given locations
      return graph.shortestPathData(startLocation, endLocation);
    } catch (NoSuchElementException e) {
      // Check if the graph is missing the start or end location
      boolean startExists = graph.getAllNodes().contains(startLocation);
      boolean endExists = graph.getAllNodes().contains(endLocation);

      if (!startExists && !endExists) {
        System.err.println("Error: Both start and end locations are invalid.");
      } else if (!startExists) {
        System.err.println("Error: Start location '" + startLocation + "' does not exist.");
      } else if (!endExists) {
        System.err.println("Error: End location '" + endLocation + "' does not exist.");
      } else {
        System.err.println("Error: No path found between '" + startLocation + "' and '" + endLocation + "'.");
      }

      return new ArrayList<>();
    }
  }

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from
   * startLocation to endLocation, or an empty list of no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   * startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    // Check if the start location exists in the graph
    if (!graph.getAllNodes().contains(startLocation)) {
      System.err.println("Error: Start location '" + startLocation + "' does not exist.");
      return new ArrayList<>();
    }

    List<String> path = findLocationsOnShortestPath(startLocation, endLocation);
    List<Double> weights = new ArrayList<>();

    // Loop through each pair of adjacent locations on the path
    for (int i = 0; i < path.size() - 1; i++) {
      try {
        // Add the weight of the edge between the current location and the next to the weights list
        weights.add(graph.getEdge(path.get(i), path.get(i + 1))); //Try adding edge weight to list
      } catch (NoSuchElementException e) {
        // If the edge doesn't exist, return an empty list
        return new ArrayList<>();
      }
    }
    return weights;
  }

  /**
   * Returns the longest list of locations along any shortest path that starts from startLocation
   * and ends at any of the reachable destinations in the graph.
   *
   * @param startLocation the location to search through paths leaving from
   * @return the longest list of locations found on any shortest path that starts at the specified
   * startLocation.
   * @throws NoSuchElementException if startLocation does not exist, or if there are no other
   *                                locations that can be reached from there
   */
  @Override
  public List<String> getLongestLocationListFrom(String startLocation)
      throws NoSuchElementException {
    List<String> longestPath = new ArrayList<>();

    // Iterate over every node in the graph
    for (String destination : graph.getAllNodes()) {
      // Skip the case where the destination is the same as the starting point
      if (!destination.equals(startLocation)) {
        // Find the shortest path from the startLocation to the current destination
        List<String> path = findLocationsOnShortestPath(startLocation, destination);
        // If this path is longer than the current longest path, update longestPath
        if (path.size() > longestPath.size()) {
          longestPath = path;
        }
      }
    }
    if (longestPath.isEmpty()) {
      throw new NoSuchElementException("No reachable locations from " + startLocation);
    }
    return longestPath;
  }
}


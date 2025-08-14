// Olivia Nayak
// CS 400 - Florian Heimerl
// March 18, 2025

import java.util.List;

public class Frontend implements FrontendInterface{

    private BackendInterface backend;

    /**
    * Implementing classes should support the constructor below.
    * @param backend is used for shortest path computations
    */
    public Frontend(BackendInterface backend) {
        this.backend = backend;
        try {
            backend.loadGraphData("campus.dot");
        }
        catch (Exception e) {}
    }
    
    /**
    * Returns an HTML fragment that can be embedded within the body of a
    * larger html page.  This HTML output should include:
    * - a text input field with the id="start", for the start location
    * - a text input field with the id="end", for the destination
    * - a button labelled "Find Shortest Path" to request this computation
    * Ensure that these text fields are clearly labelled, so that the user
    * can understand how to use them.
    * @return an HTML string that contains input controls that the user can
    *         make use of to request a shortest path computation
    */
    @Override
    public String generateShortestPathPromptHTML() {
        // create the HTML fragment as a string
        String fragment = "";
        // add the input field with id="start" to the HTML fragment
        fragment += "<input id=\"start\" type=\"text\" placeholder=\"Enter the start location\"/>\n";
        // add the input field with id="end" to the HTML fragment
        fragment += "<input id=\"end\" type=\"text\" placeholder=\"Enter the end location\"/>\n";
        // add the button
        fragment += "<input type=\"button\" value=\"Find Shortest Path\"/>";
        return fragment;
    }
    
    /**
    * Returns an HTML fragment that can be embedded within the body of a
    * larger html page.  This HTML output should include:
    * - a paragraph (p) that describes the path's start and end locations
    * - an ordered list (ol) of locations along that shortest path
    * - a paragraph (p) that includes the total travel time along this path
    * Or if there is no such path, the HTML returned should instead indicate 
    * the kind of problem encountered.
    * @param start is the starting location to find a shortest path from
    * @param end is the destination that this shortest path should end at
    * @return an HTML string that describes the shortest path between these
    *         two locations
    */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        // create the HTML fragment as a string
        String fragment = "";
        fragment += "<p>The path starts at " + start + " and ends at " + end + "</p>\n";

        // get the locations on the shortest path from start to end
        List<String> locationsOnShortestPath = backend.findLocationsOnShortestPath(start, end);

        // check if a path exists for that start and end combination
        if (locationsOnShortestPath.size() == 0) {
            List<String> allLocations = backend.getListOfAllLocations();
            boolean invalidStart = false;
            // check if the start is in the list of locations
            if (!allLocations.contains(start)) {
                fragment += "<p>Error: Invalid start location. That start location does not exist.</p>";
                invalidStart = true;
            }
            // check if the end is in the list of locations
            if (!allLocations.contains(end)) {
                // if the start was also invalid, add a newline character so that the error messages are on two different lines
                if (invalidStart) {
                    fragment += "\n"; // EDIT: instead of adding the whole string, just add the newline character
                }
                fragment += "<p>Error: Invalid end location. That end location does not exist.</p>";
            }
            // check if the start and end locations exist, but there's no way to travel between them
            if (allLocations.contains(start) && allLocations.contains(end)) {
                fragment += "<p>Error: There is no such path between " + start + " and " + end + ".</p>"; // EDIT: specified the start and end in the error message
            }
            // fragment += "<p>Error: There is no such path</p>";
            return fragment;
        }

        // create the ordered list
        fragment += "<ol>\n";

        // loop through all the locations and add them to fragment
        for (String location : locationsOnShortestPath) {
            fragment += "\t<li>" + location + "</li>\n";
        }

        // close the ordered list 
        fragment += "</ol>\n";

        // get the times between every two nodes on the shortest path
        List<Double> travelTimes = backend.findTimesOnShortestPath(start, end);
        double totalTime = 0.0;
        // loop through the travel times and add them up
        for (double time : travelTimes) {
            totalTime += time;
        }
        // add the total travel time to the fragment
        fragment += "<p>The total travel time along this path is " + String.valueOf(totalTime) + " seconds</p>";

        return fragment;
    }

    /**
    * Returns an HTML fragment that can be embedded within the body of a
    * larger html page.  This HTML output should include:
    * - a text input field with the id="from", for the start location
    * - a button labelled "Longest Location List From" to submit this request
    * Ensure that this text field is clearly labelled, so that the user
    * can understand how to use it.
    * @return an HTML string that contains input controls that the user can
    *         make use of to request a longest location list calculation
    */
    @Override
    public String generateLongestLocationListFromPromptHTML() {
        // create the HTML fragment as a string
        String fragment = "";
        // add the input field with id="from" to the HTML fragment
        fragment += "<input id=\"from\" type=\"text\" placeholder=\"Enter the start location\"/>\n";
        // add the button
        fragment += "<input type=\"button\" value=\"Longest Location List From\"/>"; // EDIT: fixed a typo where there was an extra apostrophe
        return fragment;
    }
    
    /**
    * Returns an HTML fragment that can be embedded within the body of a
    * larger html page.  This HTML output should include:
    * - a paragraph (p) that describes the path's start and end locations
    * - an ordered list (ol) of locations along that shortest path
    * - a paragraph (p) that includes the total number of locations on path
    * Or if no such path can be found, the HTML returned should instead
    * indicate the kind of problem encountered.
    * @param start is the starting location to find the longest list from
    * @return an HTML string that describes the longest list of locations 
    *        along a shortest path starting from the specified location
    */
    @Override
    public String generateLongestLocationListFromResponseHTML(String start) {
        // create the HTML fragment as a string
        String fragment = "";
        try {
            // get the locations along any shortest path that starts from startLocation and ends at any of the 
            // reachable destinations in the graph.
            List<String> locationList = backend.getLongestLocationListFrom(start);

            // get the ending location
            String end = locationList.get(locationList.size()-1);

            fragment += "<p>The path starts at " + start + " and ends at " + end + "</p>\n";

            // create the ordered list 
            fragment += "<ol>\n";

            // loop through all the locations and add them to fragment
            for (String location : locationList) {
                fragment += "\t<li>" + location + "</li>\n";
            }

            // close the ordered list
            fragment += "</ol>\n";

            // EDIT: add in the total number of locations on the path and remove the total time on the path 
            // add the total number of locations on the path
            fragment += "<p>The total number of locations on this path is " + String.valueOf(locationList.size()) + "</p>";

            // // get the times between every two nodes on the shortest path
            // List<Double> travelTimes = backend.findTimesOnShortestPath(start, end);
            // double totalTime = 0.0;
            // // loop through the travel times and add them up
            // for (double time : travelTimes) {
            //     totalTime += time;
            // }
            // // add the total travel time to the fragment
            // fragment += "<p>The total travel time along this path is " + String.valueOf(totalTime) + " seconds</p>";

            return fragment;
        }
        catch (Exception e) {
            List<String> allLocations = backend.getListOfAllLocations();
            // check if the start is in the list of locations
            if (!allLocations.contains(start)) {
                fragment += "<p>Error: Invalid start location. That start location does not exist.</p>";
            }
            else {
                // the start exists, but no paths were found
                fragment += "<p>Error: No paths found</p>";
            }
            return fragment;
        }
    }
 
}

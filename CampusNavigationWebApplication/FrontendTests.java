// Olivia Nayak
// CS 400 - Florian Heimerl
// March 20, 2025

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class FrontendTests {

    /**
     * tests generateShortestPathPromptHTML()
     */
    @Test
    public void frontendTest1() {
        Frontend frontend = new Frontend(new Backend_Placeholder(new Graph_Placeholder()));
        // get the html prompt
        String actualOutput = frontend.generateShortestPathPromptHTML();
        // create the string for the expected prompt
        String expectedOutput = "<input id=\"start\" type=\"text\" placeholder=\"Enter the start location\"/>\n" + 
                                "<input id=\"end\" type=\"text\" placeholder=\"Enter the end location\"/>\n" + 
                                "<input type=\"button\" value=\"Find Shortest Path\"/>";

        // check if the output is correct
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * tests generateShortestPathResponseHTML() with a start and end that have a valid path
     */
    @Test
    public void frontendTest2() {
        Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
        try {
            // backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            // get the html prompt
            String actualOutput = frontend.generateShortestPathResponseHTML("Union South", "Weeks Hall for Geological Sciences");
            // create the string for the expected prompt
            String expectedOutput = "<p>The path starts at Union South and ends at Weeks Hall for Geological Sciences</p>\n" + 
                                    "<ol>\n" +
                                    "\t<li>Union South</li>\n" + 
                                    "\t<li>Computer Sciences and Statistics</li>\n" + 
                                    "\t<li>Weeks Hall for Geological Sciences</li>\n" + 
                                    "</ol>\n" + 
                                    "<p>The total travel time along this path is 6.0 seconds</p>";
            
            // check if the output is correct
            Assertions.assertEquals(expectedOutput, actualOutput);
        }
        catch (Exception e) {
            // threw an unexpected exception
            Assertions.assertTrue(false);
        }
    }

    /**
     * tests generateShortestPathResponseHTML() with a start and end that don't have a valid path - start doesn't exist
     */
    @Test
    public void frontendTest3() {
        Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
        try {
            // backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            // get the html prompt
            String actualOutput = frontend.generateShortestPathResponseHTML("Brogden Psychology", "Union South");
            // create the string for the expected prompt
            String expectedOutput = "<p>The path starts at Brogden Psychology and ends at Union South</p>\n" + 
                                    "<p>Error: Invalid start location. That start location does not exist.</p>";
            
            // check if the output is correct
            Assertions.assertEquals(expectedOutput, actualOutput);
        }
        catch (Exception e) {
            // threw an unexpected exception
            Assertions.assertTrue(false);
        }
    }

    /**
     * tests generateLongestLocationListFromPromptHTML()
     */
    @Test
    public void frontendTest4() {
        Frontend frontend = new Frontend(new Backend_Placeholder(new Graph_Placeholder()));
        // get the html prompt
        String actualOutput = frontend.generateLongestLocationListFromPromptHTML();
        // create the string for the expected prompt
        String expectedOutput = "<input id=\"from\" type=\"text\" placeholder=\"Enter the start location\"/>\n" +
                                "<input type'\"button\" value=\"Longest Location List From\"/>";

        // check if the output is correct
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * tests generateLongestLocationListFromResponseHTML() with a valid start
     */
    @Test
    public void frontendTest5() {
        Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
        try {
            // backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            // get the html prompt
            String actualOutput = frontend.generateLongestLocationListFromResponseHTML("Computer Sciences and Statistics");
            // create the string for the expected prompt
            String expectedOutput = "<p>The path starts at Computer Sciences and Statistics and ends at Mosse Humanities Building</p>\n" + 
                                    "<ol>\n" + 
                                    "\t<li>Computer Sciences and Statistics</li>\n" + 
                                    "\t<li>Weeks Hall for Geological Sciences</li>\n" + 
                                    "\t<li>Mosse Humanities Building</li>\n" + 
                                    "</ol>\n" + 
                                    "<p>The total travel time along this path is 6.0 seconds</p>";

            // check if the output is correct
            Assertions.assertEquals(expectedOutput, actualOutput);
        }
        catch (Exception e) {
            // threw an unexpected exception
            Assertions.assertTrue(false);
        }
    }

    /**
     * tests generateLongestLocationListFromResponseHTML() with an invalid start
     */
    @Test
    public void frontendTest6() {
        Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
        try {
            // backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            // get the html prompt
            String actualOutput = frontend.generateLongestLocationListFromResponseHTML("Brogden Psychology");
            // create the string for the expected prompt
            String expectedOutput = "<p>Error: Invalid start location. That start location does not exist.</p>";

            // check if the output is correct
            Assertions.assertEquals(expectedOutput, actualOutput);
        }
        catch (Exception e) {
            // threw an unexpected exception
            Assertions.assertTrue(false);
        }
    }
}

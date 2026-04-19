import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GraphTesting{
    Graph g = new Graph();
    /*
     * Stream for the parameterized tests with blank starting graphs 
     */
    public static Stream<Arguments> addNodeStartingConditions(){
        return Stream.of(
            Arguments.of("", false),
            Arguments.of("Hamilton", true),
            Arguments.of("hamilton", true),
            Arguments.of("chfesjewfvudfhbtwefsdgdsfhgndsfnvjkdsvjueirtgjuifhrg854fhds3nbggdojgeruigeri43thfdsegunweurngurenugnuirebuirhuifhueh9wh9hew9fh948h9843h8h48fh43hg98h384hg894h38g9h438gh438hg843hg843hg8h348h348hg9843h9843hg8934hg83h48gh348gh438hg483hg834h8gheruihgurhfujwejfouiwheuifhuwiehwuiehfuiwehfuihweuifhewuifhwuiehfuiewhfuiewhfuiwehfuiehfuihewuifhweuifhwieu", false),
            Arguments.of(null, false),
            Arguments.of("Hamilton, Auckland", false)
        );
    }
    /*Stream for the parameterized tests with non-blank starting graphs */
    public static Stream<Arguments> twoCityPlusTypeFormatStartingConditions(){
        return Stream.of(
            //Format: Starting city, Ending city, Connection type, if it should be parseable in a medium graph, if it should be parseable in a large graph, if a connection should exist between them, if it's direct
            Arguments.of("Hamilton0", "Hamilton2" , "Bus", true, true, true, false),
            Arguments.of("Hamilton1", "Hamilton5","Train", true, true, false, false),
            Arguments.of("Hamilton0","Hamilton11", "Train", true, true, true, true),
            Arguments.of("hamilton0", "hamilton2", "Bus", true, true, true, false),
            Arguments.of("Hamilton0", "Hamilton6", "Plane" ,true, true, true, false),
            Arguments.of("Hamilton0", "Hamilton1199", "Bus", false, true, true, true),
            Arguments.of("Hamilton0", "Hamilton599", "Train", false, true, false, false),
            Arguments.of("wrong", "invalid", "Bus", false, false, false, false),
            Arguments.of("Hamilton0", "Hamilton599", "Plane", false, true, true, false),
            Arguments.of("Hamikton0", "Hami;ton6", "Bvus", false, false, false, false),
            Arguments.of("Hamikton0", "Hami;ton6", "Bus", false, false, false, false)
        );
    }

    /*Tests in this class all use the same empty graph method at the start */
    class emptyGraphTests {
        /* Resets the graph between tests */
        @BeforeEach
        public void graphSetup(){
            g = new Graph();
        }
        /*Before addNode's ability to return true and false is tested, test if it runs without errors */
        @DisplayName("Test if the addNode method throws exceptions for a range of possible inputs")
        @ParameterizedTest
        @MethodSource("addNodeStartingConditions")
        public void testAddNodeDoesntThrow(String s, boolean expectedResult){
            Assertions.assertDoesNotThrow(() -> g.addNode(s));
        }
        /*
         * Test multiple cases of nodes being added to a graph.
         * Blank strings should return false, capitalised and uncapitalised forms should both be accepted (though maybe something should be done 
         * to count them as the same, can be tested elsewhere) extremely long strings are likely errors and should return false. null strings should return false. 
         * inputs that contain characters that aren't allowed (such as commas which aren't allowed for CSV file compatibility) should return false an exception. 
         * Note: this test may fail because an exception was thrown. to tell whether an exception was thrown or it returned an incorrect input, see testAddNodeDoesntThrow
         */
        @DisplayName("Test if the addNode method returns the right response to a range of possible inputs")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0}")
        @MethodSource("addNodeStartingConditions")
        public void testAddNode(String s, boolean expectedResult){
            Boolean actual = true;
            actual = g.addNode(s);
            Assertions.assertEquals(expectedResult, actual);
        }
            /*Before removeNode's ability to return true and false is tested, test if it runs without exceptions */
        @DisplayName("Test if the addNode method throws exceptions for a range of possible inputs")
        @ParameterizedTest
        @MethodSource("addNodeStartingConditions")
        public void testRemoveNodeDoesntThrow(String s, boolean expectedResult){
            Assertions.assertDoesNotThrow(() -> g.addNode(s));
        }
        /*
         * Test multiple cases of nodes being removed from a graph for if they return the right values. see testAddNode for the full description of which cases should return false. 
         * see testRemoveNodeDoesntThrow for if certain cases failed because they threw exceptions, not beause they returned the wrong value
         * @see testAddNode
         * @see testRemoveNodeDoesntThrow
         */
        @DisplayName("Test if the removeNode method returns the right response to a range of possible inputs")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0}")
        @MethodSource("addNodeStartingConditions")
        public void testRemoveNode(String s, boolean expectedResult){
            Boolean actual = true;
            g.addNode(s);
            actual = g.removeNode(s);
            Assertions.assertEquals(expectedResult, actual);
        }
        /*Tests if removeNode will throw an exception if an empty graph has a valid node attempted to be removed */
        @DisplayName("Test if removeNode throws an exception if something valid is removed from an empty graph")
        @Test
        public void testEmptyGraphRemoveNodeException(){
            Assertions.assertDoesNotThrow(() -> g.removeNode("Hamilton"));
        }
        /*Tests if removeNode returns false after a valid string is inputted to an empty graph, which it should */
        @DisplayName("Test if removeNode returns false after a valid string is inputted to an empty graph")
        @Test
        public void testEmptyGraphRemoveNodeReturn(){
            Boolean actual = g.removeNode("Hamilton");
            Assertions.assertEquals(false, actual);
        }
    }
    /*Tests in this class use a moderately complex graph as a typical use case */
    class moderateComplexityGraphTests{
        /* Resets the graph between tests (moderate size) */
        @BeforeEach
        public void graphSetup(){
            g = new Graph();
            for(int i = 0; i < 12; i++){
                g.addNode("Hamilton" + i);
            }
            for(int i = 0; i < 12; i++){
                int j = (12-i) - 1;
                //create pseudorandom values to connect the cities at. Cities in the middle of the arraylist will take a long time to traverse to
                g.addEdge(g.Nodes.get(i).location,g.Nodes.get(j).location,"Bus");
                g.addEdge(g.Nodes.get((i+1)%11).location,g.Nodes.get(j).location,"Bus");
                g.addEdge(g.Nodes.get(i).location,g.Nodes,get(j).location,"Train");
                g.addEdge(g.Nodes.get((i+1)%11).location,g.Nodes.get(j).location,"Train");
                //by plane each city will be connected both ways to exactly one other and not be able to move to any others
                g.addEdge(g.Nodes.get(i).location,g.Nodes.get(j).location,"Plane");
            }
        }
        /*parameterized test for if addEdge throws exceptions. throwing exceptions is always wrong */

        @DisplayName("Test if addEdge runs without throwing an exception over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void addEdgeThrowsExceptionMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.addEdge(s1, s2, type));
        }

        /*parameterized test for if addEdge returns the right values */

        @DisplayName("Test if addEdge returns the right value over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void addEdgeReturnsTrueFalseMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect,Boolean direct){
            actual = g.addEdge(s1, s2, type);
            Assertions.assertEquals(mediumValid, actual);
        }
        /*parameterized test for if removeEdge throws exceptions. throwing exceptions is always wrong */
        @DisplayName("Test if removeEdge runs without throwing an exception over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void removeEdgeThrowsExceptionMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.removeEdge(s1, s2, type));
        }
        /*parameterized test for if removeEdge returns the right values */
        @DisplayName("Test if removeEdge returns the right value over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void removeEdgeReturnsTrueFalseMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            actual = g.removeEdge(s1, s2, type);
            Assertions.assertEquals(mediumValid, actual);
        }
        /*parameterised test for if hasEdge throws exceptions. Throwing an exception is always wrong */
        @DisplayName("Test if hasEdge runs without throwing an exception over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void hasEdgeThrowsExceptionMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.hasEdge(s1, s2, type));
        }
        /*parameterized test for if hasEdge returns the right values */
        @DisplayName("Test if hasEdge returns the right value over a range of values (medium graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}, {6}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void hasEdgeReturnsTrueFalseMedium(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            actual = g.hasEdge(s1, s2, type);
            Assertions.assertEquals(direct, actual);
        }

        /*Test for if getEdgesOfType works correctly on a medium sized graph */
        @Test
        @DisplayName("Test if getEdgesofType returns the right string for a medium size graph")
        public void testGetEdgesByType(){
            String expected = "(Hamilton0, Hamilton11)"+System.lineSeparator()+"(Hamilton1, Hamilton10)"+System.lineSeparator()+"(Hamilton1, Hamilton11)"+System.lineSeparator()+"(Hamilton2, Hamilton9)"+System.lineSeparator()+"(Hamilton2, Hamilton10)"+System.lineSeparator()+"(Hamilton3, Hamilton8)"+System.lineSeparator()+"(Hamilton3, Hamilton9)"+System.lineSeparator()+"(Hamilton4, Hamilton7)"+System.lineSeparator()+"(Hamilton4, Hamilton8)"+System.lineSeparator()+"(Hamilton5, Hamilton6)"+System.lineSeparator()+"(Hamilton5, Hamilton7)";
            String actual = g.getEdgesOfType("Bus");
            Assertions.assertEquals(expected, actual);
        }
    }



    /*Tests in this class use an ultra short graph as an edge case */
    class smallGraphTests{
        /* Resets the graph between tests (small size) (adds a case with two nodes for tests where more than exactly one is desirable) */
        @BeforeEach
        public void graphSetup(){
            g = new Graph();
            g.addNode("Hamilton");
            Graph g2 = g;
            g2.addNode("Auckland");
            g2.addEdge("Hamilton", "Auckland", "Plane");    
        }

        @Test
        @DisplayName("Test if edges can be constructed from a node to itself (they shouldn't be allowed to)")
        public void testSelfEdge(){
            expected = false;
            actual = g.addEdge("Hamilton", "Hamilton","Bus");
            Assertions.assertEquals(expected, actual);
        }





    }
    /*Tests in this class use a very large graph as an edge case */
    class veryLargeGraphTests{
        /* Resets the graph between tests (very large size) */
        @BeforeEach
        public void graphSetup(){
            g = new Graph();
            for(int i = 0; i < 12; i++){
                g.addNode("Hamilton" + i);
            }
            for(int i = 0; i < 12; i++){
                int j = (12-i) - 1;
                //create pseudorandom values to connect the cities at. Cities in the middle of the arraylist will take a long time to traverse to
                g.addEdge(g.Nodes.get(i).location,g.Nodes.get(j).location,"Bus");
                g.addEdge(g.Nodes.get((i+1)%1199).location,g.Nodes.get(j).location,"Bus");
                g.addEdge(g.Nodes.get(i).location,g.Nodes,get(j).location,"Train");
                g.addEdge(g.Nodes.get((i+1)%1199).location,g.Nodes.get(j).location,"Train");
                //by plane each city will be connected both ways to exactly one other and not be able to move to any others
                g.addEdge(g.Nodes.get(i).location,g.Nodes.get(j).location,"Plane");
            }
        }
        @DisplayName("Test if addEdge runs without throwing an exception over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void addEdgeThrowsExceptionLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.addEdge(s1, s2, type));
        
        }
        @DisplayName("Test if addEdge returns the right value over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}, {4}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void addEdgeReturnsTrueFalseLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Boolean actual = g.addEdge(s1, s2, type);
            Assertions.assertEquals(largeValid, actual);
        }

        @DisplayName("Test if removeEdge runs without throwing an exception over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void removeEdgeThrowsExceptionLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.removeEdge(s1, s2, type));
        
        }
        @DisplayName("Test if removeEdge returns the right value over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}, {4}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void removeEdgeReturnsTrueFalseLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Boolean actual = g.removeEdge(s1, s2, type);
            Assertions.assertEquals(largeValid, actual);
        }

        @DisplayName("Test if has Edge runs without throwing an exception over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void hasEdgeThrowsExceptionLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Assertions.assertDoesNotThrow(() -> g.hasEdge(s1, s2, type));
        
        }
        @DisplayName("Test if hasEdge returns the right value over a range of values (large graph)")
        @ParameterizedTest(name = "Test with condition #{index} - value: {0} , {1} , {2}, {6}")
        @MethodSource("twoCityPlusTypeFormatStartingConditions")
        public void hasEdgeReturnsTrueFalseLarge(String s1, String s2, String type, Boolean mediumValid, Boolean largeValid, Boolean connect, Boolean direct){
            Boolean actual = g.hasEdge(s1, s2, type);
            Assertions.assertEquals(direct, actual);
        }


        @DisplayName("Test if getEdgesOfType works properly in very large graphs")
        @Test
        public void testGetEdgesByTypeLarge(){
            //since this is working with such a large output, we'll test if it matches the expected start and beginning
            String expectedStart = "(Hamilton0, Hamilton1199)";
            String expectedEnd = "(Hamilton599, Hamilton600)";
            String actualStart = g.getEdgesOfType("Plane").substring(0,25 );
            String actualEnd = (g.getEdgesOfType("Plane").length() >= 26) ? str.substring(str.length() - 26) : g.getEdgesOfType("Plane");
        }
    }







    /*
     * Set of tests utilising the same beforeeach method made to be more easily understandable than the other graph tests
     */
    class traversingTests{
        private Graph graph;

        @BeforeEach
        public void setUp() {
            graph = new Graph();
            // Setup a sample graph with multiple nodes and edges
            graph.addNode("Auckland");
            graph.addNode("Christchurch");
            graph.addNode("Wellington");
            graph.addNode("Invercargill");
            // Add edges: type can be "Road", "Plane", "Rail"
            graph.addEdge("Auckland", "Christchurch", "Plane");
            graph.addEdge("Auckland", "Wellington", "Plane");
            graph.addEdge("Christchurch", "Wellington", "Rail");
            graph.addEdge("Wellington", "Invercargill", "Road");
        }
        /*
         * Tests ticketTraverse in a scenario with no tickets
         */
        @Test
        public void testTicketTraverse_NoTickets() {
            Set<String> reachable = graph.ticketTraverse("Auckland", 0, 0, 0);
            Assertions.assertTrue(reachable.isEmpty(), "No tickets should result in no reachable cities");
        }
        /*Tests ticketTraverse in a scenario with one of all tickets */
        @Test
        public void testTicketTraverse_AllTicketsOneHop() {
            Set<String> reachable = graph.ticketTraverse("Auckland", 1, 1, 1);
            Assertions.assertTrue(reachable.contains("Auckland"));
            Assertions.assertTrue(reachable.contains("Christchurch"));
            Assertions.assertTrue(reachable.contains("Wellington"));
            // Invercargill should be unreachable with only one ticket per mode
            Assertions.assertFalse(reachable.contains("Invercargill"));
        }
        /*Tests ticketTraverse in a scenario with two of all tickets */
        @Test
        public void testTicketTraverse_MultipleHops() {
            Set<String> reachable = graph.ticketTraverse("Auckland", 2, 2, 2);
            Assertions.assertTrue(reachable.contains("Invercargill"), "Should reach Invercargill with enough tickets");
        }
        /*Tests ticketTraverse in a scenario with one of all tickets when the starting node isn't actually in the graph */
        @Test
        public void testTicketTraverse_StartNodeNotInGraph() {
            Set<String> reachable = graph.ticketTraverse("NonExistentCity", 1, 1, 1);
            Assertions.assertTrue(reachable.isEmpty(), "Starting from a non-existent city should yield an empty set");
        }
        /*Tests ticketTraverse in a scenario with one of all tickets exceptplane tickets */
        @Test
        public void testTicketTraverse_ExhaustTickets() {
            Set<String> reachable = graph.ticketTraverse("Wellington", 1, 0, 1); 
            Assertions.assertTrue(reachable.contains("Wellington"));
            Assertions.assertTrue(reachable.contains("Invercargill"));
            Assertions.assertFalse(reachable.contains("Auckland")); 
        }
    }
}
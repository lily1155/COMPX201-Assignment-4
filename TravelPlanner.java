import java.util.*;
import java.io.*;

public class TravelPlanner {
    private static Graph graph = new Graph();

    /*
     * Main method first loads a graph from a file, then lets users select from various options for interacting with the graph. once loaded from a file, new nodes cannot be added to the graph
     * as per the assignment not mentioning any way for the user to do this from this class
     * @param String[] args, The first argument of this method should specify a filename for a csv file to create a graph from.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Load graph from file (assuming filename is provided as argument)
        if (args.length > 0) {
            if(!loadGraphFromFile(args[0])){
                System.out.println("Since fileload failed, the program will now shut down");
                return;
            }
        } else {
            System.out.println("No valid file was provided");
            return;
        }

        boolean exit = false;
        //while loop lets users choose multiple options until they decide to exit the program
        while (!exit) {
            printMenu();
            int choice = getIntInput(scanner, "Enter your choice: ");
            switch (choice) {
                case 1:
                    addEdgeOption(scanner);
                    break;
                case 2:
                    removeEdgeOption(scanner);
                    break;
                case 3:
                    searchEdgeOption(scanner);
                    break;
                case 4:
                    printEdgesOfTypeOption(scanner);
                    break;
                case 5:
                    performTravelOption(scanner);
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    exit = true; 
                    break;
                default: 
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    /*
     * Loads a graph from a csv file containing edge information. nodes are formed from the start and end location of edges, and then the edge itself is added
     * This method is private since it's only use is as called by the main method. testing classes should not need to interact with this method.
     * its efficacy can be indirectly tested through if it throws exceptions
     * @param String filename, File will be read from this path. can be relative or absolute.
     * @return Returns a boolean value for whether or not it succeeds at loading a graph
     */
    private static boolean loadGraphFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.startsWith("From")){
                    String[] node = line.split(",");
                    //now check if the nodes included are present
                    for(int i = 0; i<=1; i++){
                        boolean isPresent = false;
                        for(Node n : graph.Nodes){
                            if(node[i]== n.location){
                                isPresent = true;
                            }
                        }
                        if (isPresent = false){
                            graph.Nodes.add(new Node(node[1]));
                        }
                    }//now both nodes have been added if they weren't already there, so adding the edge itself will work
                    graph.addEdge(node[0], node[1], node[2]);
                }
            }
            System.out.println("Graph loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
    }

    /*
     * Prints a menu to the console with 6 options relating to all user functions available in this class. This code is reused so it gets a method for ease of implementation.
     * This method is private since it's only use is as called by the main method. testing classes should not need to interact with this method
     */
    private static void printMenu() {
        System.out.println("\n--- Travel Planner Menu ---");
        System.out.println("1. Add a new edge");
        System.out.println("2. Remove an edge");
        System.out.println("3. Search for an edge");
        System.out.println("4. Print edges of a specific type");
        System.out.println("5. Find reachable cities with tickets");
        System.out.println("6. Exit");
    }
    /*
     * Adds an Edge. Asks user for a start and end city and a type, and finally either makes the edge or tells the user it was invalid. retries until a valid edge is formed
     * @param Scanner scanner, scanner object takes user input for the start and end cities plus edge type to add
     */
    public static void addEdgeOption(Scanner scanner) {
        boolean success = false;
        while(!success){
            System.out.print("Enter start city: ");
            String s1 = scanner.nextLine();
            System.out.print("Enter end city: ");
            String s2 = scanner.nextLine();
            System.out.print("Enter edge type (Road, Rail, Plane): ");
            String type = scanner.nextLine();
            if (s1.isEmpty() || s2.isEmpty() || type.isEmpty()) {
                System.out.println("Invalid input. Fields cannot be empty.");  
            }
            else{
                try {
                    graph.addNode(s1);
                    graph.addNode(s2);
                    graph.addEdge(s1, s2, type);
                    System.out.println("Edge added successfully.");
                    success = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    /*
     * Removes an Edge. Asks user for a start and end city and a type, and finally either removes the edge or tells the user it was invalid. retries until a valid edge is inputted
     * @param Scanner scanner, scanner object takes user input for the start and end cities plus edge type to remove
     */
    private static void removeEdgeOption(Scanner scanner) {
        boolean success = false;
        while(!success){
            System.out.print("Enter start city: ");
            String s1 = scanner.nextLine();
            System.out.print("Enter end city: ");
            String s2 = scanner.nextLine();
            System.out.print("Enter edge type: ");
            String type = scanner.nextLine();

            if (s1.isEmpty() || s2.isEmpty() || type.isEmpty()) {
                System.out.println("Invalid input. Fields cannot be empty.");
            }else{
                try {
                    graph.removeEdge(s1, s2, type);
                    System.out.println("Edge removed if it existed.");
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
            }
        }
    }
    /*
     * Searches for an Edge. Asks user for a start and end city and a type, and finally either searches for the edge or tells the user it was invalid. retries until a valid edge is inputted
     * Prints to console if the edge was found or not
     * @param Scanner scanner, scanner object takes user input for the start and end cities plus edge type to look for
     */
    private static void searchEdgeOption(Scanner scanner) {
        boolean success = false;
        while(!success){
            System.out.print("Enter start city: ");
            String s1 = scanner.nextLine();
            System.out.print("Enter end city: ");
            String s2 = scanner.nextLine();
            System.out.print("Enter edge type: ");
            String type = scanner.nextLine();
            
            if (s1.isEmpty() || s2.isEmpty() || type.isEmpty()) {
                System.out.println("Invalid input. Fields cannot be empty.");
            }else{
                try {
                    boolean exists = graph.hasEdge(s1, s2, type);
                    System.out.println("Edge exists: " + exists);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /*
     * Asks user for an edge type, searches for all edges of that type and displays them in the console
     * @param Scanner scanner, scanner object for getting user inputs on what edge type to look for
     */
    private static void printEdgesOfTypeOption(Scanner scanner) {
        System.out.print("Enter edge type to display: ");
        String type = scanner.nextLine();
        String edges = graph.getEdgesOfType(type);
        System.out.println("Edges of type " + type + ": " + edges);
    }
    /*
     * method for looking through all travel options given a user inputted city and number of tickets of each type
     * @param Scanner scanner, the scanner object that will take in the users responses for the start city, and will be passed to getIntInput to get the number of tickets of each type
     */
    private static void performTravelOption(Scanner scanner) {
        System.out.print("Enter start city: ");
        String startCity = scanner.nextLine();
        int busTickets = getIntInput(scanner, "Enter number of bus tickets: ");
        int planeTickets = getIntInput(scanner, "Enter number of plane tickets: ");
        int trainTickets = getIntInput(scanner, "Enter number of train tickets: ");
        String reachable = graph.ticketTraverse(startCity, busTickets, planeTickets, trainTickets);
        System.out.println("Reachable cities: " + reachable);
    }
    /*
     * This method automates repeated functions in performTravelOption. using a scanner and string it asks for a number, informs the user if they make an error and lets them try again until a valid input is given
     * @param String prompt, the text given to the user describing what the number they're giving will be for
     * @param Scanner scanner, the scanner object that will store the users response
     */
    private static int getIntInput(Scanner scanner, String prompt) {
        int value = -1;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Please enter a non-negative integer.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
        return value;
    }
}
import java.util.ArrayList;
public class Graph{
    ArrayList<Node> Nodes;
    public Graph(){
        Nodes = new ArrayList<Node>();
    }

    /*
     * findNode returns true if a node is already there. it works both if a node or string is passed due to having two similar implementations. A private method needed only for convenience within other methods
     * @param Node f, the node that's being searched for
     * @return Returns true if the node is found and false otherwise
     */
    private boolean findNode(Node f){
        for(Node n : Nodes){
            if(n == f) return true;
        }
        return false;
    }
    /*
     * findNode returns true if a node is already there. it works both if a node or string is passed due to having two similar implementations. A private method needed only for convenience within other methods
     * @param String s, the location contained by the Node that's being searched for
     * @return Returns true if the node is found and false otherwise
     */
    private boolean findNode(String s){
        for(Node n : Nodes){
            if (n.location ==s) return true;
        }
        return false;
    }

    /*
     * Returns a node given a string. A private method needed only for convenience within other methods
     * @param String s, the location that the node should contain
     * @return Returns the node that contains the given string
     */
    private Node getNode(String s){
        for (Node n : Nodes){
            if(n.location == s) return n;
        }
        return null;
    }
    /*
     * Used within other methods to check if the type is correct. Since this code would otherwise be often repeated it gets its own private method. Outputs error message to console if the type isn't the right format
     * @param String s, the string a method was passed by the user telling that method which type of connection the edge should be
     * @return Returns true if the string is of an accepted type and false by default
     */
    Boolean typeIsCorrect(String s){
        if(s == "Bus" || s == "Train" || s == "Plane") return true;
        System.out.println(s + " is not an accepted connection type");
        return false;
    }
    /*
     * Checks the validity of a string containing a location. more than one method needs this code to run seamlessly
     * @return Returns true if the string is valid, false otherwise
     */
    private Boolean locationIsValid(String s){
        if (s == null)return false;
        if(s.length()> 85) {
            System.out.println("Please double check the input, the given string was longer than any real place");
            return false;
        }
        if(s.contains(",")) {
            System.out.println("Invalid character detected. Please do not use commas in location names");
            return false;
        }
        return true;
    }

    /*
     * Adds a node with a certain location. Two if statements check that the location is valid and doesn't already exist first
     * @param String s, A String containing the location the new node should contain
     * @return Returns true if the method worked fine or false if an error was handled. for testing purposes
     * Additionally very long inputs or inputs containing comma's are assumed to be errors
     */
    public Boolean addNode(String s){
        if(!locationIsValid(s)) return false;
        if (findNode(s)) return false;
        Nodes.add(new Node(s));
        return true;
    }
    /*
     * Removes a node and all edges containing it given a string. first it's tested if the string is null, then if not the node being removed is found. all connections to the node get severed and finally the node
     * itself gets removed
     * @param String s, This string contains the location that the node would have in it.
     */
    public Boolean removeNode(String s){
        if (!locationIsValid(s)) return false; 
        Node removed = getNode(s);
        for(Node n: Nodes){
            n.removeEdge(removed);
        }
        Nodes.remove(removed);
        return true;
    }
    
    /*
     * //This method adds an edge between two nodes of a specified type. it returns a boolean to inform whatever calls it if it succeeded or failed for testing purposes
     * @param String s1, the starting location of the edge
     * @param String s2, the finishing location of the edge
     * @param String type, the type of connection this edge forms
     * @return Returns true if the edge was successfully formed i.e. the locations asked for really exist and returns false otherwise
     */
    public Boolean addEdge(String s1, String s2, String type){
        if(!typeIsCorrect(type)){
            return false;
        }
        //Two for loops are nested to find both nodes and make them both available to use at once
        for (Node n1 : Nodes){ 
            if (n1.location == s1) 
            {
                for (Node n2 : Nodes){
                    if (n2.location == s2) {
                        n1.addEdge(n2, type); 
                        n2.addEdge(n1, type);
                        return true;
                    }
                }
            }
        }
        //Code would terminate before reaching this point if the inputs were valid
        System.out.println("The chosen location wasn't found, please check your spelling of the start and end city");
        return false;
    }
    /*
     * //This method adds an edge between two nodes of a specified type. it returns a boolean to inform whatever calls it if it succeeded or failed for testing purposes
     * @param String s1, the starting location of the edge
     * @param String s2, the finishing location of the edge
     * @param String type, the type of connection this edge forms
     * @return Returns true if the edge was successfully removed i.e. the locations asked for really exist and returns false otherwise
     */
    public boolean removeEdge(String s1, String s2, String type){
        if(!typeIsCorrect(type)){
            return false;
        }
        //Two for loops are nested to find both nodes and make them both available to use at once
        for (Node n1 : Nodes){ 
            if (n1.location == s1)
            {
                for (Node n2 : Nodes){
                    if (n2.location == s2){
                        if(n1.findEdge(n2, type)){
                            n1.removeEdge(n2, type);
                            return true;
                        }
                        
                    }
                }
            }
        }
        //Code would terminate before reaching this point if the inputs were valid
        System.out.println("The chosen location wasn't found, please check your spelling of the start and end city");
        return false;
    }
    /*
     * hasEdge searches through all edges of all nodes for an edge matching the parameters given
     * @param String s1, Starting city
     * @param String s2, Ending city
     * @param String type, Type of transportation
     * @return Boolean, true if the edge was found, false otherwise
     */
    public boolean hasEdge(String s1, String s2, String type){
        Node n1 = new Node(null);
        Node n2 = new Node(null);
        for(Node n : Nodes){
            if(n.location == s1) n1 = n;
        }
        for (Node n: Nodes){
            if (n.location == s2) n2 = n;
        }
        return n1.findEdge(n2,type);
    }
    public String getEdgesOfType(String type){
        ArrayList<String[]> edgeList = new ArrayList<String[]>();
        for(Node n : Nodes){
            ArrayList<Edge> tempList = n.getEdge(type);
            for(Edge e : tempList){
                String[] add = new String[] {e.n1.location, e.n2.location};
                edgeList.add(add); //add each edge into the array
            }
        }
        for(String[] s : edgeList){
            //now search through all these strings and compare them to all strings, deleting any entries that are already there
            for(String[] s2: edgeList){
                if (s[1] == s2[0] && s[0] == s2[1]){
                    edgeList.remove(s); //removes inverted doubles
                }
                if (s[1] == s2[1] && s[0] == s2[0]) edgeList.remove(s); //remove duplicates
            }
        }
        //sort the list. i'm choosing to use heapsort because its fast, doesn't use much memory, and i think it's fun
        edgeList = heapSort(edgeList);
        String r = "";
        //now turn this organised list into a single string
        for(String[] s : edgeList){
            r = r + System.lineSeparator() +"(" + s[0] + ", " + s[1] + ")"; //system.lineSeparator adapts for the specific OS used since windows and linux do new lines differently for strings in java. s[0] would be the name of the
            //first location, which is then separated from the second with some space and a comma. the second location is identified with s[1]. the heapsort earlier alphabetically sorted this array by the first location 
        }
        return r;
    }
    /*Prints all nodes and connections to console */
    public String Print(){
        ArrayList<String[]> edgeList = new ArrayList<>();
        for(Node n : Nodes){
            ArrayList<Edge> tempList = n.getEdge("Bus");
            tempList.addAll(n.getEdge("Plane"));
            tempList.addAll(n.getEdge("Train"));
            for(Edge e : tempList){
                String[] add = new String[] {e.n1.location, e.n2.location, e.type};
                edgeList.add(add); 
            }
        }
        for(String[] s : edgeList){
            //search through all these strings and compare them to all strings, deleting any entries that are already there
            for(String[] s2: edgeList){
                if (s[1] == s2[0] && s[0] == s2[1]){
                    //remove inverted doubles
                    edgeList.remove(s); 
                }
                //removes duplicates
                if(s[1] == s2[1]&& s[0] == s2[0] && s[2] == s2[2]) edgeList.remove(s); 
            }
        }
        //now there should be a list of all edges with no duplicates. these need to be sorted into alphabetical order
        edgeList = heapSort(edgeList);
        String r = "";
        String currentCity = "";
        for(int i = 0; i<= edgeList.size(); i++){
            String [] currentEdge = edgeList.get(i);
            if (currentCity != currentEdge[0]){ 
                r = r + System.lineSeparator() + currentEdge[0] + ": ";
                currentCity = currentEdge[0]; 
            }
            r = r + "(" + currentEdge[1] + ", "+ currentEdge[2] + ")";
        }
        System.out.println(r);
        return r;
    }
    public String ticketTraverse(String startCity, int busTickets, int planeTickets, int trainTickets){
        String r = "";
        ArrayList<String> bussableLocations = traverseWorker(startCity, "Bus", busTickets);
        ArrayList<String> trainLocations = traverseWorker(startCity,"Train", trainTickets);
        ArrayList<String> planeLocations = traverseWorker(startCity,"Plane", planeTickets);
        for(String s : bussableLocations){
            r = r + System.lineSeparator() + s;
        }
        for(String s : trainLocations){
            r = r + System.lineSeparator() + s;
        }
        for(String s : planeLocations){
            r = r + System.lineSeparator() + s;
        }
        return r;
    }
    /*
     * Given a starting city, type and number of tickets returns an ArrayList of all cities that can be reached with that information
     * @param String startCity, Starting city for the method
     * @param String type, Type of travel being traversed
     * @param int tickets, Number of tickets the user has for that type of travel
     * @return ArrayList containing a list of locations the user can travel to
     */
    private ArrayList traverseWorker(String startCity, String type, int tickets){
        ArrayList<String> cities = new ArrayList<>();
        Node StartCity = new Node(null);
        for(Node n : Nodes){
            if(n.location == startCity)StartCity = n;
        }
        try {
            ArrayList<Edge> edges = StartCity.getEdge(type);
            if(tickets > 1){
                for(Edge e : edges){
                    cities.add(e.n2.location);
                }
            }
            ArrayList<String> visitedCities = cities;
            for(int i = 1; i <= tickets; i++){//start at 1 as getting all the edges counted as moving one step
                for(String s : cities){
                    Node n = getNode(s);
                    ArrayList<Edge> cityEdges = n.getEdge(type);
                    for(Edge e : cityEdges){
                        if (cities.contains(e.n2.location) || visitedCities.contains(e.n2.location)){} 
                        else{
                            cities.add(e.n2.location);
                        }
                    }
                }
                cities.removeAll(visitedCities);
                visitedCities.addAll(cities);
            }
            return visitedCities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }

    /*Sorts an ArrayList passed into it using heapsort */
    private ArrayList<String[]> heapSort(ArrayList<String[]> edgeList){
        int n = edgeList.size();
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(edgeList, n, i);
        }
        String r;
        //extract elements from heap
        for (int i = n-1; i>=0; i--){
            String[] temp = edgeList.get(0);
            edgeList.set(0, edgeList.get(i)) ;
            edgeList.set(i, temp);

            // call max heapify on the reduced heap
            heapify(edgeList, i, 0);
        }
        return edgeList;
    }
    /* Code for sorting an array passed into it*/
    private static void heapify(ArrayList<String[]> arr, int size, int root) {
        int largest = root; // Initialize largest as root
        int left = 2 * root + 1; // left child
        int right = 2 * root + 2; // right child

        // If left child is larger than root
        if (arr.get(left)[0].compareTo(arr.get(largest)[0]) > 0) { 
            largest = left;
        }

        // If right child is larger than largest so far
        if (arr.get(right)[0].compareTo(arr.get(largest)[0]) > 0) {
            largest = right;
        }

        // If largest is not root
        if (largest != root) {
            String[] swap = arr.get(root);
            arr.set(root, arr.get(largest));
            arr.set(largest, swap);

            // Recursively heapify the affected sub-tree
            heapify(arr, size, largest);
        }
    }
}
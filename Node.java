import java.util.ArrayList;

public class Node{
    String location;
    private ArrayList<Edge> edges;
    public Node(String s){
        location = s;
        edges = new ArrayList<Edge>();
    }
    /*Used to let the graph add edges */
    public void addEdge(Node otherNode, String type){
        edges.add(new Edge(this, otherNode, type));
    }
    /*Search for if this node has an edge with another node. note that this node would always be n1 due to how edges are made */
    public boolean findEdge(Node otherNode, String type){
        for(Edge e : edges){
            if(e.n2 == otherNode) {
                if(e.type == type) return true;
            }
        }
        return false;
    }
    /*some graph methods need to know how many edges there are for the sake of for loops. */
    public int edgeNumber(){ 
        return edges.size();
    }
    /*
     * Gets an edge given a node
     * @param Node otherNode, Node that this edge should connect to
     * @return Edge that connects to the Node inputted
     */
    public Edge getEdge(Node otherNode){
        for( Edge e : edges){
            if (e.n2 == otherNode) return e;
        }
        return null;
    }
    /*From a string, returns an ArrayList of edges
     * @param String type, String containing the connection type of the edge list being made
     * @return ArrayList<Edge> containing every Edge of the type
     */
    public ArrayList<Edge> getEdge(String type){
        ArrayList<Edge> r = new ArrayList<Edge>();
        for (Edge e : edges){
            if (e.type == type){
                r.add(e);
            }
        }
        return r;
    }
    /*removes all edges that contain the given node as the second connection. used for removing nodes
     * @param Node otherNode, The node which all connections are being severed from
     */
    public void removeEdge(Node otherNode){
        for (Edge e : edges){
            if(e.n2 == otherNode) edges.remove(e); 
        }
    }
    /*Alternate form of removeEdge which specifies a type too, rather than moving all connections to a node */
    public void removeEdge(Node otherNode, String type){
        for (Edge e : edges){
            if(e.n2 == otherNode){
                if (e.type == type){ //remove the edge that specifically has the type specified
                    edges.remove(e);
                }
            }
        }
    }
}
/*
/* This class is my solution to the problem of how to add information other than simply there being two nodes that are connected. an edge contains the node that created it n1, the node
 * it connects to n2, and the type of connection as a string called type.
 */
public class Edge{
    Node n1;
    Node n2;
    String type;
    /*Constructor for Edges */
    public Edge(Node one, Node two, String t){
        n1 = one;
        n2 = two;
        type = t;
    }
    /*Returns true if the node is contained in this Edge. Node is specified through a string containing the nodes location */
    public boolean hasNode(String s){
        if(n1.location == s || n2.location == s) return true;
        return false;
    }
    /*Returns a string giving the type of connection this Edge contains */
    public String getType(){
        return type;
    }
    

}
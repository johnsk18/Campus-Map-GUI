package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph<N,E> {
	private ArrayList<Edge<N,E>> edges;
	private HashMap<N,Node<N,E>> nodes;
	
	//	Abstraction Function:
	//		A Graph is a mutable collection of edges, modeling a directed labeled multigraph. 
	//		The nodes in the graph represent a character while the edges represent a relation
	//		between two characters (common book or number of common books). Each node has a 
	//		unique label and no identical edges are allowed. A collection of unique nodes are 
	//		also stored in the graph. The N type represents the type for node labels while the
	//		E type represents the type for edge labels.
	//	Representation Invariant for every Graph g:
	//		for all i,j from 0 <= i,j < edges.size():
	//			if i < j:
	//				edges[i].parent != edges[j].parent	||
	//				edges[i].child != edges[j].child	||
	//				edges[i].label != edges[j].label
	//		for all i,j from 0 <= i,j < nodes.values().size():
	//			if i != j:
	//				nodes.values[i].getLabel() != nodes.values[j].getLabel()
	//				
	//		In other words:
	//		* there are no identical edges in Graph g
	//		* each node in Graph g has a unique label, which doesn't need to be checked due to the
	//		  functionality of a HashMap<String,Node> by using Node labels as keys, providing 1 node per
	//		  unique label.
	
	/**
	 * Constructs a Graph object
	 * 
	 * @effects Constructs an empty Graph
	 */
	public Graph() {
		edges = new ArrayList<Edge<N,E>>(0);
		nodes = new HashMap<N,Node<N,E>>(0);
		checkRep();
	}
	
	/**
	 * Constructs a Graph object with a hashmap of nodes
	 * 
	 * @param n The hashmap of nodes
	 * @requires n != null
	 * @effects Constructs a Graph, adding all nodes 'n'
	 */
	public Graph(HashMap<N,Node<N,E>> n) {
		if (n == null) { // handles null hashmap
			nodes = new HashMap<N,Node<N,E>>(0);
		} else {
			nodes = new HashMap<N,Node<N,E>>(n);
		}
		Iterator<N> keys = nodes.keySet().iterator();
		edges = new ArrayList<Edge<N,E>>(0);
		while (keys.hasNext()) {
			ArrayList<Edge<N,E>> node_edges = nodes.get(keys.next()).getEdges();
			for (int i = 0; i < node_edges.size(); i++) {
				edges.add(node_edges.get(i));
			}
		}
		checkRep();
	}
	
	/**
	 * Constructs a Graph object with an arraylist of edges
	 * 
	 * @param e The list of edges
	 * @requires e != null
	 * @effects Constructs a Graph from the list of edges 'e'
	 */
	public Graph(ArrayList<Edge<N,E>> e) {
		if (e == null) { // handles null array list
			edges = new ArrayList<Edge<N,E>>(0);
		} else {
			edges = new ArrayList<Edge<N,E>>(e);
		}
		nodes = new HashMap<N,Node<N,E>>(0);
		for (int i = 0; i < edges.size(); i++) {
			if (nodes.get(edges.get(i).getParent().getLabel()) == null) { // if not in map
				nodes.put(edges.get(i).getParent().getLabel(), edges.get(i).getParent());
			}
			nodes.get(edges.get(i).getParent().getLabel()).addEdge(edges.get(i));
			if (nodes.get(edges.get(i).getChild().getLabel()) == null) { // if not in map
				nodes.put(edges.get(i).getChild().getLabel(), edges.get(i).getChild());
			}
			nodes.get(edges.get(i).getChild().getLabel()).addEdge(edges.get(i));	
		}
		checkRep();
	}
	
	/**
	 * Constructs a Graph object with both given nodes and edges to reduce runtime. The
	 * representation invariant is not checked to also help reduce runtime.
	 * 
	 * @param e The list of edges
	 * @param n The hashmap of nodes
	 * @requires e != null && n != null
	 * @effects Constructs a Graph from the list of edges 'e' and nodes 'n' without CheckRep
	 */
	public Graph(HashMap<N,Node<N,E>> n, ArrayList<Edge<N,E>> e) {
		if (e == null) {
			edges = new ArrayList<Edge<N,E>>(0);
		} else {
			edges = new ArrayList<Edge<N,E>>(e);
		}
		if (n == null) {
			nodes = new HashMap<N,Node<N,E>>(0);
		} else {
			nodes = new HashMap<N,Node<N,E>>(n);
		}
//		checkRep();
	}
	
	/**
	 * Returns a node labeled 'l' or null if none found
	 * 
	 * @param l The label of the node
	 * @requires l != null
	 * @return the node with the matching label 'l', or null if no such node with label 'l'
	 */
	public Node<N,E> getNode(N l) {
		return nodes.get(l);
	}
	
	/**
	 * Returns true if Node n can be added to nodes
	 * 
	 * @param n The node to be added
	 * @requires n != null
	 * @modifies nodes
	 * @effects Adds n in nodes if contained
	 * @return true if and if only there n is successfully added from nodes
	 */
	public boolean addNode(Node<N,E> n) {
		if (nodes.containsKey(n.getLabel())) {
			return false;
		} else {
			nodes.put(n.getLabel(), n);
			return true;
		}
	}
	
	/**
	 * Returns true if Node n can be removed from nodes
	 * 
	 * @param n The node to be removed
	 * @requires n != null
	 * @modifies nodes
	 * @effects Removes n in nodes if contained
	 * @return true if and if only there n is successfully removed from nodes
	 */
	public boolean removeNode(Node<N,E> n) {
		if (nodes.remove(n.getLabel()) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if Edge e can be added to edges
	 * 
	 * @param e The edge to be added
	 * @requires e != null
	 * @modifies edges
	 * @effects Adds e to edges if not contained
	 * @return true if and if only there e is successfully added to edges
	 */
	public boolean addEdge(Edge<N,E> e) {
		if (!(nodes.containsKey(e.getParent().getLabel()) && nodes.containsKey(e.getChild().getLabel()))) {
			return false;
		} else {
			edges.add(e);
			nodes.get(e.getParent().getLabel()).addEdge(e);
			checkRep();
			return true;
		}
	}
	
	/**
	 * Returns true if Edge e can be removed from edges
	 * 
	 * @param e The edge to be removed
	 * @requires e != null
	 * @modifies edges
	 * @effects Removes e in edges if contained
	 * @return true if and if only there e is successfully removed from edges
	 */
	public boolean removeEdge(Edge<N,E> e) {
		return edges.remove(e);
	}
	
	/**
	 * Returns the list of edges in Graph
	 * 
	 * @return a list of edges
	 */
	public ArrayList<Edge<N,E>> getEdges() {
		return new ArrayList<Edge<N,E>>(edges);
	}
	
	/**
	 * Returns the values of nodes in Graph or an empty list if there are none
	 * 
	 * @return a list of nodes, or an empty list if the graph is empty
	 */
	public ArrayList<Node<N,E>> getNodes() {
		if (nodes.isEmpty()) {
			return new ArrayList<Node<N,E>>(0);
		} else {
			return new ArrayList<Node<N,E>>(nodes.values());
		}
	}
	
    /**
     * Checks that the representation invariant holds (if any).
     **/
    // Throws a RuntimeException if the rep invariant is violated.
	private void checkRep() throws RuntimeException {
		for (int i = 0; i < edges.size(); i++) {
			for (int j = 0; j < edges.size(); j++) {
				if (i < j && (edges.get(i).equals(edges.get(j)))) {
					throw new RuntimeException("Edges in a Graph cannot be identical");
				}
			}
		}
	}
}
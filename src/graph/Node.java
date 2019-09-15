package graph;

import java.util.ArrayList;

public class Node<N,E> {
	private final N label;
	private ArrayList<Edge<N,E>> edges;
	
	//	Abstraction Function:
	//		A Node is a vertex of the graph that contains its label and list of edges to 
	//		directed nodes. This may represent a character in marvel.csv. The edges a node 
	//		contains simulates a relation between the child nodes of the edges with this node.
	//		For example, a node will have edges to other nodes if they are in the same book.
	//		The N type represents the type for node labels while the E type represents the type 
	//		for edge labels.
	//	Representation Invariant for every Graph g:
	//		n.label != null
	//				
	//		In other words:
	//		* no node label is null
	
	/**
	 * Constructs a Node object
	 * 
	 * @param l The node's label
	 * @requires l != null
	 * @effects Constructs a node named 'l'
	 */
	public Node(N l) {
		label = l;
		edges = new ArrayList<Edge<N,E>>(0);
		checkRep();
	}
	
	/**
	 * Constructs a Node object with a given arraylist of edges
	 * 
	 * @param l The name of the node
	 * @param e List of edges to directed nodes
	 * @requires l != null, e != null
	 * @effects Constructs a node, named 'l', with given edges from 'e'
	 */
	public Node(N l, ArrayList<Edge<N,E>> e) {
		label = l;
		edges = new ArrayList<Edge<N,E>>(0);
		for (int i = 0; i < e.size(); i++) {
			this.addEdge(e.get(i));
		}
		checkRep();
	}
	
	/**
	 * Returns the label of the node
	 * 
	 * @return a string of the node's label
	 */
	public N getLabel() {
		return label;
	}
	
	/**
	 * Adds an edge from this node to another.
	 * 
	 * @param e The edge to be added
	 * @requires e != null
	 * @modifies edges
	 * @effects Adds e to edges if not contained
	 * @return true if and if only there e is successfully added to edges
	 */
	public boolean addEdge(Edge<N,E> e) {
		if (containsEdge(e)) {
			return false;
		} else {
			edges.add(e);
			return true;
		}
	}
	
	/**
	 * Removes an edge from this node's stored edges
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
	 * Returns a copy of the list of edges from this node
	 * 
	 * @return a list of node's edges
	 */
	public ArrayList<Edge<N,E>> getEdges() {
		return new ArrayList<Edge<N,E>>(edges);
	}
	
	/**
	 * Returns true if 'e' is contained in edges
	 * 
	 * @param e The edge being compared with
	 * @requires e != null
	 * @return true if and if only there exists an edge in edges that is identical to 'e'
	 */
	public boolean containsEdge(Edge<N,E> e) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).equals(e)) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * Standard equality operation.
     * 
     * @param obj The object to be compared for equality.
     * @return true if and if only the labels for 'this' and 'obj' are identical
     */
    @Override
    public boolean equals(/*@Nullable*/ Object obj) {
        if (obj instanceof Node<?,?>) {
        	Node<?,?> n = (Node<?,?>) obj;
            return this.getLabel() == n.getLabel();
        } else {
            return false;
        }
    }
	
    /**
     * Checks that the representation invariant holds (if any).
     **/
    // Throws a RuntimeException if the rep invariant is violated.
	private void checkRep() throws RuntimeException {
		if (label == null) {
			throw new RuntimeException("Label must be specified");
		}
	}
}

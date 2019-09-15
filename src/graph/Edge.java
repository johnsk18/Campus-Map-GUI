package graph;

public class Edge<N,E> {
	private Node<N,E> parent;
	private Node<N,E> child;
	private E label;
	
	//	Abstraction Function:
	//		A Edge is a connection between two nodes. This represents the relation between two
	//		characters in the graph, such as a common book or the number of times both characters
	//		appear in the same book. The N type represents the type for node labels while the E 
	//		type represents the type for edge labels.
	//	Representation Invariant for every Graph g:
	//		e.parent != null && 
	//		e.child != null && 
	//		e.label != null
	//				
	//		In other words:
	//		* no edge parent node, child node, and label is null
	
	/**
	 * Constructs an Edge object
	 * 
	 * @param p The node being directed away from
	 * @param c The node being directed to (or partner node)
	 * @param l The edge's label (weight)
	 * @requires parent != null && child != null && label != null
	 * @effects Constructs a edge named 'l', modeling 'p' to 'c'
	 */
	public Edge(Node<N,E> p, Node<N,E> c, E l) {
		parent = p;
		child = c;
		label = l;
		checkRep();
	}
	
	/**
	 * Returns the node that is being directed away from
	 * 
	 * @return the parent node
	 */
	public Node<N,E> getParent() {
		return parent;
	}
	
	/**
	 * Returns the node that is being directed to
	 * 
	 * @return the child node
	 */
	public Node<N,E> getChild() {
		return child;
	}
	
	/**
	 * Returns the label of the edge
	 * 
	 * @return a string of the edge's label (weight)
	 */
	public E getLabel() {
		return label;
	}
	
    /**
     * Standard equality operation.
     * 
     * @param obj The object to be compared for equality.
     * @return true if and if only the parents, children, and labels between 'this'
     * and 'obj' are identical
     */
    @Override
    public boolean equals(/*@Nullable*/ Object obj) {
        if (obj instanceof Edge<?,?>) {
            Edge<?,?> e = (Edge<?,?>) obj;
            return this.getParent().equals(e.getParent()) && 
            	   this.getChild().equals(e.getChild()) && 
            	   this.getLabel().equals(e.getLabel());
        } else {
            return false;
        }
    }
	
    /**
     * Checks that the representation invariant holds (if any).
     **/
    // Throws a RuntimeException if the rep invariant is violated.
	private void checkRep() throws RuntimeException {
		if (parent == null) {
			throw new RuntimeException("parent == null");
		} 		
		if (child == null) {
			throw new RuntimeException("child == null");
		} 
		if (label == null) {
			throw new RuntimeException("label == null");
		}
	}
}

package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

// Comparator to help sort strings
class StringComparator implements Comparator<String> {
	public int compare(String s1, String s2) {
		return s1.compareTo(s2);
	}
}

public class GraphWrapper {
	private Graph<String,String> graph;
	
	public GraphWrapper() {
		graph = new Graph<String,String>();
	}
	
	public void addNode(String nodeData) {
		graph.addNode(new Node<String,String>(nodeData));
	}
	
	public void addEdge(String parentNode, String childNode, String edgeLabel) {
		graph.addEdge(new Edge<String,String>(new Node<String,String>(parentNode), new Node<String,String>(childNode), edgeLabel));
	}
	
	public Iterator<String> listNodes() {
		ArrayList<Node<String,String>> graph_nodes = graph.getNodes();
		ArrayList<String> strings = new ArrayList<String>(0);
		for (int i = 0; i < graph_nodes.size(); i++) {
			strings.add(graph_nodes.get(i).getLabel());
		}
		strings.sort(new StringComparator());
		return strings.iterator();
	}
	
	public Iterator<String> listChildren(String parentNode) {
		Node<String,String> node = graph.getNode(parentNode);
		if (node == null) {
			return null;
		} else {
			ArrayList<Edge<String,String>> node_edges = node.getEdges();
			ArrayList<String> strings = new ArrayList<String>(0);
			for (int i = 0; i < node_edges.size(); i++) {
				strings.add(node_edges.get(i).getChild().getLabel() + "(" + node_edges.get(i).getLabel() + ")");
			}
			strings.sort(new StringComparator());
			return strings.iterator();
		}
	}
}

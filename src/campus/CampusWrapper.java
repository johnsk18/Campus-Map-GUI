package campus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Node;

//Comparator to help sort edges by weights, then child label in a given arraylist of edges.
class EdgeWeightComparator<N,E> implements Comparator<Edge<N,E>> {
	public int compare(Edge<N,E> e1, Edge<N,E> e2) {
		int x = ((Double) e1.getLabel()).compareTo((Double) e2.getLabel());
		if (x == 0) {
			return ((String) e1.getChild().getLabel()).compareTo((String) e2.getChild().getLabel());
		}
		return x;
	}
}

//Comparator to help sort arraylists in a priority queue by increasing cost order
class QueueCostComparator<N,E> implements Comparator<ArrayList<Edge<N,E>>> {
	public int compare(ArrayList<Edge<N,E>> a1, ArrayList<Edge<N,E>> a2) {
		double a1_sum = 0;
		double a2_sum = 0;
		for (Edge<N,E> edge : a1) a1_sum += (Double) edge.getLabel();
		for (Edge<N,E> edge : a2) a2_sum += (Double) edge.getLabel();
		if (a1_sum < a2_sum) {
			return -1;
		} else if (a1_sum > a2_sum) {
			return 1;
		}
		return 0;
	}
}

//CampusWrapper is not an ADT, but is a subtype of Graph; therefore it does not need an
//abstraction function nor representation invariant. CampusWrapper supports creating a 
//Graph<String,Double> with the given filename by parsing through the data. The data is 
//stored a node file and edge file. CampusWrapper also supports finding the lowest cost 
//path between two buildings on campus using Dijkstra's algorithm on the graph. CampusWrapper
//acts as a model for the MVC.

public class CampusWrapper { // Model
	private Graph<String,Double> campus;
	private CampusLocations locations;
	
	/**
	 * Creates a CampusWrapper object
	 * 
	 * @effects Constructs a CampusWrapper object with an uninitialized graph
	 */
	public CampusWrapper() {}
	
	/**
	 * Creates a CampusWrapper object and tries to make a graph with given filenames
	 * 
	 * @param nodeFilename The name of the node file to be parsed
	 * @param edgeFilename The name of the edge file to be parsed
	 * @requires nodeFilename != null && edgeFilename != null
	 * @effects Constructs a CampusWrapper object with a graph from the data files
	 */
	public CampusWrapper(String nodeFilename, String edgeFilename) {
		createNewGraph(nodeFilename,edgeFilename);
	}
	
	/**
	 * 
	 * @return The campus locations
	 */
	public CampusLocations getLocations() { return locations; }
	
	/**
	 * Tries to create a graph with a given filenames
	 * 
	 * @param nodeFilename The filename containing node data
	 * @param edgeFilename The filename containing edge data
	 * @modifies graph
	 */
	public void createNewGraph(String nodeFilename, String edgeFilename) {
		try {
			Map<String,ArrayList<String>> dataFromIDs = new HashMap<String,ArrayList<String>>();
			Map<String,String> buildings = new HashMap<String,String>();
    		Set<ArrayList<String>> pathways = new HashSet<ArrayList<String>>();
    		HashMap<String,Node<String,Double>> nodes = new HashMap<String,Node<String,Double>>(0);
			CampusParser.readData(nodeFilename,edgeFilename,dataFromIDs,buildings,pathways,nodes);
			locations = new CampusLocations(dataFromIDs,buildings);
			Iterator<ArrayList<String>> path_iter = pathways.iterator();
    		ArrayList<Edge<String,Double>> edges = new ArrayList<Edge<String,Double>>(0);
			while (path_iter.hasNext()) {
				ArrayList<String> ids = path_iter.next();
				String id1 = ids.get(0);
				String id2 = ids.get(1);
				Double distance = Math.sqrt(Math.pow(Double.parseDouble(dataFromIDs.get(id2).get(1)) - Double.parseDouble(dataFromIDs.get(id1).get(1)),2) + Math.pow(Double.parseDouble(dataFromIDs.get(id2).get(2)) - Double.parseDouble(dataFromIDs.get(id1).get(2)),2));
				edges.add(new Edge<String,Double>(nodes.get(id1),nodes.get(id2),distance));
				edges.add(new Edge<String,Double>(nodes.get(id2),nodes.get(id1),distance));
				nodes.get(id1).addEdge(new Edge<String,Double>(nodes.get(id1),nodes.get(id2),distance));
				nodes.get(id2).addEdge(new Edge<String,Double>(nodes.get(id2),nodes.get(id1),distance));
			}
			campus = new Graph<String,Double>(nodes,edges);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Tries to find a minimum-cost path from name1 to name2 using Dijkstra's algorithm
	 * 
	 * @param name1 The start node
	 * @param name2 The destination node
	 * @requires name1 != null && name2 != null
	 * @return a simple string of the path from name1 to name2 using Dijkstra's algorithm
	 */
	public String findPath(String name1, String name2) {
		String path_string = new String();
		// checks if IDs belongs to a building in the graph
		if ((locations.getName(name1) != null && locations.getName(name1).equals("")) || campus.getNode(name1) == null) { // if name1 invalid
			path_string += new String(","+name1);
		}
		if (!(name1.equals(name2)) && ((locations.getName(name2) != null && locations.getName(name2).equals("")) || campus.getNode(name2) == null)) { // if name2 is invalid & unique
			path_string += new String(","+name2);
		}
		if (!path_string.equals(new String())) { // returns invalid input string
			return path_string;
		}
		// start of the Dijkstra algorithm
		PriorityQueue<ArrayList<Edge<String,Double>>> active = new PriorityQueue<ArrayList<Edge<String,Double>>>(1,new QueueCostComparator<String,Double>());
		ArrayList<String> finished = new ArrayList<String>();
		ArrayList<Edge<String,Double>> self_path = new ArrayList<Edge<String,Double>>(0);
		self_path.add(new Edge<String,Double>(campus.getNode(name1),campus.getNode(name1),(double) 0));
		active.add(self_path);
		while (!active.isEmpty()) {
			ArrayList<Edge<String,Double>> minPath = active.poll();
			String minDest = minPath.get(minPath.size() - 1).getChild().getLabel();
			if (minDest.equals(name2)) { // destination has been reached
				double total_cost = 0;
				minPath.remove(0);
				for (Edge<String,Double> edge : minPath) {
					path_string += String.format("%s,", edge.getChild().getLabel());
					total_cost += edge.getLabel();
				}
				return path_string + String.format("%.3f",total_cost);
			}
			if (finished.contains(minDest)) continue;
			ArrayList<Edge<String,Double>> node_edges = new ArrayList<Edge<String,Double>>(campus.getNode(minDest).getEdges());
			Collections.sort(node_edges,new EdgeWeightComparator<String,Double>());
			Iterator<Edge<String,Double>> sorted_node_edges = node_edges.iterator();
			// loops for each edge next_edge = <minDest,child_name>
			while (sorted_node_edges.hasNext()) {
				Edge<String,Double> next_edge = sorted_node_edges.next();
				String child_name = next_edge.getChild().getLabel();
				if (!finished.contains(child_name)) { // if the child hasn't been visited
					ArrayList<Edge<String,Double>> newPath = new ArrayList<Edge<String,Double>>(minPath);
					newPath.add(next_edge);
					active.add(newPath);
				}
			}
			finished.add(minDest);
		}
		// returns no path;
		return new String("");
	}
}

package campus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//	CampusPaths is not an ADT; therefore it does not need an abstraction function nor 
//	representation invariant. CampusWrapper supports finding the lowest cost path between two 
//	buildings on campus using Dijkstra's algorithm on the graph, listing all campus buildings,
//	print all menu commands with their descriptions, and quitting the view. CampusPaths acts as the
//	view and controller for the MVC. Most of CampusPaths.menu is the view while CampusPaths.main is
//	the controller.

public class CampusPaths { // View and Controller
	private static BufferedReader reader;
	
	/**
	 * Processes menu commands from the main method
	 * 
	 * @param input The input command
	 * @param graph The campus graph 
	 * @throws IOException
	 */
	public static void menu(String input, CampusWrapper graph) throws IOException {
		if (input.equals("b")) {
			System.out.print(graph.getLocations().listAllBuildings());
		} else if (input.equals("r")) {
			System.out.print("First building id/name, followed by Enter: ");
			String input1 = reader.readLine();
			System.out.print("Second building id/name, followed by Enter: ");
			String input2 = reader.readLine();
			String path,id1,id2;
			// Tries to determine the ID from the first input
			if (graph.getLocations().getID(input1) == null) id1 = input1;
			else id1 = graph.getLocations().getID(input1); 
			// Tries to determine the ID from the second input
			if (graph.getLocations().getID(input2) == null) id2 = input2;
			else id2 = graph.getLocations().getID(input2);
			// finds and prints path
			path = graph.findPath(id1, id2);
			if (path.equals("0.000")) { // for path to self
				System.out.println(String.format("Path from %s to %s:\nTotal distance: %s pixel units.",graph.getLocations().getName(id1),graph.getLocations().getName(id2),path));
				return;
			}
			int i = path.indexOf(",");
			if (i == 0) { // for unknown buildings
				i = path.indexOf(",",1);
				if (i != -1) System.out.println(String.format("Unknown building: [%s]\nUnknown building: [%s]",path.substring(1, i),path.substring(i+1,path.length())));
				else System.out.println(String.format("Unknown building: [%s]",path.substring(1,path.length())));
			} else if (i == -1) System.out.println(String.format("There is no path from %s to %s.",graph.getLocations().getName(id1),graph.getLocations().getName(id2)));
			else { // prints path
				System.out.println(String.format("Path from %s to %s:",graph.getLocations().getName(id1),graph.getLocations().getName(id2)));
				String id3 = path.substring(0,i);
				String name = graph.getLocations().getName(id3);
				String direction = graph.getLocations().getDirection(id1,id3);
				if (name.equals("")) System.out.println(String.format("\tWalk %s to (Intersection %s)",direction,id3));
				else System.out.println(String.format("\tWalk %s to (%s)",direction,name));
				int j = path.indexOf(",",i+1);
				while (i != -1) { // iterates through path string, printing each line
					if (j != -1) { // if path has not ended
						String id4 = path.substring(i+1,j);
						name = graph.getLocations().getName(id4);
						direction = graph.getLocations().getDirection(id3,id4);
						if (name.equals("")) System.out.println(String.format("\tWalk %s to (Intersection %s)",direction,id4));
						else System.out.println(String.format("\tWalk %s to (%s)",direction,name));
						id3 = id4;
					} else System.out.println(String.format("Total distance: %s pixel units.",path.substring(i+1,path.length())));
					i = j;
					j = path.indexOf(",",i+1);
				}
			}
		} else if (input.equals("m")) {
			System.out.println("b: lists all buildings (only buildings) in the form name,id in lexicographic (alphabetical) order of name.");
			System.out.println("r: prompts the user for the ids or names of two buildings (only buildings!) and prints directions for the shortest route between them.");
			System.out.println("q: quits the program.\nm: prints a menu of all commands.");
		} else System.out.println("Unknown option");
	}
	
	/**
	 * Main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String nodeArg = "src/campus/data/RPI_map_data_Nodes.csv";
		String edgeArg = "src/campus/data/RPI_map_data_Edges.csv";
		CampusWrapper graph = new CampusWrapper(nodeArg,edgeArg);
		reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		while (!input.equals("q")) {
			menu(input,graph);
			input = reader.readLine();
		}
	}
}

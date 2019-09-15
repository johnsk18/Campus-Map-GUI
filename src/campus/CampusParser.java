package campus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import graph.Node;


//	CampusParser is not an ADT; therefore it does not need an abstraction function nor 
//	representation invariant. CampusParser supports reading data from two files, a node
//	file and an edge file, to fill multiple containers for information on buildings, pathways, 
//	and nodes. The files passed into CampusParser are assumed to be correctly parsed, and contain
//	no duplicate IDs. CampusParser is a helper class for the model for the MVC.

public class CampusParser {
    private static BufferedReader reader;

    /**
     * Parses through the given files and inputs data into the other parameters.
     * 
     * @param nodeFilename The path to the "CSV" file that contains the node data
     * @param edgeFilename The path to the "CSV" file that contains the edge data
     * @param dataFromIDs The map that stores parsed <id, [name,x,y]>
     * @param buildings The map that stores <name,id>
     * @param pathways The set that stores <[id1,id2]> pathways
     * @param nodes The hashmap that stores <id, Node(id)>
     * @throws IOException if file cannot be read
     */
	public static void readData(String nodeFilename, String edgeFilename, Map<String,ArrayList<String>> dataFromIDs, Map<String,String> buildings, Set<ArrayList<String>> pathways, HashMap<String,Node<String,Double>> nodes) 
    		throws IOException {

    	reader = new BufferedReader(new FileReader(nodeFilename));
        String line = null;

        while ((line = reader.readLine()) != null) {
             int i = line.indexOf(",");
             int j = line.indexOf(",",i+1);
             int k = line.indexOf(",",j+1);
//             if ((i == -1) || (j == -1) || (j == -1)) { // Node file is a working CSV
//            	 throw new IOException("File "+nodeFilename+" not a CSV (name,id,x-coordinate,y-coordinate) file.");
//             }             
             String name = line.substring(0,i);
             String id = line.substring(i+1,j);
             String x = line.substring(j+1,k);
             String y = line.substring(k+1,line.length());
             // Node file seems to promise no duplicate IDs so no need to check (improves coverage)
             dataFromIDs.put(id,new ArrayList<String>(Arrays.asList(name,x,y)));
        	 buildings.put(name, id);
        	 nodes.put(id, new Node<String,Double>(id));
        }
        
       	reader = new BufferedReader(new FileReader(edgeFilename));

        while ((line = reader.readLine()) != null) {
             int i = line.indexOf(",");
//             if (i == -1) { // Edge file is a working CSV
//            	 throw new IOException("File "+edgeFilename+" not a CSV (id1,id2) file.");
//             }
             String id1 = line.substring(0,i);
             String id2 = line.substring(i+1,line.length());
             pathways.add(new ArrayList<String>(Arrays.asList(id1,id2)));
        }
        
    }
}

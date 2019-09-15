package campus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;


public class CampusLocations { // Model
	private Map<String,ArrayList<String>> dataFromIDs;
	private Map<String,String> buildings;
	
	//	Abstraction Function:
	//		CampusLocations is an immutable collection of data from campus locations. It stores two maps:
	//		a map of IDs, with their data, and a map of buildings, with their ID. Every location name has
	//		an ID but not every ID has a location name because some IDs are for intersections.
	//		
	//	Representation Invariant for every CampusLocations l:
	//		dataFromIDs != null
	//		buildings != null
	
	/**
	 * Creates a CampusLocations object with ID data and campus buildings
	 * 
	 * @param dataFromIDs Map of ids with data
	 * @param buildings Map of buildings with ids
	 * @effects Creates a CampusLocations object
	 */
	public CampusLocations(Map<String,ArrayList<String>> dataFromIDs, Map<String,String> buildings) {
		this.dataFromIDs = dataFromIDs;
		this.buildings = buildings;
		checkRep();
	}
	
	/**
	 * Tries to return the ID for a given building name
	 *   
	 * @param name The building name
	 * @return the ID of the building name, or null if there is no such building name
	 */
	public String getID(String name) { // returns id from name
		if (buildings.get(name) == null) return null;
		return new String(buildings.get(name));
	}
	
	/**
	 * Tries to return the name for a given ID
	 * 
	 * @param id The id of a building or intersection
	 * @return the name of the specified ID, or null if there is no such ID
	 */
	public String getName(String id) { // returns name from id
		if (dataFromIDs.get(id) == null) return null;
		return new String(dataFromIDs.get(id).get(0));
	}
	
	/**
	 * Tries to return the x coordinate for a given ID
	 * 
	 * @param id The id of a building or intersection
	 * @return the x coordinate of the specified ID, or null if there is no such ID
	 */
	public String getX(String id) {
		if (dataFromIDs.get(id) == null) return null;
		return new String(dataFromIDs.get(id).get(1));
	}
	
	/**
	 * Tries to return the y coordinate for a given ID
	 * 
	 * @param id The id of a building or intersection
	 * @return the y coordinate of the specified ID, or null if there is no such ID
	 */
	public String getY(String id) {
		if (dataFromIDs.get(id) == null) return null;
		return new String(dataFromIDs.get(id).get(2));
	}
	
	/**
	 * Returns the direction one would have to walk to go from id1 to id2
	 * 
	 * @param id1 The starting ID
	 * @param id2 The ending ID
	 * @return the direction one must travel to reach id2 from id1
	 */
	public String getDirection(String id1, String id2) { // returns direction from id1 to id2
		Double angle = Math.toDegrees((Math.atan2(Double.parseDouble(getY(id2)) - Double.parseDouble(getY(id1)), Double.parseDouble(getX(id2)) - Double.parseDouble(getX(id1)))) + (Math.PI/2.0));
		if (angle < 0) angle += (double) 360;
		if (angle < (double) 22.5) return new String("North");
		else if (angle < (double) 67.5) return new String("NorthEast");
		else if (angle < (double) 112.5) return new String("East");
		else if (angle < (double) 157.5) return new String("SouthEast");
		else if (angle < (double) 202.5) return new String("South");
		else if (angle < (double) 247.5) return new String("SouthWest");
		else if (angle < (double) 292.5) return new String("West");
		else if (angle < (double) 337.5) return new String("NorthWest");
		else return new String("North");
	}
	
	/**
	 * Lists all stored buildings 
	 * 
	 * @return a string of all buildings in the repeating order: "name,id\n"
	 */
	public String listAllBuildings() { // lists all buildings
		Iterator<String> data_iter = dataFromIDs.keySet().iterator();
		TreeSet<String> buildings_set = new TreeSet<String>();
		String buildings_string = new String();
		while (data_iter.hasNext()) {
			String id = data_iter.next();
			ArrayList<String> data = dataFromIDs.get(id);
			if (!data.get(0).equals("")) buildings_set.add(String.format("%s,%s\n", data.get(0),id));
		}
		Iterator<String> building_iter = buildings_set.iterator();
		while (building_iter.hasNext()) buildings_string += building_iter.next();
		return buildings_string;
	}
	
	/**
	 * Returns an array of all the buildings
	 * 
	 * @param departureMenu A boolean value for whether the menu is for departure or destination
	 * @return a string array of all buildings
	 */
	public String[] getBuildings(boolean departureMenu) { 
		String[] buildingStrings = new TreeSet<String>(buildings.keySet()).toArray(new String[buildings.size()]);
		if (departureMenu) buildingStrings[0] = "Select Depature";
		else buildingStrings[0] = "Select Destination";
		return buildingStrings;
	}
	
	private void checkRep() throws RuntimeException {
		if (dataFromIDs == null) throw new RuntimeException("dataFromIDs must not be null");
		if (buildings == null) throw new RuntimeException("buildings must not be null");
	}
	
}

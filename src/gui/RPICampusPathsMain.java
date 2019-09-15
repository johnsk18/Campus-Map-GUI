package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import campus.CampusWrapper;

//	RPICampusPathsMain is not an ADT. It represents the view and controller for finding and painting
//	a route between two selected buildings on an RPI Campus map image.

public class RPICampusPathsMain {
	private MapPanel mapPanel;
	private JComboBox<String> departureMenu;
	private JComboBox<String> destinationMenu;
	private String clickedPath1;
	
	// The listener for the GUI buttons
	private class ButtonListener implements ActionListener {
		private String id;
		private CampusWrapper graph;
		
		/**
		 * Creates a ButtonListener object
		 * 
		 * @param id The name of the action
		 * @param graph The campus graph
		 * @effects Creates a ButtonListener object
		 */
		public ButtonListener(String id, CampusWrapper graph) {
			this.id = id;
			this.graph = graph;
		}
		
		/**
		 * Performs events based on the id
		 * 
		 * @modifies mapPanel
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (id.equals("Menu")) { // Opens the Route Menu frame
				loadRouteFrame(graph);
			} else if (id.equals("Route")) { // Calculates and draws a route between two selected buildings
				mapPanel.clearDrawings();
				String id1 = graph.getLocations().getID((String) departureMenu.getSelectedItem());
				String id2 = graph.getLocations().getID((String) destinationMenu.getSelectedItem());
				if (id1 == null || id2 == null) return;
				String path = graph.findPath(id1, id2);
				pathParser(id1,id2,path,graph);
				mapPanel.animate();
			} else if (id.equals("ZoomIn")) { // Zooms in by 50 pixels
				mapPanel.zoomIn();
				mapPanel.repaint();
			} else if (id.equals("ZoomOut")) { // Zooms out by 50 pixels
				mapPanel.zoomOut();
				mapPanel.repaint();
			} else { // Resets the map
				mapPanel.clearDrawings();
				mapPanel.repaint();
			}
		}
	}
	
	// The listener for mouse events
	private class MapMouseListener extends MouseAdapter implements MouseListener, MouseMotionListener {
		private CampusWrapper graph;
		private int start_x;
		private int start_y;
		
		/**
		 * Creates a MapMouseListener object
		 * 
		 * @param graph The campus graph
		 * @effects Creates a MapMouseListener object
		 */
		public MapMouseListener(CampusWrapper graph) {
			this.graph = graph;
		}
		
		/**
		 * Selects buildings and draws route if two buildings are selected by mouse clicking on appropriate map area
		 * 
		 * @modifies clickedPath1, mapPanel
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			int x_difference = mapPanel.getWidth() - mapPanel.getMapSize();
			int y_difference = mapPanel.getHeight() - mapPanel.getMapSize();
			int min_difference = Math.min(x_difference, y_difference);
			int	new_x = (int) (x_difference/2.0 - min_difference/2.0);
			int	new_y = (int) (y_difference/2.0 - min_difference/2.0);
			double scale = mapPanel.getMapSize() / (double) (mapPanel.getMapSize() + min_difference);
			double clickX = (((e.getX() - new_x) * 2.5) * scale);
			double clickY = (((e.getY() - new_y) * 2.5) * scale);
			Iterator<String> iter = Arrays.stream(graph.getLocations().getBuildings(true)).iterator();
			while (iter.hasNext()) { // Iterates through all buildings 
				String name = iter.next();
				if (graph.getLocations().getID(name) != null) {
					String id = graph.getLocations().getID(name);
					double idX = Integer.parseInt(graph.getLocations().getX(id));
					double idY = Integer.parseInt(graph.getLocations().getY(id));		
					if (idX - 14*scale <= clickX && clickX <= idX + 14*scale && idY - 14*scale <= clickY && clickY <= idY + 14*scale) { // Checks to see is mouse clicked on building id area
						if (clickedPath1.equals("")) { // sets building to be the departure
							clickedPath1 = id;
							mapPanel.clearDrawings();
							mapPanel.repaint();
							mapPanel.drawEndpoint(0, (int) idX, (int) idY);
							mapPanel.repaint();
						} else { // sets building to be the destination, calculates the path, draws the path, and resets the departure string variable 
							mapPanel.drawEndpoint(1, (int) idX, (int) idY);
							mapPanel.repaint();
							pathParser(clickedPath1,id,graph.findPath(clickedPath1, id),graph);
							mapPanel.animate();
							clickedPath1 = "";
						}
						break;
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		/**
		 * Sets starting mouse coordinates
		 * 
		 * @modifies start_x, start_y
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			start_x = e.getX();
			start_y = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
		/**
		 * Drags map with cursor
		 * 
		 * @modifies mapPanel, start_x, start_y
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			mapPanel.zoomMove((int)((start_x - e.getX())/1.0), (int)((start_y - e.getY())/1.0));
			mapPanel.repaint();
			start_x = e.getX();
			start_y = e.getY();
		}
		
	}
	
	/**
	 * Parses path for the route between id1 and id2, and draws or shows a dialog accordingly
	 * 
	 * @param id1 The departure id
	 * @param id2 The destination id
	 * @param path The string to be parsed
	 * @param graph The campus graph
	 * @modifies mapPanel
	 */
	private void pathParser(String id1, String id2, String path, CampusWrapper graph) {
		if (path.equals("0.000")) { // for path to self
			mapPanel.setDraw(Integer.parseInt(graph.getLocations().getX(id1)), Integer.parseInt(graph.getLocations().getY(id1)), Integer.parseInt(graph.getLocations().getX(id2)), Integer.parseInt(graph.getLocations().getY(id2)));
			return;
		}
		int i = path.indexOf(",");
		if (i == -1) { // no path found
			JOptionPane.showMessageDialog(new JFrame(), "No path could be drawn.");
		} else { // prints path
			String id3 = path.substring(0,i);
			mapPanel.setDraw(Integer.parseInt(graph.getLocations().getX(id1)), Integer.parseInt(graph.getLocations().getY(id1)), Integer.parseInt(graph.getLocations().getX(id3)), Integer.parseInt(graph.getLocations().getY(id3)));
			int j = path.indexOf(",",i+1);
			while (i != -1) { // iterates through path string, printing each line
				if (j != -1) { // if path has not ended
					String id4 = path.substring(i+1,j);
					mapPanel.setDraw(Integer.parseInt(graph.getLocations().getX(id3)), Integer.parseInt(graph.getLocations().getY(id3)), Integer.parseInt(graph.getLocations().getX(id4)), Integer.parseInt(graph.getLocations().getY(id4)));
					id3 = id4;
				} 
				i = j;
				j = path.indexOf(",",i+1);
			}
		}
	}
	
	/**
	 * Creates the main campus GUI frame
	 * 
	 * @param graph The campus graph
	 * @modifies clickedPath1, mapPanel
	 */
	public void loadMapFrame(CampusWrapper graph) {
		// Initializing variables and components
		clickedPath1 = "";
	    JFrame frame = new JFrame("RPI Campus Paths");
	    String imagePath = "src/gui/data/RPI_campus_map_2010_extra_nodes_edges.png";
	    mapPanel = new MapPanel(imagePath);
	    JPanel buttonPanel = new JPanel();
	    mapPanel.setPreferredSize(new Dimension(mapPanel.getMapSize(),mapPanel.getMapSize()));
	    MapMouseListener mapMouseListener = new MapMouseListener(graph);
	    mapPanel.addMouseListener(mapMouseListener);
	    mapPanel.addMouseMotionListener(mapMouseListener);
	    JButton routeButton = new JButton("Route");
	    routeButton.addActionListener(new ButtonListener("Menu",graph));
	    JButton resetButton = new JButton("Reset");
	    resetButton.addActionListener(new ButtonListener("Reset",null));
	    JButton zoomInButton = new JButton("ZoomIn");
	    zoomInButton.addActionListener(new ButtonListener("ZoomIn",null));
	    JButton zoomOutButton = new JButton("ZoomOut");
	    zoomOutButton.addActionListener(new ButtonListener("ZoomOut",null));
	    // Adds to GUI
	    frame.add(mapPanel,BorderLayout.CENTER);
	    frame.add(buttonPanel,BorderLayout.NORTH);
	    buttonPanel.add(routeButton);
	    buttonPanel.add(resetButton);
	    buttonPanel.add(zoomInButton);
	    buttonPanel.add(zoomOutButton);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true); 
	}
	
	/**
	 * Creates the building selection frame
	 * 
	 * @param graph The campus graph
	 * @modifies departureMenu, destinationMenu
	 */
	public void loadRouteFrame(CampusWrapper graph) {
		// Initializes components
		JFrame frame = new JFrame("Select Buildings");
		departureMenu = new JComboBox<String>(graph.getLocations().getBuildings(true));
		destinationMenu = new JComboBox<String>(graph.getLocations().getBuildings(false));
	    JButton routeButton = new JButton("Draw Route");
	    routeButton.addActionListener(new ButtonListener("Route",graph));
		// Adds to Route GUI
		frame.add(departureMenu,BorderLayout.NORTH);
		frame.add(destinationMenu,BorderLayout.CENTER);
		frame.add(routeButton,BorderLayout.SOUTH);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true); 
	}
	
	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Initializing graph and map
		String nodeArg = "src/campus/data/RPI_map_data_Nodes.csv";
		String edgeArg = "src/campus/data/RPI_map_data_Edges.csv";
		CampusWrapper graph = new CampusWrapper(nodeArg,edgeArg);
		RPICampusPathsMain main = new RPICampusPathsMain();
		main.loadMapFrame(graph);
	}
}

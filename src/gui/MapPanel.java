package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;


//	MapPanel is not an ADT, but a subtype of JPanel. It represents the panel used to contain
//	the campus map image. It is apart of the Model for this GUI program as it deals with the
//	calculations behind the painting
class MapPanel extends JPanel {
	private static final long serialVersionUID = -4067234593881149800L;
	private String imagePath;
	private BufferedImage img;
	private ArrayList<Integer> coords;
	private boolean draw;
	private boolean unzoom;
	private boolean change_x;
	private double b;
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double slope;
	private int iter;
	private int x_zoom;
	private int y_zoom;
	private int w_zoom;
	private int h_zoom;
	private int mapSize;
	
	/**
	 * Constructs a MapPanel object
	 * 
	 * @param path The path of the image
	 * @throws IOException
	 * @modifies imagePath
	 * @effects Creates a MapPanel object
	 */
	public MapPanel(String path) {
		imagePath = path;
		clearDrawings();
	}
	
	/**
	 * 
	 * @return whether the path is still being drawn or not
	 */
	public boolean getDraw() { return draw; }
	
	/**
	 * Sets iter to 0 and empties elements in coords, if and if only draw is true
	 * 
	 * @modifies  unzoom, iter, coords, img, x_zoom, y_zoom, w_zoom, h_zoom, mapSize
	 */
	public void clearDrawings() {
		if (draw) return;
		unzoom = true;
		iter = 0;
		coords = new ArrayList<Integer>();
		try { // obtains the campus map image and initializes zoom variables 
			BufferedImage full_img = ImageIO.read(new File(imagePath));
			img = full_img.getSubimage(0, 0, full_img.getWidth(), full_img.getWidth());
			x_zoom = 0;
			y_zoom = 0;
			w_zoom = img.getWidth();
			h_zoom = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapSize = (int)(img.getWidth()*(30/75.0));
	}
	
	/**
	 * 
	 * @return The size of the map in pixels
	 */
	public int getMapSize() { return mapSize; }
	
	/**
	 * Sets draw to true and adds coordinates to coords
	 * 
	 * @modifies draw, coords
	 * @param x_1 First x coordinate
	 * @param y_1 First y coordinate
	 * @param x_2 Second x coordinate
	 * @param y_2 Second y coordinate
	 */
	public void setDraw(int x_1, int y_1, int x_2, int y_2) { 
		draw = true;
		coords.add((int)(x_1));
		coords.add((int)(y_1));
		coords.add((int)(x_2));
		coords.add((int)(y_2));
	}
	
	/**
	 * 
	 * @return the starting zoomed x
	 */
	public int getZoomX() { return x_zoom; }
	
	/**
	 * 
	 * @return the starting zoomed y
	 */
	public int getZoomY() { return y_zoom; }
	
	/**
	 * Drags the zoomed map
	 * 
	 * @param x The dragged x difference
	 * @param y The dragged y difference
	 * @modifies unzoom, x_zoom, y_zoom
	 */
	public void zoomMove(int x, int y) {
		unzoom = false;
		if (x > 0) x_zoom = Math.min(x_zoom + x, img.getWidth() - w_zoom);
		if (y > 0) y_zoom = Math.min(y_zoom + y, img.getHeight() - h_zoom);
		if (x < 0) x_zoom = Math.max(x_zoom + x, 0);
		if (y < 0) y_zoom = Math.max(y_zoom + y, 0);
	}
	
	/**
	 * If the width & height are greater than 75 pixels, zooms in by 50 pixels
	 * 
	 * @modifies unzoom, x_zoom, y_zoom, w_zoom, h_zoom
	 */
	public void zoomIn() {
		unzoom = false;
		if (w_zoom <= 75 || h_zoom <= 75) return;
		x_zoom += 50;
		y_zoom += 50;
		w_zoom = Math.max(w_zoom - 100, 75);
		h_zoom = Math.max(h_zoom - 100, 75);
	}
	
	/**
	 * If the map is has been zoomed in, zooms out by 50 pixels
	 * 
	 * @modifies unzoom, x_zoom, y_zoom, w_zoom, h_zoom
	 */
	public void zoomOut() {
		unzoom = false;
		x_zoom = Math.max(x_zoom - 50, 0);
		y_zoom = Math.max(y_zoom - 50, 0);
		w_zoom = Math.min(w_zoom + 100, img.getWidth() - x_zoom);
		h_zoom = Math.min(h_zoom + 100, img.getHeight() - y_zoom);
	}
	
	/**
	 * Calculates the variables needed to zoom on the generated route
	 * 
	 * @modifies unzoom, x_zoom, y_zoom, w_zoom, h_zoom 
	 */
	public void calculateZoomVariables() {
		unzoom = false;
		int x_min = coords.get(0);
		int y_min = coords.get(1);
		int x_max = coords.get(coords.size()-2);
		int y_max = coords.get(coords.size()-1);
		for (int i = 0; i < coords.size(); i+=2) { // Obtains the max and min x and y values
			if (x_min > coords.get(i)) x_min = coords.get(i);
			if (x_max < coords.get(i)) x_max = coords.get(i);
			if (y_min > coords.get(i+1)) y_min = coords.get(i+1);
			if (y_max < coords.get(i+1)) y_max = coords.get(i+1);
		}
		int x_difference = Math.abs(x_max - x_min);
		int y_difference = Math.abs(y_max - y_min);
		if (x_difference > y_difference) { // compensates for y to make zoom not distorted
			w_zoom = Math.abs(x_max - x_min) + 60;
			h_zoom = w_zoom;
			x_zoom = x_min - 30;
			y_zoom = y_min - (int)((x_difference - y_difference) / 2.0) - 30;
		} else if (x_difference < y_difference){ // compensates for x to make zoom not distorted
			h_zoom = Math.abs(y_max - y_min) + 60;
			w_zoom = h_zoom;
			x_zoom = x_min - (int)((y_difference - x_difference) / 2.0) - 30;
			y_zoom = y_min - 30;
		} else { // zooms in on path to self
			x_zoom = x_min - 30;
			y_zoom = y_min - 30;
			w_zoom = 60;
			h_zoom = 60;
		}
	}
	
	/**
	 * Delays drawing each line by incrementing by the pixel every 1 millisecond
	 * 
	 * @modifies iter, x1, y1, x2, y2
	 */
	public void animate() {
		calculateZoomVariables();
		if (coords.isEmpty()) return;
		updateRouteVariables();
		if (x1 == coords.get(iter+2) && y1 == coords.get(iter+3)) { // if path to self, repaints and exits
			repaint();
			return;
		}
		Timer timer = new Timer(0, new ActionListener() { // delays each pixel drawing to looked animated
			
			/**
			 * Iterates to the next pixel value in the path
			 * 
			 * @modifies iter, x1, y1, x2, y2
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				if ((change_x && x2 == coords.get(iter+2)) || (!change_x && y2 == coords.get(iter+3))) { // if end of line has been reached 
					iter += 4;
					if (iter >= coords.size()) { // if end of the entire path has been reached
						Timer sourceTimer = (Timer) e.getSource();
						sourceTimer.stop();
					} else {
						updateRouteVariables();
					}
				} else { // updates route variables
					if (change_x) { // if incrementing by x
						if (x1 < coords.get(iter+2)) { // if target x is greater
							++x1;
							++x2;
						} else {
							--x1;
							--x2;
						}
						y1 = y2;
						y2 = slope*x2 + b;
					} else { // if incrementing by y
						if (y1 < coords.get(iter+3)) { // if target y is greater
							++y1;
							++y2;
						} else {
							--y1;
							--y2;
						}
						x1 = x2;
						x2 = (y2 - b) / slope;
					}
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
	
	/**
	 * Updates the variables after a line is finished being drawn
	 * 
	 * @modifies x1, y1, x2, y2, b, slope, change_x
	 */
	private void updateRouteVariables() {
		x1 = coords.get(iter);
		y1 = coords.get(iter+1);
		slope = (coords.get(iter+3) - coords.get(iter+1)) / (double) (coords.get(iter+2) - coords.get(iter));
		b = y1 - (slope*x1);
		if (Math.abs(coords.get(iter+3) - y2) > Math.abs(coords.get(iter+2) - x2)) { // iterates by y
			change_x = false;
			if (y1 < coords.get(iter+3)) y2 = y1 + 1;
			else y2 = y1 - 1;
			x2 = (y2 - b) / slope;
		} else { // iterates by x
			change_x = true;
			if (x1 < coords.get(iter+2)) x2 = x1 + 1;
			else x2 = x1 - 1;
			y2 = slope*x2 + b;
		}
	}
	 /**
	  * Draws a circle around the route endpoint
	  * 
	  * @param color 0 for green, 1 for red, 2 for yellow
	  * @param x The x coordinate
	  * @param y The y coordinate
	  */
	public void drawEndpoint(int color, int x, int y) {
		Graphics2D g2 = img.createGraphics();
		g2.setStroke(new BasicStroke(10));
		if (color == 0) {
			g2.setColor(Color.green);
		} else if (color == 1){
			g2.setColor(Color.red);
		} else {
			g2.setColor(Color.yellow);
		}
		g2.drawOval(x - 15, y - 15, 30, 30);
	}
	
	// Paints to the map
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x_difference = getWidth() - mapSize;
		int y_difference = getHeight() - mapSize;
		int min_difference = Math.min(x_difference, y_difference);
		int	new_x = (int) (x_difference/2.0 - min_difference/2.0);
		int	new_y = (int) (y_difference/2.0 - min_difference/2.0);
		if (draw) { // if drawing
			Graphics2D g2 = img.createGraphics();
			g2.setStroke(new BasicStroke(10));
			if (iter == 0) { // draws departure endpoint
				if ((coords.get(iter).equals(coords.get(iter+2))) && (coords.get(iter+1).equals(coords.get(iter+3)))) { // if path to self
					drawEndpoint(2,coords.get(iter),coords.get(iter+1));
					draw = false;
				} else drawEndpoint(0,coords.get(iter),coords.get(iter+1));
			}
			g2.setColor(Color.black);
			g2.draw(new Line2D.Double(x1, y1, x2, y2));
			if (iter == coords.size()) { // draws destination endpoint
				drawEndpoint(1,coords.get(iter-2),coords.get(iter-1));
				draw = false;
			}
		} 
		if (!unzoom)((Graphics2D) g).drawImage(img.getSubimage(Math.max(x_zoom, 0), Math.max(y_zoom, 0), Math.min(w_zoom, img.getWidth() - x_zoom), Math.min(h_zoom, img.getHeight() - y_zoom)), new_x, new_y, mapSize + min_difference, mapSize + min_difference, this);
		else ((Graphics2D) g).drawImage(img, new_x, new_y, mapSize + min_difference, mapSize + min_difference, this);
	}
}
package define_parts_ui.model;

/**
 * XYCoordinate stores (X,Y) coordinates of SMT parts on PNP workspace and simplifies coordinate calculations
 * @author Justin Johnson
 *
 */
public class XYCoordinate {

	/**
	 * Constructor assigns x and y coordinates
	 * @param double x
	 * @param double y
	 */
	public XYCoordinate(double x, double y) {
		xCoordinate = x;
		yCoordinate = y;
	}
	
	/**
	 * Constructor assigns x and y coordinates
	 * @param int x
	 * @param int y
	 */
	public XYCoordinate(int x, int y){
		xCoordinate = (double) x;
		yCoordinate = (double) y;
	}
	
	/**
	 * Get X Coordinate
	 * @return double xCoordinate
	 */
	public double getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * set xCoordiante
	 * @param double xCoordinate
	 */
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * set xCoordiante
	 * @param int xCoordinate
	 */
	public void setxCoordinate(int xCoordinate){
		this.xCoordinate = (double) xCoordinate;
	}
	
	/**
	 * Get Y Coordinate
	 * @return double yCoordinate
	 */
	public double getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * Set Y Coordinate
	 * @param double yCoordinate
	 */
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * Set Y Coordinate
	 * @param int yCoordinate
	 */
	public void setyCoordinate(int yCoordinate){
		this.yCoordinate = (double) yCoordinate;
	}

	/**
	 * Print coordinates (x, y) to console
	 */
	public void print(){
		System.out.println("(" + xCoordinate + ", " + yCoordinate + ")");
	}


	private double xCoordinate;
	private double yCoordinate;
	
}

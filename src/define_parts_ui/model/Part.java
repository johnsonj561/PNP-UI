package define_parts_ui.model;


public class Part {

	public Part(int id, String fp, String val, double xi, double yi, double xf, double yf, String c) {
		partID = id;
		footprint = fp;
		value = val;
		xInitial = xi;
		yInitial = yi;
		xFinal = xf;
		yFinal = yf;
		count = Integer.parseInt(c);
		partFeatureString = makePartFeatureString();
	}
	
	/**
	 * Creates a comma separated list from the part's features
	 * @return String of comma separated part features
	 */
	private String makePartFeatureString(){
		return partID + ", " + footprint + ", " + value + ", " + xInitial + ", " + yInitial + ", " +
				xFinal + ", " + yFinal + ", " + count;	
	}
	
	/**
	 * Print part features to screen in comma separated list
	 */
	public void print(){
		System.out.println(partFeatureString);
	}
	
	/**
	 * Return copy of parts features in a comma separated String
	 * @return
	 */
	public String getPartFeatureString(){
		return partFeatureString;
	}
	
	/**
	 * Get current part ID
	 * @return int partID
	 */
	public int getPartID() {
		return partID;
	}

	/**
	 * Get part's assigned footprint
	 * @return String footprint
	 */
	public String getFootprint() {
		return footprint;
	}

	/**
	 * Set part's footprint
	 * @param footprint
	 */
	public void setFootprint(String footprint) {
		this.footprint = footprint;
	}
	
	/**
	 * Get value of part
	 * @return String value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set value of part
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Get X coordinate of first part in part row
	 * @return double xInitial
	 */
	public double getxInitial() {
		return xInitial;
	}

	/**
	 * Set X coordinate of first part in row
	 * @param xInitial
	 */
	public void setxInitial(double xInitial) {
		this.xInitial = xInitial;
	}

	/**
	 * Get Y coordinate of first part in row
	 * @return double yInitial
	 */
	public double getyInitial() {
		return yInitial;
	}

	/**
	 * Set Y coordinate of first part in row
	 * @param yInitial
	 */
	public void setyInitial(double yInitial) {
		this.yInitial = yInitial;
	}
	
	/**
	 * Get X coordinate of last part in row
	 * @return double xFinal
	 */
	public double getxFinal() {
		return xFinal;
	}

	/**
	 * Set X coordinate of last part in row
	 * @param xFinal
	 */
	public void setxFinal(double xFinal) {
		this.xFinal = xFinal;
	}

	/**
	 * Get Y coordinate of last part in row
	 * @return double yFinal
	 */
	public double getyFinal() {
		return yFinal;
	}

	/**
	 * Set Y coordinate of last part in row
	 * @param yFinal
	 */
	public void setyFinal(double yFinal) {
		this.yFinal = yFinal;
	}

	/**
	 * Get count = total number of parts on tape
	 * @return int count = total number of parts on tape in present row
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Set count = total number of parts on tape
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	//part features
	private int partID;
	private String footprint;
	private String value;
	private double xInitial;
	private double yInitial;
	private double xFinal;
	private double yFinal;
	private int count;
	//comma separated String of part features
	private String partFeatureString;
	
}

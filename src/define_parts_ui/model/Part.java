package define_parts_ui.model;

/**
 * Part class is used with PNP machine to define location of parts and their orientation
 * Each part object defines a reel or a tape by registering the 1st and last part on tape
 * Given 1st and last coordinates, and total number of parts in row, coordinates of all subsequent parts are calculated
 * @author Justin Johnson
 *
 */
public class Part {

	public Part(int id, String fp, String val, String pitch, double xi, double yi, double xf, double yf, String c, int v) {
		partID = id;
		footprint = fp;
		value = val;
		cavityPitch = Integer.parseInt(pitch);
		xInitial = xi;
		yInitial = yi;
		xFinal = xf;
		yFinal = yf;
		count = Integer.parseInt(c);
		visionRequired = v;
		//create a String of all part features
		partFeatureString = makePartFeatureString();
		//create an array of coordinates where each cell stores a part's (x,y) location
		xyCoordinates = new XYCoordinate[count];
		validPartLocations = calculatePartCoordinates();
		//nextAvailablePart will start as the last part
		nextAvailablePart = count-1;
		//calculate angle of part
		theta = calculateRotation();
		theta = (double)Math.round(theta * 100d) / 100d;
		printCoordinates();
	}

	/**
	 * Updates part count, re-calculates coordinates of individual components, and re-calculates theta
	 * Intended to be called after a PNP Job is complete, allowing machine to continue using same part's files
	 */
	public void updatePartCount(){
		//nextAvailablePart was set to count -1 and decremented as components were used
		//we update the count to = the ith part
		if(nextAvailablePart < 0){
			//TODO
			//Notify User that there are no more parts left!
		}
		else{
			count = nextAvailablePart + 1;
			xFinal = xyCoordinates[nextAvailablePart].getxCoordinate();
			yFinal = xyCoordinates[nextAvailablePart].getyCoordinate();
			validPartLocations = calculatePartCoordinates();
			theta = calculateRotation();
			System.out.println("Part -> updatePartCount() complete. Printing coordinates of new parts:");
			printCoordinates();
		}
		
	}


	/**
	 * Calculate orientation of part
	 * Assume that part is laid horizontally along x-axis if deltaX is greater than deltaY
	 * @return double theta
	 */
	private double calculateRotation(){
		//if working with a reel, no theta to calculate
		if(isReel){
			return 0;
		}
		/* If deltaX is greater than deltaY, then part tape is laid horizontal
		 *  Calculate angle about x axis
		 *  If yFinal is less yInitial, then part is rotated CW
		 *  If yFinal is greater than yInitial, then part is rotated CCW
		 */
		double dx = xFinal - xInitial;
		double dy = yFinal - yInitial;
		if(dx >= dy){
			if(yFinal < yInitial){
				return Math.atan(dy/dx);
			}
			else{
				return (-1)*Math.atan(dy/dx);
			}
		}
		else{
			if(xFinal < xInitial){
				return Math.atan(dx/dy);
			}
			else{
				return (-1)*Math.atan(dx/dy);
			}
		}
	}

	/**
	 * Get coordinates of next available part and increment nextAvailablePart counter
	 * @return XYCoordinate of next available part
	 */
	public XYCoordinate getNextPartLocation(){
		//if there are no more parts to use
		if(nextAvailablePart < 0){
			return null;
		}
		else{
			XYCoordinate nextPart = xyCoordinates[nextAvailablePart];
			//if we used the last part, set availablePart to -1
			if(--nextAvailablePart < 0){
				nextAvailablePart = -1;
			}
			return nextPart;
		}
	}

	/**
	 * Creates a comma separated list from the part's features
	 * @return String of comma separated part features
	 */
	private String makePartFeatureString(){
		return partID + ", " + footprint + ", " + value + ", " + cavityPitch + ", " + xInitial + ", " + yInitial + ", " +
				xFinal + ", " + yFinal + ", " + count + ", " + visionRequired;	
	}

	/**
	 * Print part features to screen in comma separated list
	 */
	public void print(){
		System.out.println(makePartFeatureString());
	}

	/**
	 * Returns true if part locations are valid.
	 * Part locations are valid if the expected position matches the calculated position
	 * @return validPartLocation
	 */
	public boolean isValidPartPosition(){
		return validPartLocations;
	}


	/**
	 * Calculate the (x,y) position of every part on this tape of parts
	 * Part positions are calculated by creating a line between 1st part and last part
	 * Parts are found on this line, spaced by equal distance of cavity pitch
	 * @return true if calculated last part position matches expected last part position
	 */
	public boolean calculatePartCoordinates(){
		//if first part position matches last part position - we must be working with a tape reel
		if(xInitial == xFinal && yInitial == yFinal){
			System.out.println("Part -> calculatePartCoordinates(): 1st Part Position = Last Part Position.");
			isReel = true;
			//first and last coordinate is the same
			xyCoordinates = new XYCoordinate[1];
			xyCoordinates[0] = new XYCoordinate(xInitial, yInitial);
			return true;
		}
		isReel = false;
		//we know first and last coordinates
		XYCoordinate firstPartCoordinate = new XYCoordinate(xInitial, yInitial);
		XYCoordinate lastPartCoordinate = new XYCoordinate(xFinal, yFinal);
		double deltaX = xFinal - xInitial;
		double dx = deltaX/(count-1);
		double deltaY = yFinal - yInitial;
		double dy = deltaY/(count-1);
		//we know where first part is - assign it
		xyCoordinates[0] = firstPartCoordinate;
		//for each part on tape, calculate XY coordinate and store in xyCoordinates[] array
		//we are going to use calculated cavity pitch for now because the machine's mm is not accurate
		//double calculatedCavityPitch = (Math.sqrt(deltaX*deltaX + deltaY*deltaY))/(count-1);
		for(int i = 1; i < count; i++){
			//double x = xInitial + Math.cos(theta)*i*calculatedCavityPitch;
			double x = xInitial + i*dx;
			x = Math.round(x*1000);
			x /= 1000.0;
			//double y = yInitial + Math.sin(theta)*i*calculatedCavityPitch;
			double y = yInitial + i*dy;
			y = Math.round(y*1000);
			y /= 1000.0;
			xyCoordinates[i] = new XYCoordinate(x, y);
		}
		//if calculated position of last part matches known position of last part, return true
		//known(x,y) - EPSILON < calculated (x,y) < known(x,y) + EPSILON 
		if((Math.abs((xyCoordinates[count-1].getxCoordinate() - lastPartCoordinate.getxCoordinate())) < PartConstants.COORD_EPSILON) &&
				(Math.abs((xyCoordinates[count-1].getyCoordinate() - lastPartCoordinate.getyCoordinate())) < PartConstants.COORD_EPSILON)){	
			System.out.println("Part -> calculatePartCoordinates() -> Part Coordinates Calculated Without Error");
			return true;
		}
		//else calculated last part position and known last part position don't match, error occurred, return false
		System.out.println("Part -> calculatePartCoordinates() -> Calculated position of last part does not" + 
				" match the expected position of last part.");
		return false;
	}

	/**
	 * Print coordinates of all parts found between first part and last part
	 */
	public void printCoordinates(){
		System.out.println("Printing part coordinates:");
		for(int i = 0; i < count; i++){
			xyCoordinates[i].print();
		}
		System.out.println("Printing theta: " + theta);
	}

	/**
	 * Return copy of parts features in a comma separated String
	 * @return
	 */
	public String getPartFeatureString(){
		partFeatureString = makePartFeatureString();
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
	 * Return cavity pitch
	 * @return
	 */
	public int getCavityPitch(){
		return cavityPitch;
	}

	/**
	 * Set cavity pitch
	 * @param pitch
	 */
	public void setCavityPitch(int pitch){
		cavityPitch = pitch;
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

	/**
	 * Get value of vision required (1 = required, 0 = not required)
	 * @return int visionRequired
	 */
	public int getVisionRequired(){
		return visionRequired;
	}
	
	/**
	 * Set value of vision required (1 = required, 0 = not required)
	 * @param int required
	 */
	public void setVisionRequired(int required){
		visionRequired = required;
	}
	
	/**
	 * Return true if defined part is a reel
	 * @return boolean isReel
	 */
	public boolean isReel(){
		return isReel;
	}

	/**
	 * Return the angle theta of part
	 * @return double theta
	 */
	public double getTheta(){
		return theta;
	}


	//part features
	private int partID;
	private String footprint;
	private String value;
	private int cavityPitch;
	private double xInitial;
	private double yInitial;
	private double xFinal;
	private double yFinal;
	private int count;
	private int visionRequired;
	private double theta;
	//comma separated String of part features
	private String partFeatureString;
	//array of coordinates that will store coordinate of each component on this part tape
	private XYCoordinate[] xyCoordinates;
	//flag that represents a tape reel of parts
	//note that on tape parts are found in the same position every time, the reel feeds parts to loading area
	private boolean isReel;
	//boolean that declares true when part calculations are accurate
	private boolean validPartLocations;
	//index of next available part
	private int nextAvailablePart;
}

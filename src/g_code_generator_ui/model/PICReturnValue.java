package g_code_generator_ui.model;

/**
 * PICReturnValue class parses return String from PIC microcontroller and provides basic functionality to simplify
 * message handling.
 * @author Justin Johnson
 *
 */
public class PICReturnValue {

	/**
	 * PICReturnValue validates PIC return message to determine X Y Z coordinates or applicable error message
	 * @param picReturnMessage
	 */
	public PICReturnValue(String picReturnMessage) {
		picReturnString = picReturnMessage;
		//check to see if PIC reply matches Xddd.ddd Yddd.ddd Zddd.ddd
		if(picReturnString.matches(VALID_PIC_REGEX)){
			isErrorMessage = false;
			if(identifyPicCoordinates()){
				System.out.println("PICReturnValue -> Coordinates identified");
				isValidPicReturn = true;
			}
			else{
				System.out.println("PICReturnValue -> Unable to identify coordinates");
				isValidPicReturn = false;
			}
		}
		//check to see if PIC reply matches one of the valid error messages
		else if(picReturnString.contentEquals(INVALID_COMMAND) || picReturnString.contentEquals(INVALID_ARG) ||
				picReturnString.contentEquals(OUT_OF_BOUNDS) || picReturnString.contentEquals(UNKNOWN_ERROR)){
			System.out.println("PICReturnValue -> Error message identified");
			picErrorType = picReturnString;
			isValidPicReturn = true;
			isErrorMessage = true;
		}
		//else we don't recognize PIC reply, it is invalid
		else{
			System.out.println("PICReturnValue -> Unrecognized PIC return value");
			isValidPicReturn = false;
			isErrorMessage = false;
		}	
	}
	
	/**
	 * Parse PIC return value and identify X Y Z coordinates
	 * @return true if PIC message was parsed successfully
	 * @return false if unable to identify PIC return values
	 */
	private boolean identifyPicCoordinates(){
		String[] returnTokens = picReturnString.split("\\s+");
		for(String token : returnTokens){
			char c = token.charAt(0);
			switch(c){
			case 'X':
				xValue = Double.parseDouble(token.replace("X", ""));
				break;
			case 'Y':
				yValue = Double.parseDouble(token.replace("Y", ""));
				break;
			case 'Z':
				zValue = Double.parseDouble(token.replace("Z", ""));
				break;
			default:
				return false;
			}
		}
		//all tokens have been successfully parsed
		return true;
	}

	/**
	 * Get PIC return X coordinate
	 * @return double X
	 */
	public double getxValue() {
		return xValue;
	}
	/**
	 * Get PIC return Y coordinate
	 * @return double Y
	 */
	public double getyValue() {
		return yValue;
	}
	/**
	 * Get PIC return Z coordinate
	 * @return double Z
	 */
	public double getzValue() {
		return zValue;
	}

	/**
	 * Return true if PICs return value is a valid PIC return value
	 * @return boolean isValidPicReturn
	 */
	public boolean isValidPicReturn() {
		return isValidPicReturn;
	}

	/**
	 * Return true if PICs return value is an error message
	 * @return boolean isErrorMessage
	 */
	public boolean isErrorMessage() {
		return isErrorMessage;
	}
	
	/**
	 * Return copy of PIC return value
	 * @return String picReturnString
	 */
	public String getPicReturnValue(){
		return picReturnString;
	}
	
	/**
	 * Return PIC error type
	 * @return String picErrorType
	 */
	public String getErrorType(){
		return picErrorType;
	}
	

	//store original return message
	private String picReturnString;
	//store X Y Z values
	private double xValue;
	private double yValue;
	private double zValue;
	//store error type
	private String picErrorType;
	//flags to declare valid PIC return values and error messages
	private boolean isValidPicReturn;
	private boolean isErrorMessage;
	//define expected PIC error messages
	private final String INVALID_COMMAND = "INVALID COMMAND";
	private final String INVALID_ARG = "INVALID_ARGUMENT";
	private final String OUT_OF_BOUNDS = "OUT OF BOUNDS";
	private final String UNKNOWN_ERROR = "UNKNOWN";
	//regular expression for PIC reply Xddd.ddd Yddd.ddd Zddd.ddd
	private final String VALID_PIC_REGEX = "^X\\d+\\.\\d+\\s*Y\\d+\\.\\d+\\s*Z\\d+\\.\\d+\\s*$";
	
}

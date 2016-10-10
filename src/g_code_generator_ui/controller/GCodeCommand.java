package g_code_generator_ui.controller;

/**
 * Class that parses a single G Code instruction and stores instruction values
 * G1 = linear Move (X, Y, Z, R)
 * G4 = delay (P, R) = milliseconds, S = seconds)
 * G28 = home
 * M10 = vacuum on, M11 = vacuum off
 * X = X coordinate (mm)
 * Y = Y coordinate (mm)
 * Z = Z coordinate (mm)
 * R = R rotation (mm)
 * P = time (ms)
 * S = time (s)
 * F = Feed Rate
 * @author Justin Johnson
 */
public class GCodeCommand {

	/**
	 * 
	 * @param instruction String containing G Code command
	 */
	public GCodeCommand(String instruction) {
		//initialize command types to false until we can identify
		gCommand = false;
		xMove = false;
		yMove = false;
		zMove = false;
		rMove = false;
		pDelay = false;
		sDelay = false;
		fCommand = false;
		mCommand = false;
		//split instruction on spaces
		instructionTokens = instruction.split("\\s+");
		for(String token : instructionTokens){
			identifyToken(token);
		}
		validateInstruction();
	}
	
	private void validateInstruction(){
	//TODO Validate command
		
	}
	
	
	private void identifyToken(String token){
		char c = token.charAt(0);
		switch(c) {
		case 'G':
			gValue = Integer.parseInt(token.replace("G", ""));
			gCommand = true;
			break;
		case 'M':
			mValue = Integer.parseInt(token.replace("M", ""));
			mCommand = true;
			break;
		case 'X':
			xValue = Double.parseDouble(token.replace("X", ""));
			xMove = true;
			break;
		case 'Y':
			yValue = Double.parseDouble(token.replace("Y", ""));
			yMove = true;
			break;
		case 'Z':
			zValue = Double.parseDouble(token.replace("Z",""));
			zMove = true;
			break;
		case 'R':
			rValue = Double.parseDouble(token.replace("R",""));
			rMove = true;
			break;
		case 'P':
			pValue = Double.parseDouble(token.replace("P",""));
			pDelay = true;
			break;
		case 'S':
			sValue = Double.parseDouble(token.replace("S",""));
			sDelay = true;
			break;
		case 'F':
			fValue = Integer.parseInt(token.replace("F",""));
			fCommand = true;
			break;
		default:
			badCommand = true;
		}
	}
	
	/**
	 * Get copy of instruction tokens
	 * @return String[] of G Code Tokens
	 */
	public String[] getInstructionTokens() {
		return instructionTokens;
	}

	/**
	 * Get G value (1, 4, 28, or null)
	 * @return int G value
	 */
	public int getgValue() {
		return gValue;
	}

	/**
	 * Get M value(10, 11, or null)
	 * @return int M value
	 */
	public int getmValue() {
		return mValue;
	}

	/**
	 * Get X coordinate value
	 * @return double X Value
	 */
	public double getxValue() {
		return xValue;
	}

	/**
	 * Get Y coordinate value
	 * @return double Y value
	 */
	public double getyValue() {
		return yValue;
	}

	/**
	 * Get Z coordinate value
	 * @return double z value
	 */
	public double getzValue() {
		return zValue;
	}
	
	/**
	 * Get R rotation value
	 * @return double r rotation value
	 */
	public double getrValue() {
		return rValue;
	}

	/**
	 * Get P value (delay in ms)
	 * @return double P delay in ms
	 */
	public double getpValue() {
		return pValue;
	}
	
	/**
	 * get S Value (delay in s)
	 * @return double S delay in s
	 */
	public double getsValue() {
		return sValue;
	}

	/**
	 * Get F feed rate value
	 * @return int feed rate
	 */
	public int getfValue() {
		return fValue;
	}

	/**
	 * Returns true if instruction contains G Command
	 * @return boolean gCommand
	 */
	public boolean isgCommand() {
		return gCommand;
	}
	/**
	 * Returns true if instruction contains X coordinate
	 * @return boolean xMove
	 */
	public boolean isxMove() {
		return xMove;
	}

	/**
	 * Returns true if instruction contains Y coordinate
	 * @return boolean yMove
	 */
	public boolean isyMove() {
		return yMove;
	}

	/**
	 * Returns true if instruction contains Z coordinate
	 * @return boolean zMove
	 */
	public boolean iszMove() {
		return zMove;
	}
	
	/**
	 * Returns true if instruction contains a R value
	 * @return boolean rMove
	 */
	public boolean isrMove(){
		return rMove;
	}

	/**
	 * Returns true of instruction contains P delay (ms)
	 * @return boolean pDelay
	 */
	public boolean ispDelay() {
		return pDelay;
	}

	/**
	 * Returns true of instruction contains S delay (s)
	 * return boolean sDelay
	 */
	public boolean issDelay() {
		return sDelay;
	}

	/**
	 * Return true if instruction contains F Command
	 * @return boolean fCommand
	 */
	public boolean isfCommand() {
		return fCommand;
	}
	
	/**
	 * Returns true if instruction contains M Command
	 * @return boolean mCommand
	 */
	public boolean ismCommand() {
		return mCommand;
	}

	
	/**
	 * Returns true if G Code command is not valid
	 * @return true if command is invalid or undetected
	 */
	public boolean isBadCommand(){
		return badCommand;
	}
	
	/**
	 * Returns true if G Code command is valid
	 * @return true if command is valid
	 */
	public boolean isValidCommand(){
		return !badCommand;
	}

	//store input g code instruction as array of tokens
	private String[] instructionTokens;
	//store values
	private int gValue;
	private int mValue;
	private double xValue;
	private double yValue;
	private double zValue;
	private double rValue;
	private double pValue;
	private double sValue;
	private int fValue;
	//identify instruction types
	private boolean gCommand;
	private boolean xMove;
	private boolean yMove;
	private boolean zMove;
	private boolean rMove;
	private boolean pDelay;
	private boolean sDelay;
	private boolean fCommand;
	private boolean mCommand;
	//flag to detect bad instruction
	private boolean badCommand;
	
}

package g_code_generator_ui.model;


/**
 * Class that parses a single G Code instruction and stores instruction values
 * G1 = linear Move (X, Y, Z, R)
 * G4 = delay (P, R) = milliseconds, S = seconds)
 * G28 = home
 * G88 = light box
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
	public GCodeCommand(String instruction, double width, double depth, double height) {
		//store boundaries
		maxWidth = width;
		maxDepth = depth;
		maxHeight = height;
		//initialize command types to false until we can identify them properly
		gCommand = false;
		xMove = false;
		yMove = false;
		zMove = false;
		rMove = false;
		pDelay = false;
		sDelay = false;
		fCommand = false;
		mCommand = false;
		isValidCommand = false;
		//remove any comments
		gCodeString = removeComments(instruction);
		//take quick peek at command using regex to check for basic format requirements
		//A 'G' or 'M' followed by 1 or more digits, then 1 or more spaces, followed by 0 or 1 of 
		//X, Y, Z, R, F, P, S, each of which is separated by 0 or more spaces
		String gCodeRegex = "^[G,M]{1}\\d+\\s*(X[+]?\\d+(\\.\\d+)?)?\\s*(Y[+]?\\d+(\\.\\d+)?)?\\s*" +
				"(Z[+]?\\d+(\\.\\d+)?)?\\s*(R[+]?\\d+(\\.\\d+)?)?\\s*(P[+]?\\d+(\\.\\d+)?)?\\s*" +
				"(S[+]?\\d+(\\.\\d+)?)?\\s*(F[+]?\\d+)?\\s*$";
		//if it matches expected G Code Command Format
		if(gCodeString.matches(gCodeRegex)){
			isValidCommand = true;
		}
		//else command must be invalid
		else{
			isValidCommand = false;
			System.out.println("GCodeCommand -> G Code Instruction Failed Format Test: " + gCodeString);
		}
		
		//if we passed fundamental testing - then dig deeper for more validation
		if(isValidCommand){
			instructionTokens = gCodeString.split("\\s+");
			for(String token : instructionTokens){
				identifyToken(token);
			}
			//validateInstruction() will run additional tests and return false if validation fails
			isValidCommand = validateInstruction();
		}
		
		
	}

	/**
	 * Remove G Code comments denoted by ';' character
	 * @param instruction
	 * @return String G code instruction without comments
	 */
	private String removeComments(String instruction){
		if(instruction.contains(";")){
			instruction = instruction.substring(0, instruction.indexOf(";"));
			System.out.println(instruction);
		}
		return instruction;
	}

	/**
	 * Run a set of rules to verify that the G Code instruction is valid.
	 * @return true if instruction is valid
	 * 
	 */
	private boolean validateInstruction(){
		//if we found badCommand in identifyTokens method, no need to proceed any further
		if(!isValidCommand){
			return false;
		}
		//command must contain G1, G4, G28, M10, M11 to be a valid instruction
		//if it doesn't contain a G or M command, it's a bad command
		if(!(isgCommand() || ismCommand())){
			System.out.println("Must contain either a G or a M Command");
			return false;
		}
		//if it is a G command
		if(isgCommand()){
			//1st token must contain the G command
			if(instructionTokens[0].charAt(0) != 'G'){
				System.out.println("1st char not G");
				return false;
			}
			//make sure there is only 1 'G' and there are no 'M's
			for(int i = 1; i < instructionTokens.length; i++){
				//there can only be 1 G character in string
				if(instructionTokens[i].contains("G")){
					System.out.println("Too many Gs");
					return false;
				}
				//there can not be a M command anywhere
				if(instructionTokens[i].contains("M")){
					System.out.println("Can't contain G and M");
					return false;
				}
			}
			//if G28 or G56 command, there should be no values for X Y Z R or F
			if(gValue == 28 || gValue == 56){
				if(xMove || yMove || zMove || rMove || fCommand){
					System.out.println("G28 can not have a value for X Y Z R F");
					return false;
				}
			}
			//if a G4 command there must be 1 and only 1 of P or S values and they must be > 0
			if(gValue == 4){
				//pDelay XNOR sDelay - must have only 1 or the other P or S
				if(!(pDelay ^ sDelay)){
					System.out.println("pDelay" + pDelay);
					System.out.println("sDelay" + sDelay);
				
					System.out.println("G4 must have only 1 delay associated with it, P or S");
					return false;
				}
				//make sure delays are greater than 0
				if(pDelay && pValue < 0){
					System.out.println("P Delay must be greater than 0");
					return false;
				}
				//make sure delays are greater than 0
				if(sDelay && sValue < 0){
					System.out.println("S delay must be greater than 0");
					return false;
				}
			}
			//if G1 then there must be at least 1 of X Y Z R or F
			if(gValue == 1){
				//must be at least one of the X Y Z R or F
				if(!(xMove || yMove || zMove || rMove || fCommand)){
					System.out.println("must be at least one X Y Z R F associated with a G1");
					return false;
				}
				if(xMove || yMove){
					//z move and r move can't exist with x or y. x and y moves can co-exist
					if(zMove || rMove){
						System.out.println("Can't have X Y with a Z or R");
						return false;
					}
				}
				//r moves and z moves must not exist with x y z or each other
				if(rMove && zMove){
					System.out.println("Can't have an R and Z together");
					return false;
				}
				//r moves should not be associated with a feed rate
				if(rMove && fCommand){
					System.out.println("Can't have an R and F together");
					return false;
				}
			}
		}

		//If instruction is an M command, validate
		//Note - M10/M11 is Vacuum On/Off instructions
		//M12/M13 is Lightbox on/off instructions
		if(ismCommand()){
			//1st token must contain the M command
			if(instructionTokens[0].charAt(0) != 'M'){
				System.out.println("1st char 'M' expected but not found");
				return false;
			}
			//make sure there is only 1 'M' and there are no 'G's
			//we start at 1 because we want t skip the first token
			for(int i = 1; i < instructionTokens.length; i++){
				//there can only be 1 M character in string
				if(instructionTokens[i].contains("M")){
					System.out.println("Contains more than 1 M");
					return false;
				}
				//there can not be a G command anywhere
				if(instructionTokens[i].contains("G")){
					System.out.println("Contains M and G");
					return false;
				}
			}
			//there can be no X Y Z R or F
			if(xMove || yMove || zMove || rMove || fCommand || sDelay || pDelay){
				System.out.println("there can't be an X Y Z R S P or F associated with a M");
				return false;
			}
		}
		return true;
	}

	/**
	 * Identify the command stored within token. If invalid command is found, set validComman flag to false
	 * Identifiable Tokens include allowable characters (G, M, X, Y, Z, R, F, P, S) followed by appropriate digits
	 * @param token G Code token to be parsed
	 */
	private void identifyToken(String token){
		String temp = "";
		char c = token.charAt(0);
		//switch on the 1st character of token
		//if 1st character does not match any expected cases, validCommand = false
		switch(c) {
		//Case G should only accept values of G equal to 1, 4, or 28
		case 'G':
			//remove the G check that value for G is valid then assign to gValue
			temp = token.replace("G", "");
			//\\d{1,2} checks for 1 or 2 digits, covers cases G1, G4, G28
			if(temp.matches("^\\d{1,2}$")){
				gValue = Integer.parseInt(temp);
				if(gValue == 1 || gValue == 4 || gValue == 28 || gValue == 56){
					gCommand = true;
				}
				else{
					System.out.println("G value expects a 1, 4, 28, or 56");
					isValidCommand = false;
				}
			}
			else{
				System.out.println("G value expects 1 or 2 digits");
				isValidCommand = false;
			}
			break;
			//Case M should only accept values of M equal to 10 or 11 for vacuum on/off
		case 'M':
			temp = token.replace("M",  "");
			//\\d{2} checks for 2 digits, covers cases M10 and M11
			if(temp.matches("^\\d{2}$")){
				mValue = Integer.parseInt(temp);
				if(mValue == 10 || mValue == 11 || mValue == 12 || mValue == 13){
					mCommand = true;
				}
				else{
					System.out.println("M value expects a 10 or 11");
					isValidCommand = false;
				}
			}
			else{
				System.out.println("M value expects exactly 2 digits");
				isValidCommand = false;
			}
			break;
		//X value expects digits and optional decimal
		case 'X':
			//regex = 0 or 1 '+' signs, 1 or more digits,
			//^ is beginning of string
			// [+]? is 0 or 1 plus symbol
			//\\d+ is 1 or more digits
			//(\\.\\d+)? is 0 or 1 of the substrings found within parenthesis
			//\\.\\d+ is a decimal point followed by 1 or more digits
			//$ is end of line
			temp = token.replace("X", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				xValue = Double.parseDouble(temp);
				xMove = true;
				if(xValue < 0 || xValue > maxWidth){
					isValidCommand = false;
				}
			}
			else{
				System.out.println("X expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//Y value expects digits and optional decimal
		case 'Y':
			temp = token.replace("Y", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				yValue = Double.parseDouble(temp);
				yMove = true;
				if(yValue < 0 || yValue > maxDepth){
					isValidCommand = false;
				}
			}
			else{
				System.out.println("Y expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//Z value expects digits and optional decimal
		case 'Z':
			temp = token.replace("Z", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				zValue = Double.parseDouble(temp);
				zMove = true;
				if(zValue < 0 || zValue > maxHeight){
					isValidCommand = false;
				}
			}
			else{
				System.out.println("Z expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//R value expects digits and optional decimal
		case 'R':
			temp = token.replace("R", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				rValue = Double.parseDouble(temp);
				rMove = true;
			}
			else{
				System.out.println("R expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//P value expects digits and optional decimal
		case 'P':
			temp = token.replace("P", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				pValue = Double.parseDouble(temp);
				pDelay = true;
			}
			else{
				System.out.println("P expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//S value expects digits and optional decimal
		case 'S':
			temp = token.replace("S", "");
			if(temp.matches("^[+]?\\d+(\\.\\d+)?$")){
				sValue = Double.parseDouble(temp);
				sDelay = true;
			}
			else{
				System.out.println("S expects value that contains digits and optional decimal");
				isValidCommand = false;
			}
			break;
		//F value expects digits only, will not accept decimal
		case 'F':
			temp = token.replace("F", "");
			//accepts an optional plus sign followed by 1 or more digits
			if(temp.matches("^[+]?\\d+$")){
				fValue = Integer.parseInt(temp);
				fCommand = true;
			}
			else{
				System.out.println("F expects a value that contains 1 or more digits only");
				isValidCommand = false;
			}
			break;
		//Default is only reached if token does not match any of expected values, set validCommand flag to false
		default:
			isValidCommand = false;
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
	 * Get G value (1, 4, 28, 56, or null)
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
		return !isValidCommand;
	}

	/**
	 * Returns true if G Code command is valid
	 * @return true if command is valid
	 */
	public boolean isValidCommand(){
		return isValidCommand;
	}

	public void printCommand(){
		System.out.println(gCodeString);
	}

	public boolean isG56Command(){
		if(gCommand && gValue == 56){
			return true;
		}
		return false;
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
	private String gCodeString;
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
	//boundaries to check X Y Z against
	private double maxWidth;
	private double maxDepth;
	private double maxHeight;
	//flag to detect bad instruction
	private boolean isValidCommand;

}

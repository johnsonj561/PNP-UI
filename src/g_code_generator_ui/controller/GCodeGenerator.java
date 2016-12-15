package g_code_generator_ui.controller;

import g_code_generator_ui.controller.CentroidParser;
import g_code_generator_ui.model.AltiumSMTComponent;
import g_code_generator_ui.model.EagleSMTComponent;
import java.util.ArrayList;
import java.util.List;
import pnp_main.model.PNPConstants;
import define_parts_ui.controller.PartsFileParser;
import define_parts_ui.model.Part;
import define_parts_ui.model.XYCoordinate;

/**
 * GCodeGenerator class accepts Eagle and Altium centroid files and component part files as input
 * and generates G Code suitable for Pick and Place Machine
 * @author Justin Johnson
 *
 */
public class GCodeGenerator {

	/**
	 * GCodeGenerator constructor assigns centroid and part input files and specifies output file to store G Code
	 * @param centroidPath full path to centroid input file
	 * @param partsPath full path to the parts file
	 * @param fileType specifies Eagle or Altium input type
	 */
	public GCodeGenerator(String centroidPath, String partsPath, int fileType){
		centroidFilePath = centroidPath;
		partsFilePath = partsPath;
		inputFileType = fileType;
		isValidInput = parseInputFiles();
	}	

	/**
	 * Initialize CentroidParser and generate SMTComponent arrays that store all components
	 * being placed on PCB and identify locations of all parts (reels/trays)
	 */
	private boolean parseInputFiles(){
		//flags to detect correct parse job
		boolean centroidParseResult;
		boolean partsParseResult;
		//parser for PCB centroid file
		centroidParser = new CentroidParser(centroidFilePath);
		centroidComponentList = centroidParser.getComponentList();
		//parse for part position file
		partsParser = new PartsFileParser(partsFilePath);
		//generate appropriate array of components to be placed and parts from reels/trays
		if(inputFileType == EAGLE_CENTROID_FILE){
			centroidParseResult = generateEagleComponentArrayFromCentroid();
			partsParseResult = generatePartsArray();
			System.out.println("Centroid File Parsed");
		}
		else if(inputFileType == ALTIUM_CENTROID_FILE){
			centroidParseResult = generateAltiumComponentArrayFromCentroid();
			partsParseResult = generatePartsArray();
			System.out.println("Centroid File Parsed");
		}
		else{
			centroidParseResult = partsParseResult = false;
			System.out.println("Error GCodeGenerator:\nparseInputFilles(): invalid centroid file type");
		}
		return (centroidParseResult && partsParseResult);
	}

	/**
	 * Generates initialization G Code configuration settings to be performed at start of new job and applies them to gCode member
	 * Common start up settings include: units, absolute coordinates, home x coord, home y coord, home z coord, baudrate
	 */
	public String initializeGCode(){
		// display project information
		String gCode =  "; /////////////////////////////////////////////////////////////////////////\n" +
						";                     PIC N PLACE MACHINE                   \n" +
						"; /////////////////////////////////////////////////////////////////////////\n" +
						";                 Florida Atlantic University               \n" +
						";                     Engineering Design 2                  \n" + 
						"; /////////////////////////////////////////////////////////////////////////\n" +
						";                         Design By:\n" +
						";                      Sam Rosenfield, EE\n" +
						";                       Stephen Lyons, EE\n" + 
						";                   Justin Johnson, CE & CS\n" +
						"; /////////////////////////////////////////////////////////////////////////\n\n";
		// initialize settings
		gCode += "\nG1 Z6 ; lift head\n" +
				"G1 F1500 ; Set feed rate for first move\n"	+	
				"G28 ; Home Machine's X and Y axis\n\n";
		return gCode;
	}

	/**
	 * Generate G Code instructions to place parts on PCB
	 * G Code instructions will be applied to class member String gCode
	 */
	public String generateGCode(){		
		String gCode = "";
		if(inputFileType == EAGLE_CENTROID_FILE){
			for(int i = 0; i < eagleInputComponents.length; i++){
				boolean visionRequired = requiresVision(eagleInputComponents[i]);
				gCode += moveToPart(eagleInputComponents[i]);
				gCode += pickUpComponent();
				//gCode += rotateHead(eagleInputComponents[i].getRotation());
				//if alignment through CV is required, move to light box
				if(visionRequired){
					//lift component, move to light box, turn light on
					gCode += "\n; STARTING VISION ROUTINE\n";
					gCode += PNPConstants.LIFT_ABOVE_LIGHTBOX;
					gCode += PNPConstants.MOVE_TO_LIGHTBOX;
					gCode += PNPConstants.LIGHT_ON;
					gCode += PNPConstants.LOWER_TO_LIGHTBOX;
					gCode += "G56 ; CAPTURING IMAGE\n";
					gCode += PNPConstants.LIGHT_OFF;
					gCode += PNPConstants.LIFT_ABOVE_LIGHTBOX;
				}
				gCode += moveToLocation(eagleInputComponents[i].getxCoordinate(), eagleInputComponents[i].getyCoordinate());
				gCode += lowerComponent() + "\n";
			}
		}
		else if(inputFileType == ALTIUM_CENTROID_FILE){
			for(int i = 0; i < altiumInputComponents.length; i++){
				boolean visionRequired = requiresVision(altiumInputComponents[i]);
				gCode += moveToPart(altiumInputComponents[i]);
				gCode += pickUpComponent();
				gCode += rotateHead("0.0");
				//if alignment through CV is required, move to light box
				if(visionRequired){
					//lift component, move to light box, turn light on
					gCode += "\n; STARTING VISION ROUTINE\n";
					gCode += PNPConstants.LIFT_ABOVE_LIGHTBOX;
					gCode += PNPConstants.MOVE_TO_LIGHTBOX;
					gCode += PNPConstants.LIGHT_ON;
					gCode += PNPConstants.LOWER_TO_LIGHTBOX;
					gCode += "G56 ; CAPTURING IMAGE\n";
					gCode += PNPConstants.LIGHT_OFF;
					gCode += PNPConstants.LIFT_ABOVE_LIGHTBOX;
				}
				gCode += moveToLocation(altiumInputComponents[i].getxCoordinate(), altiumInputComponents[i].getyCoordinate());
				// Need altium file's angle of rotation
				// TODO
				gCode += lowerComponent() + "\n";
			}
		}
		else{
			return "Error: Invalid Centroid File Type Provided. Please use Altium or Eagle file types.";
		}
		gCode += "\n\nG28 ; Job Complete, Return To Home";
		tempPartString = updatePartFile();
		return gCode;
	}

	/**
	 * Rotate head to angle
	 * @param theta angle to rotate
	 * @return String gCode instruction to rotate head
	 */
	private String rotateHead(String theta){
		return "G1 R" + theta + " ; rotating head to " + theta + "\n";
	}
	
	/**
	 * Generates G Code commands that move PnP head to location of part identified by component Packate Type
	 * @param component EagleSMTComponent to be picked up from part stack
	 * @return String G Code instructions that move to component's part stack for pick up
	 */
	private String moveToPart(EagleSMTComponent component){
		if(inputFileType == EAGLE_CENTROID_FILE){
			String componentPackageType = component.getPackageType();
			String componentValue = component.getValue();
			for(Part part : partArray){
				if(componentPackageType.contentEquals(part.getFootprint()) 
						&& componentValue.contentEquals(part.getValue())){
					//if we found matching footprint and value available for use, return G Code to This Part
					XYCoordinate coordinates = part.getNextPartLocation();
					if(coordinates != null){
						return "G1 Z6 F300 ; lift head before moving\n" +
								"G1 R0.00 ; rotate head to zero\n" +
								"G1 X" + coordinates.getxCoordinate() + 
								" Y" + coordinates.getyCoordinate() + 
								" F1000 ; move head to next part\n" +
								"G1 R" + part.getTheta() + " ; rotate head to angle of part\n";
					}
				}
			}
		}
		return "\nmoveToPart(EagleSMTComponent) \nPART NOT FOUND\n";
	}
	
	/**
	 * Generates G Code commands that move PnP head to location of part identified by component Ref Designator
	 * @param component AltiumSMTComponent to be picked up from part stack
	 * @return String G Code instructions that move to component's part stack for pick up
	 */
	private String moveToPart(AltiumSMTComponent component){
		if(inputFileType == ALTIUM_CENTROID_FILE){
			String componentRefDesignator = component.getRefDesignator();
			for(Part part : partArray){
				if(componentRefDesignator.contentEquals(part.getFootprint())){
					//if we found part and there is 1 available for use, return G Code to This Part
					XYCoordinate coordinates = part.getNextPartLocation();
					if(coordinates != null){
						return "G1 Z6 F300 ; lift head before moving" +
								"G1 R0.00 ; rotate head to zero\n" +
								"G1 X" + coordinates.getxCoordinate() + 
								" Y" + coordinates.getyCoordinate() + 
								" F1000\t ; move head to next part\n" +
								"G1 R" + part.getTheta() + " ; rotate head to angle of part\n";
					}
				}
			}
		}
		return "\nPART NOT FOUND\n";
	}

	
	private boolean requiresVision(EagleSMTComponent component){
		if(inputFileType == EAGLE_CENTROID_FILE){
			String componentPackageType = component.getPackageType();
			String componentValue = component.getValue();
			for(Part part : partArray){
				if(componentPackageType.contentEquals(part.getFootprint()) 
						&& componentValue.contentEquals(part.getValue())){
					//if we found matching footprint and value available for use, return G Code to This Part
					if(part.getVisionRequired() == 1){
						System.out.println("GCodeGenerator: Vision for " + componentPackageType + " is required");
						return true;
					}
					System.out.println("GCodeGenerator: Vision for " + componentPackageType + " not required");
					return false;
				}
			}
		}
		return false;
	}
	
	
	private boolean requiresVision(AltiumSMTComponent component){
		if(inputFileType == ALTIUM_CENTROID_FILE){
			String componentRefDesignator = component.getRefDesignator();
			for(Part part : partArray){
				if(componentRefDesignator.contentEquals(part.getFootprint())){
					//if we found matching footprint and value available for use, return G Code to This Part
					if(part.getVisionRequired() == 1){
						System.out.println("GCodeGenerator: Vision for " + componentRefDesignator + " is required");
						return true;
					}
					System.out.println("GCodeGenerator: Vision for " + componentRefDesignator + " not required");
					return false;
					}
			}
		}
		return false;
	}
	
	/**
	 * Generate G Code to move head to given location
	 * @param xCoord being moved to
	 * @param yCoord being moved to
	 * @return String G Code instructions that move to location (xCoord, yCoord)
	 */
	private String moveToLocation(String xCoord, String yCoord){
		return "G1 Z6 F300 ; lift head before moving\n" +
				"G1 X" + xCoord + 
				" Y" + yCoord + 
				" F1000 ; moving head to location (" + xCoord + ", " + yCoord + ")\n";
	}

	/**
	 * Generates appropriate G Code to pick up a component from tray/reel
	 * @return String G Code instructions that lower head, turn on vacuum, and lift part 
	 */
	private String pickUpComponent(){
		return "G1 Z0 F300 ; lower head to component" +
				"\nM10 ; vacuum On" +
				"\nG4 P5000 ; delay" +
				"\nG1 Z6 F300 ; lift head off table\n";
	}

	/**
	 * Generates appropriate G Code to lower component down onto PCB
	 * @return String G Code instructions that lower component, turn off vacuum, and raise head
	 */
	private String lowerComponent(){
		return "G1 Z0 F300; lower head to PCB board\n" +
				"M11; vacuum Off\n" +
				"G4 P5000 ; delay\n" +
				"G1 Z6 F300 ; Lift head off PCB board\n";
	}

	/**
	 * Produces an array of EagleSMTComponents from input centroid file
	 * @return true if array of SMTComponents generated without error
	 */
	private boolean generateEagleComponentArrayFromCentroid(){
		if(inputFileType == EAGLE_CENTROID_FILE){
			//array of EagleSMTComponent objects to store each component object
			eagleInputComponents = new EagleSMTComponent[centroidComponentList.size()];
			//for each inputComponent in List, generate corresponding EagleSMTComponent
			//add each EagleSMTComponent to the eagleComponents array
			for(int i = 0; i < centroidComponentList.size(); ++i){
				//create EagleSMTComponent and add to array of components
				eagleInputComponents[i] = new EagleSMTComponent(centroidParser.parseComponentAttributes(
						centroidComponentList.get(i), EagleSMTComponent.ATTRIBUTE_COUNT));
				//if we encounter a bad part, we halt and return error to user
				if(!eagleInputComponents[i].isValidComponent()){
					return false;
				}
			}
			return true;
		}
		else{
			System.out.println("\nError GCodeGenerator: generateEagleArrayFromCentroid(): verify that you are using" +
					" correct input file type\n");
			return false;
		}
	}

	/**
	 * Produces an array of AltiumSMTComponents from input centroid file
	 * @return true if array of SMTComponents generated without error
	 */
	private boolean generateAltiumComponentArrayFromCentroid(){
		if(inputFileType == ALTIUM_CENTROID_FILE){
			//array of AltiumSMTComponent objects to store each component object
			altiumInputComponents = new AltiumSMTComponent[centroidComponentList.size()];
			//for each inputComponent in List, generate corresponding AltiumSMTComponent
			//add each AltiumSMTComponent to the AltiumComponents array
			for(int i = 0; i < centroidComponentList.size(); ++i){
				//create Altium SMTComponent and add to array of components
				altiumInputComponents[i] = new AltiumSMTComponent(centroidParser.parseComponentAttributes(
						centroidComponentList.get(i), AltiumSMTComponent.ATTRIBUTE_COUNT));
				//if we encounter a bad part, we halt and return error to user
				if(!altiumInputComponents[i].isValidComponent()){
					return false;
				}
			}
			return true;
		}
		else{
			System.out.println("\nError GCodeGenerator: generateAltiumComponentArrayFromCentroid(): verify that you are using" +
					" correct input file type\n");
			return false;
		}
	}

	/**
	 * Generate an array of Parts found in part input file
	 * @return true if all parts defined are valid
	 */
	private boolean generatePartsArray(){
		//create an array of parts
		partArray = partsParser.makePartsArray();
		if(partArray != null){
			return true;
		}
		return false;
	}

	/**
	 * @return Return true if input centroid and parts files were parsed without error
	 */
	public boolean isValidInput(){
		return isValidInput;
	}

	/**
	 * Updates part files and writes part features to tempPartString
	 * @return String tempPartString
	 */
	private String updatePartFile(){
		String tempParts = "";
		for(Part part : partArray){
			System.out.println("GCodeGenerator -> Updating parts files with new part counts");
			part.updatePartCount();
			System.out.println("GCodeGenerator -> Writing updated parts file to tempPartString");
			tempParts += part.getPartFeatureString();
		}
		return tempParts;
	}
	
	/**
	 * Returns a copy of tempPartString which contains String representation of all part features
	 * tempPartString is updated after G Code is generated, therefore representing parts file after PNP jobs
	 * @return
	 */
	public String getUpdatedPartString(){
		return tempPartString;
	}
	
	//components to be placed
	private EagleSMTComponent[] eagleInputComponents;
	private AltiumSMTComponent[] altiumInputComponents;
	//locations of parts
	private ArrayList<Part> partArray;
	//centroid file contains placement of each component
	private String centroidFilePath;
	//parts file contains location of all parts
	private String partsFilePath;
	//file type specifies Altium vs Eagle
	private int inputFileType;
	public static final int EAGLE_CENTROID_FILE = 1;
	public static final int ALTIUM_CENTROID_FILE = 2;
	//centroid parsers to parse input files
	private CentroidParser centroidParser;
	private PartsFileParser partsParser;
	//lists to store Strings of component attributes
	private List<String> centroidComponentList;
	//flag to detect proper parsing
	private boolean isValidInput;
	//string to hold updated part values
	private String tempPartString;
}
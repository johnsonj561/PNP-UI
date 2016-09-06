package g_code_generator_ui.controller;

import g_code_generator_ui.controller.CentroidParser;
import g_code_generator_ui.model.AltiumSMTComponent;
import g_code_generator_ui.model.EagleSMTComponent;

import java.util.List;


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
		//Initialize CentroidParsers and generate SMTComponent arrays
		parseInputFiles();
	}	

	/**
	 * Initialize CentroidParsers and generate SMTComponent arrays that store all components
	 * being placed on PCB and locations of all parts (reels/trays)
	 */
	private void parseInputFiles(){
		//create a Centroid parser that will parse the PCB Centroid File and the Parts File
		centroidParser = new CentroidParser(centroidFilePath);
		centroidComponentList = centroidParser.getComponentList();
		partsParser = new CentroidParser(partsFilePath);
		partsComponentList = partsParser.getComponentList();

		//generate appropriate array of components to be placed and parts from reels/trays
		if(inputFileType == EAGLE_CENTROID_FILE){
			generateEagleComponentArrayFromCentroid();
			generateEagleComponentArrayFromParts();
		}
		else if(inputFileType == ALTIUM_CENTROID_FILE){
			generateAltiumComponentArrayFromCentroid();
			generateAltiumComponentArrayFromParts();
		}
		else{
			System.out.println("Error GCodeGenerator: invalid centroid file type");
		}
	}

	/**
	 * Generates initialization G Code configuration settings to be performed at start of new job and applies them to gCode member
	 * Common start up settings include: units, absolute coordinates, home x coord, home y coord, home z coord, baudrate
	 */
	public String initializeGCode(){
		String gCode = "\n;initializeGCode() configures start up settings. See GCodeGenerator.py to change\n";
		gCode += "G21; Set units to millimeters\nG90; Set absolute coordinates\nG28 X0 Y0; Home x and y axis\n";
		gCode += "G21; Set units to millimeters\nG90; Set absolute coordinates\nG28 X0 Y0; Home x and y axis\n";
		gCode += "G28 Z0; Home Z axis\nG1 F3000; Set feed rate (speed) for first move\n\n";
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
				gCode += moveToPart(eagleInputComponents[i]);
				gCode += pickUpComponent();
				gCode += moveToLocation(eagleInputComponents[i].getxCoordinate(), eagleInputComponents[i].getyCoordinate());
				//if requires alignment, move to light box
				//gCode += moveToLocation(Double.parseDouble(eagleInputComponents[i].getxCoordinate()), 
				//		Double.parseDouble(eagleInputComponents[i].getyCoordinate()));
				gCode += lowerComponent() + "\n";
			}
			return gCode;
		}
		else if(inputFileType == ALTIUM_CENTROID_FILE){
			for(int i = 0; i < altiumInputComponents.length; i++){
				gCode += moveToPart(altiumInputComponents[i]);
				gCode += pickUpComponent();
				gCode += moveToLocation(altiumInputComponents[i].getxCoordinate(), altiumInputComponents[i].getyCoordinate());
				//if requires alignment, move to light box
				//gCode += moveToLocation(Double.parseDouble(eagleInputComponents[i].getxCoordinate()), 
				//		Double.parseDouble(eagleInputComponents[i].getyCoordinate()));
				gCode += lowerComponent() + "\n";
			}
			return gCode;
		}
		return "Error: Invalid Centroid File Type Provided. Please use Altium or Eagle file types.";

	}

	/**
	 * Generates G Code commands that move PnP head to location of part identified by component Packate Type
	 * @param component EagleSMTComponent to be picked up from part stack
	 * @return String G Code instructions that move to component's part stack for pick up
	 */
	private String moveToPart(EagleSMTComponent component){
		if(inputFileType == EAGLE_CENTROID_FILE){
			String componentPackageType = component.getPackageType();
			for(EagleSMTComponent part : eagleParts){
				if(componentPackageType.contentEquals(part.getPackageType())){
					return "G1 X" + part.getxCoordinate() + " Y" + part.getyCoordinate() + "; moving to part for pick up\n";
				}
			}
		}
		else{
			System.out.println("Error GCodeGenerate:moveToPart(EagleSMTComponent component): inputFileType" +
					" and component type do not match");
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
			for(AltiumSMTComponent part : altiumParts){
				if(componentRefDesignator.contentEquals(part.getRefDesignator())){
					return "G1 X" + part.getxCoordinate() + " Y" + part.getyCoordinate() + "; moving to part for pick up\n";
				}
			}
		}
		else{
			System.out.println("Error GCodeGenerator:moveToPart(AltiumSMTComponent component): inputFileType" +
					" and component type do not match");
		}
		return "\nmoveToPart(AltiumSMTComponent) \nPART NOT FOUND\n";
	}

	/**
	 * Generate G Code to move head to given location
	 * @param xCoord being moved to
	 * @param yCoord being moved to
	 * @return String G Code instructions that move to location (xCoord, yCoord)
	 */
	private String moveToLocation(String xCoord, String yCoord){
		return "G1 X" + xCoord + " Y" + yCoord + "; moving to placement location\n";
	}

	/**
	 * Generates appropriate G Code to pick up a component from tray/reel
	 * @return String G Code instructions that lower head, turn on vacuum, and lift part 
	 */
	private String pickUpComponent(){
		return "G1 Z15; Lower Z axis to component\nM10; Vacuum On\nG1 Z0; Raise Z axis to home\n";
	}

	/**
	 * Generates appropriate G Code to lower component down onto PCB
	 * @return String G Code instructions that lower component, turn off vacuum, and raise head
	 */
	private String lowerComponent(){
		return "G1 Z15; Lower Z axis to PCB board\nM11; Vacuum Off\nG1 Z0; Raise Z axis to home\n";
	}

	/**
	 * Produces an array of EagleSMTComponents from input centroid file
	 * @return eagleComponents array of EagleSMTComponents generated from centroid file
	 */
	private void generateEagleComponentArrayFromCentroid(){
		if(inputFileType == EAGLE_CENTROID_FILE){
			//array of EagleSMTComponent objects to store each component object
			eagleInputComponents = new EagleSMTComponent[centroidComponentList.size()];
			//for each inputComponent in List, generate corresponding EagleSMTComponent
			//add each EagleSMTComponent to the eagleComponents array
			for(int i = 0; i < centroidComponentList.size(); ++i){
				eagleInputComponents[i] = new EagleSMTComponent(centroidParser.parseComponentAttributes(
						centroidComponentList.get(i), EagleSMTComponent.ATTRIBUTE_COUNT));
			}
		}
		else{
			System.out.println("\nError GCodeGenerator: generateEagleArrayFromCentroid(): verify that you are using" +
					" correct input file type\n");
		}
	}

	/**
	 * Produces an array of AltiumSMTComponents from input centroid file
	 * @return altiumComponents array of AltiumSMTComponents generated from centroid file
	 */
	private void generateAltiumComponentArrayFromCentroid(){
		if(inputFileType == ALTIUM_CENTROID_FILE){
			//array of AltiumSMTComponent objects to store each component object
			altiumInputComponents = new AltiumSMTComponent[centroidComponentList.size()];
			//for each inputComponent in List, generate corresponding AltiumSMTComponent
			//add each AltiumSMTComponent to the AltiumComponents array
			for(int i = 0; i < centroidComponentList.size(); ++i){
				altiumInputComponents[i] = new AltiumSMTComponent(centroidParser.parseComponentAttributes(
						centroidComponentList.get(i), AltiumSMTComponent.ATTRIBUTE_COUNT));
			}
		}
		else{
			System.out.println("\nError GCodeGenerator: generateAltiumComponentArrayFromCentroid(): verify that you are using" +
					" correct input file type\n");
		}
	}

	/**
	 * Produces an array of EagleSMTComponents from input parts file (reels/tray locations)
	 */
	private void generateEagleComponentArrayFromParts(){
		if(inputFileType == EAGLE_CENTROID_FILE){
			//array of EagleSMTComponent objects to store each component object
			eagleParts = new EagleSMTComponent[partsComponentList.size()];
			//for each partsComponent in List, generate corresponding EagleSMTComponent
			//add each EagleSMTComponent to the eagleComponents array
			for(int i = 0; i < partsComponentList.size(); ++i){
				eagleParts[i] = new EagleSMTComponent(partsParser.parseComponentAttributes(
						partsComponentList.get(i), EagleSMTComponent.ATTRIBUTE_COUNT));
			}
		}
		else{
			System.out.println("\nError GCodeGenerator: generateEagleArrayFromParts(): verify that you are using" +
					" correct input file type\n");
		}
	}

	/**
	 * Produces an array of AltiumSMTComponents from input parts file (reels/tray locations)
	 */
	private void generateAltiumComponentArrayFromParts(){
		if(inputFileType == ALTIUM_CENTROID_FILE){
			//array of AltiumSMTComponent objects to store each component object
			altiumParts = new AltiumSMTComponent[partsComponentList.size()];
			//for each partsComponent in List, generate corresponding AltiumSMTComponent
			//add each AltiumSMTComponent to the altiumParts array
			for(int i = 0; i < partsComponentList.size(); ++i){
				altiumParts[i] = new AltiumSMTComponent(partsParser.parseComponentAttributes(
						partsComponentList.get(i), AltiumSMTComponent.ATTRIBUTE_COUNT));
			}
		}
		else{
			System.out.println("\nError GCodeGenerator: generateAltiumArrayFromParts(): verify that you are using" +
					" correct input file type\n");
		}
	}


	//components to be placed
	private EagleSMTComponent[] eagleInputComponents;
	private AltiumSMTComponent[] altiumInputComponents;
	//locations of parts
	private EagleSMTComponent[] eagleParts;
	private AltiumSMTComponent[] altiumParts;
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
	private CentroidParser partsParser;
	//lists to store Strings of component attributes
	private List<String> centroidComponentList;
	private List<String> partsComponentList;

}
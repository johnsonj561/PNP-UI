package define_parts_ui.model;

/**
 * Class defines constants for Part Definitions
 * Tape Cavity Pitch Values, Footprint Options
 * @author Justin Johnson
 */
public class PartConstants {

	/**
	 * Return list of available footprints
	 * @return String[] availableFootprints
	 */
	public static String[] getAvailableFootprints(){
		return availableFootprints;
	}
	
	/**
	 * Return list of available tape cavity pitch values
	 * Values begin at 4mm and increase in increments of 4.0mm, per industry standards
	 * @return String[] tapeCavityPitchValues
	 */
	public static String[] getTapeCavityPitchValues(){
		return tapeCavityPitchValues;
	}
	
	/**
	 * Return a list of integers between 1 and max
	 * @param max number of parts on tape
	 * @return String[] of integers between 1 and max number of parts on tape
	 */
	public static String[] getPartsCounter(int max){
		String[] counter = new String[max];
		for(int i = 0; i < max; i++){
			counter[i] = (i+1) + "";
		}
		return counter;
	}
	
	
	private static String[] availableFootprints = {"C0603", "C0805", "CHIP-LED805", "HC49UP", "M0603", "M0805", "R0603", 
					"R0805", "SO14", "TO252"};
	private static String[] tapeCavityPitchValues = {"4", "8", "12", "16", "20", "24", "28", "32"};
}

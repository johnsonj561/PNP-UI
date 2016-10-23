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
	 * Values begin at 4.00mm and increase in increments of 4.0mm, per industry standards
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
		for(int i = 1; i < max; i++){
			counter[i] = i + "";
		}
		return counter;
	}
	
	
	private static String[] availableFootprints = {"C-0805", "R-0805", "C-0603", "R-0603", "SOT-223"};
	private static String[] tapeCavityPitchValues = {"4.00", "8.00", "12.00", "16.00", "20.00", "24.00", "28.00", "32.00"};
}

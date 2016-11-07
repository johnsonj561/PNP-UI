package g_code_generator_ui.model;

/**
 * SMTComponent Class provides functionality for storing and manipulating component attributes
 * @author Justin Johnson
 *
 */
public class SMTComponent {
	
	/**
	 * SMTComponent constructor assigns component attributes and attribute titles
	 * @param attributes SMT Component attributes as provided by Centroid file
	 */
	public SMTComponent(String[] attributes) {
		//the component's placement attributes
		attributeList = new String[attributes.length];
		attributeList = attributes;
		//the title (heading) of each attribute or field
		//attribute titles will be enumerated by default
		attributeTitles = new String[attributes.length];
		for(int i = 0; i < attributes.length; ++i){
			attributeTitles[i] = i + "";
		}
	}

	/**
	 * Return an array of component placement attributes
	 * @return attributeList
	 */
	public String[] getAttributeList(){
		return attributeList;
	}
	
	/**
	 * Return an array of component attribute titles or headings
	 * @return attributeTitels
	 */
	public String[] getAttributeTitles(){
		return attributeTitles;
	}
	
	/**
	 * Print component attribute 'title: value' pairs
	 */
	public void printComponent(){
		for(int i = 0; i < attributeList.length; ++i){
			System.out.println(attributeTitles[i] + ": " + attributeList[i]);
		}
		
	}
	
	protected String[] attributeList;
	protected String[] attributeTitles;
	protected final double PCB_X_OFFSET = 0.0;
	protected final double PCB_Y_OFFSET = 0.0;
}

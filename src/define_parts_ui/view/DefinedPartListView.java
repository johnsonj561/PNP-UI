package define_parts_ui.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * DefinedPartListView displays list of all defined PNP SMT Components for a given project
 * and provides functionality for editing/updating defined parts
 * @author Justin Johnson
 *
 */
public class DefinedPartListView extends JPanel{

	/**
	 * Constructs UI that displays list of defined SMT components 
	 */
	public DefinedPartListView() {
		partCounter = 0;
		initUI();
	}

	/**
	 * Initialize UI elements
	 */
	private void initUI(){
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	}
	
	/**
	 * Adds a new row to the list of defined parts
	 * @param String footprint of new part
	 * @param String value of new part
	 */
	public void addPart(String footprint, String value){
		partCounter++;
		DefinedPartFeatures newPart = new DefinedPartFeatures(partCounter, footprint, value);
		this.add(newPart);
	}
	
	public void removePart(int index){
		
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private int partCounter;
}

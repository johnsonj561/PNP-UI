package define_parts_ui.view;

import java.util.ArrayList;

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
		initUI();
	}

	/**
	 * Initialize UI elements
	 */
	private void initUI(){
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		//we must keep a list of all DefinedPartDisplays so that we can access each Button
		definedPartDisplayList = new ArrayList<DefinedPartDisplay>();
	}
	
	/**
	 * Adds a new row to the list of defined parts
	 * @param String footprint of new part
	 * @param String value of new part
	 */
	public void addPart(int partID, String footprint, String value){
		//create new defined part display and add new part display to list
		DefinedPartDisplay newPart = new DefinedPartDisplay(partID, footprint, value);
		definedPartDisplayList.add(newPart);
		//refresh UI
		this.add(newPart);
		refreshUI();
	}
	
	/**
	 * Remove part from list with given partID
	 * @param int partID to be removed
	 */
	public void removePart(int partID){
		int partToRemove = -1;
		for(int i = 0; i < definedPartDisplayList.size(); i++){
			if(definedPartDisplayList.get(i).getPartNumber() == partID){
				partToRemove = i;
			}
		}
		if(partToRemove >= 0){
			this.remove(definedPartDisplayList.get(partToRemove));
			definedPartDisplayList.remove(partToRemove);
			refreshUI();
		}
		else{
			System.out.println("Part Not Found. No Parts Removed");
		}
	}
	
	/**
	 * Update a part display that has been edited
	 * @param partID to be edited
	 * @param footprint new footprint to display
	 * @param value new value to display
	 */
	public void updatePart(int partID, String footprint, String value){
		int partToUpdate = -1;
		for(int i = 0; i < definedPartDisplayList.size(); i++){
			if(definedPartDisplayList.get(i).getPartNumber() == partID){
				partToUpdate = i;
			}
		}
		if(partToUpdate >= 0){
			definedPartDisplayList.get(partToUpdate).updatePartDisplay(footprint, value);
			refreshUI();
		}
		else{
			System.out.println("Part Not Found. No Parts Removed");
		}
	}

	public void printDefinedPartList(){
		System.out.println("Current Defined Parts:");
		for(DefinedPartDisplay part : definedPartDisplayList){
			part.print();
		}
	}
	
	/**
	 * Update UI with new content
	 */
	private void refreshUI(){
		this.validate();
		this.repaint();
	}
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	public ArrayList<DefinedPartDisplay> definedPartDisplayList;
}

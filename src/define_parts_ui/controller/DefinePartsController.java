package define_parts_ui.controller;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import jssc.SerialPortException;
import connection_settings_ui.view.SettingsTitleView;
import define_parts_ui.model.Part;
import define_parts_ui.view.AddNewPartView;
import define_parts_ui.view.DefinedPartListView;
import define_parts_ui.view.SavePartFileView;

/**
 * Parts Definition Controller allows user to define parts for a PNP project
 * Defining parts consists of identifying footprints, values, and positions on workspace
 * @author Justin Johnson
 *
 */
public class DefinePartsController extends JPanel{

	/**
	 * Create UI for defining PNP Project parts and their locations
	 * @throws SerialPortException 
	 */
	public DefinePartsController(){
		initUI();
		initButtons();
		//part number acts as an ID to keep track of total number of parts
		partID = editPartID = 0;
		//array list to store list of all parts as String 
		definedPartList = new ArrayList<Part>();
	}

	/**
	 * Initialize Parts Definition UI
	 */
	private void initUI(){
		//Defined Parts Title
		definedPartsTitle = new SettingsTitleView("Defined Parts");
		definedPartsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//a view that displays list of defined parts and will grow dynamically as user adds/removes parts from view
		definedPartListView = new DefinedPartListView();
		definedPartListView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//add panel to scroll pane to handle overflow of parts
		definePartScrollPane = new JScrollPane(definedPartListView);
		definePartScrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
		definePartScrollPane.setMinimumSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
		definePartScrollPane.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
		definePartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		definePartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//Add New Parts Title
		addNewPartsTitle = new SettingsTitleView("Add New Part");
		addNewPartsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//a view that allows user to add new parts or edit current parts
		addNewPartsView = new AddNewPartView();
		addNewPartsView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//view that displays import/export part file buttons
		savePartFileView = new SavePartFileView();
		savePartFileView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(definedPartsTitle);
		this.add(definePartScrollPane);
		this.add(savePartFileView);
		this.add(addNewPartsTitle);
		this.add(addNewPartsView);

	}

	private void initButtons(){
		addNewPartsView.addPartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validateInput()){
					addPart();
				}
				else{
					System.out.println("Verify Input Is Accurate");
				}
			}
		});
		addNewPartsView.savePartButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						saveSelectedPart();
					}
				});
		savePartFileView.exportPartFileButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						writePartListToFile();
					}
				});
		savePartFileView.importPartFileButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importPartsFile();
					}
				});
	}

	/**
	 * Define the Delete and Edit buttons for a newly defined part
	 * @param partNumber
	 */
	private void initNewPartButtons(final int partNumber){
		//the buttons we are initializing belong to the last element in the list
		int lastIndex = definedPartListView.definedPartDisplayList.size() - 1;
		definedPartListView.definedPartDisplayList.get(lastIndex).deleteButton.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removePart(partNumber);
					}
				});
		definedPartListView.definedPartDisplayList.get(lastIndex).editButton.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						editSelectedPart(partNumber);
					}
				});
	}

	/**
	 * Allow user to edit part by loading part into input fields for editing/re-saving
	 * @param partNumber to be edited
	 */
	private void editSelectedPart(int partNumber){
		Part p = null;
		editPartID = partNumber;
		//get out part from list that has matching ID
		for(int i = 0; i < definedPartList.size(); i++){
			if(definedPartList.get(i).getPartID() == partNumber){
				p = definedPartList.get(i);
			}
		}
		//if p was found, add part to edit console and update button options
		if(p != null){
			//move part information to the addnewpartview
			addNewPartsView.setFootprint(p.getFootprint());
			addNewPartsView.setComponentValue(p.getValue());
			addNewPartsView.setXinitial(p.getxInitial());
			addNewPartsView.setYinitial(p.getyInitial());
			addNewPartsView.setXfinal(p.getxFinal());
			addNewPartsView.setYfinal(p.getyFinal());
			addNewPartsView.setPartCount(p.getCount() + "");
			//change addnewpartview title and button text to Edit/Save
			addNewPartsView.savePartButton.setEnabled(true);
			addNewPartsView.addPartButton.setEnabled(false);
			//update first part/last part to positive state, because position was previously defined
			addNewPartsView.setFirstPartDefinitionStatusPositive();
			addNewPartsView.setLastPartDefinitionStatusPositive();
			updateNewPartTitle("Editing PartID: " + partNumber);
			updateErrorMessage("Save Part When Finished Editing");
			refreshUI();
		}
		//else do nothing, if part wasn't found, nothing will happen
	}

	/**
	 * Save part that has been recently edited by user
	 * @param partNumber
	 */
	private void saveSelectedPart(){
		//get the partID that is being worked on
		int partIndex = -1;
		for(int i = 0; i < definedPartList.size(); i++){
			if(definedPartList.get(i).getPartID() == editPartID){
				partIndex = i;
			}
		}
		//if p was found, update it's components
		if(partIndex >= 0){
			definedPartList.get(partIndex).setFootprint(addNewPartsView.getFootprint());
			definedPartList.get(partIndex).setValue(addNewPartsView.getComponentValue());
			definedPartList.get(partIndex).setxInitial(addNewPartsView.getXinitial());
			definedPartList.get(partIndex).setyInitial(addNewPartsView.getYinitial());
			definedPartList.get(partIndex).setxFinal(addNewPartsView.getXfinal());
			definedPartList.get(partIndex).setyFinal(addNewPartsView.getYfinal());
			definedPartList.get(partIndex).setCount(Integer.parseInt(addNewPartsView.getPartCount()));
			//replace the old p with the new p in our definedPartList
			definedPartListView.updatePart(editPartID, addNewPartsView.getFootprint(), addNewPartsView.getComponentValue());
			//update part button definitions
			//initNewPartButtons(editPartID);
		}
		//make save part button invisible, make add part button visible
		addNewPartsView.addPartButton.setEnabled(true);
		addNewPartsView.savePartButton.setEnabled(false);
		//reset first/last part position status to negative
		addNewPartsView.setFirstPartDefinitionStatusNegative();
		addNewPartsView.setLastPartDefinitionStatusNegative();
		updateErrorMessage("Part Saved");
		updateNewPartTitle("Add New Part");
		//print to console for debug
		definedPartListView.printDefinedPartList();
		printArrayOfDefinedParts();
		resetPartLocationButtons();
		//update UI
		refreshUI();
	}

	/**
	 * 
	 * @param partNumber
	 */
	private void removePart(int partNumber){
		//remove part from array of defined parts
		int partToRemove = -1;
		for(int i = 0; i < definedPartList.size(); i++){
			if(definedPartList.get(i).getPartID() == partNumber){
				partToRemove = i;
			}
		}
		if(partToRemove >= 0){
			definedPartList.remove(partToRemove);
		}
		//remove part from display definedPartListView
		definedPartListView.removePart(partNumber);
		refreshUI();
		System.out.println("Part Removed: " + partNumber);
		//print to console for debux
		definedPartListView.printDefinedPartList();
		printArrayOfDefinedParts();
	}

	/**
	 * Extracts data from New Part Editor view and adds it to the parts list
	 */
	public boolean addPart(){
		if(!validateInput()){
			return false;
		}
		partID++;
		//create new part to store features, add new part to our array of defined parts
		Part newPart = new Part(partID, addNewPartsView.getFootprint(), addNewPartsView.getComponentValue(),
				addNewPartsView.getXinitial(), addNewPartsView.getYinitial(), addNewPartsView.getXfinal(),
				addNewPartsView.getYfinal(), addNewPartsView.getPartCount());
		definedPartList.add(newPart);
		//add new part values to display definedPartListView
		definedPartListView.addPart(partID, addNewPartsView.getFootprint(), addNewPartsView.getComponentValue());
		refreshUI();
		//initialize new part display's buttons to allow user to edit/delete from list
		initNewPartButtons(partID);
		//print to console for debug
		definedPartListView.printDefinedPartList();
		printArrayOfDefinedParts();
		resetPartLocationButtons();
		return true;
	}

	/**
	 * Reset part position values to -1 and change graphics to negative feedback
	 */
	private void resetPartLocationButtons(){
		addNewPartsView.setFirstPartDefinitionStatusNegative();
		addNewPartsView.setLastPartDefinitionStatusNegative();
		addNewPartsView.setXinitial(-1);
		addNewPartsView.setYinitial(-1);
		addNewPartsView.setXfinal(-1);
		addNewPartsView.setYfinal(-1);
	}

	/**
	 * Verify that user's New Part data is valid 
	 * @return false if input features are invalid
	 */
	private boolean validateInput(){
		if(addNewPartsView.getFootprint() == null){
			return false;
		}
		if(addNewPartsView.getComponentValue().length() == 0){
			updateErrorMessage("Please enter valid value.");
			return false;
		}
		if(addNewPartsView.getXinitial() == -1 || addNewPartsView.getYinitial() == -1){
			updateErrorMessage("First Part's (X,Y) Coordinates Not Defined");
			return false;
		}
		if(addNewPartsView.getXfinal() == -1 || addNewPartsView.getYfinal() == -1){
			updateErrorMessage("Last Part's (X,Y) Coordinates Not Defined");
			return false;
		}
		updateErrorMessage("");
		return true;
	}

	/**
	 * Update error message feedback
	 * @param String error
	 */
	public void updateErrorMessage(String error){
		addNewPartsView.setNewPartErrorMessage(error);
	}

	/**
	 * Prints ArrayList<Part> to screen for debug purposes
	 */
	public void printArrayOfDefinedParts(){
		System.out.println("Printing ArrayList<Parts>:");
		for(Part p : definedPartList){
			p.print();
		}
	}

	/**
	 * Write a comma separated parts list to file and save
	 * Parts file is required as input to generate G Code
	 */
	public void writePartListToFile(){
		FileNameExtensionFilter mExtensionFilter = new FileNameExtensionFilter("Text File", "txt");
		JFileChooser mSaveAsFileChooser = new JFileChooser();
		mSaveAsFileChooser.setApproveButtonText("Save");
		mSaveAsFileChooser.setFileFilter(mExtensionFilter);
		int actionDialog = mSaveAsFileChooser.showOpenDialog(this);
		if (actionDialog != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = mSaveAsFileChooser.getSelectedFile();
		if (!file.getName().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}

		BufferedWriter outFile = null;
		try {
			outFile = new BufferedWriter(new FileWriter(file));
			for(Part part : definedPartList){
				outFile.append(part.getPartFeatureString());
				outFile.newLine();
				//outFile.append();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outFile != null) {
				try {
					outFile.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 * Remove all currently defined parts from display and storage
	 */
	public void clearAllParts(){
		//while parts exist
		while(!definedPartList.isEmpty()){
			//remove part from view
			definedPartListView.removePart(definedPartList.get(0).getPartID());
			//then remove part from list
			definedPartList.remove(0);
		}
		//reset partID to zero because we're going to have to make a new list
		partID = 0;
	}

	/**
	 * Import a parts file from local machine to PNP UI for editing purposes
	 */
	public void importPartsFile(){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select Input");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {		
			PartsFileParser partParser = new PartsFileParser(chooser.getSelectedFile().toString());
			clearAllParts();
			definedPartList = partParser.makePartsArray();
			for(Part p : definedPartList){
				definedPartListView.addPart(p.getPartID(), p.getFootprint(), p.getValue());
				initNewPartButtons(p.getPartID());
				//if partID of part being imported is greater than system's current ID placeholder
				//we re-set out ID placeholder to make sure IDs aren't duplicated
				if(p.getPartID() > partID){
					partID = p.getPartID();
				}
			}
			refreshUI();
		} 
		else{
			updateErrorMessage("Error importing parts file");
		}
	}

	/**
	 * Refresh UI elements
	 */
	private void refreshUI(){
		this.validate();
		this.repaint();
	}

	/**
	 * Update the title of Section 2 that allows user to add new parts
	 * This will toggle between "Add New Part" and "Edit Part" based on user input
	 * @param String title to be applied
	 */
	public void updateNewPartTitle(String title){
		addNewPartsTitle.setTitle(title);
	}

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class variables
	 */
	//UI Elements
	private SettingsTitleView definedPartsTitle;
	private SettingsTitleView addNewPartsTitle;
	public AddNewPartView addNewPartsView;
	private JScrollPane definePartScrollPane;
	public DefinedPartListView definedPartListView;
	public SavePartFileView savePartFileView;
	private final int SCROLL_PANE_WIDTH = 550;
	private final int SCROLL_PANE_HEIGHT = 230;
	//Array to store current defined parts
	private ArrayList<Part> definedPartList;
	//counter to keep track of part numbers and manage button clicks
	private int partID;
	private int editPartID;

}

package define_parts_ui.controller;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jssc.SerialPortException;
import connection_settings_ui.view.SettingsTitleView;
import define_parts_ui.view.AddNewPartView;
import define_parts_ui.view.DefinedPartListView;

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
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
		definedPartList.addPart("C_0805", "20u");
	}

	/**
	 * Initialize Parts Definition UI
	 */
	private void initUI(){
		//Defined Parts Title
		definedPartsTitle = new SettingsTitleView("Defined Parts");
		definedPartsTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//a view that displays list of defined parts and will grow dynamically as user adds/removes parts from view
		definedPartList = new DefinedPartListView();
		definedPartList.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//add panel to scroll pane to handle overflow of parts
		definePartScrollPane = new JScrollPane(definedPartList);
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
		//Add individual components to this.JPanel for final display, laid on vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(definedPartsTitle);
		this.add(definePartScrollPane);
		this.add(addNewPartsTitle);
		this.add(addNewPartsView);
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
	private SettingsTitleView definedPartsTitle;
	private SettingsTitleView addNewPartsTitle;
	public AddNewPartView addNewPartsView;
	private JScrollPane definePartScrollPane;
	public DefinedPartListView definedPartList;
	private final int SCROLL_PANE_WIDTH = 550;
	private final int SCROLL_PANE_HEIGHT = 250;


}

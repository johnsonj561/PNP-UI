package manual_control_ui.controller;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import connection_settings_ui.view.SettingsTitleView;
import manual_control_ui.view.HomeButtonView;
import manual_control_ui.view.JogButtonView;
import manual_control_ui.view.ManualInstructionView;

/**
 * ImportGCodeController assigns functionality to buttons in Upload G Code Views
 * @author Justin Johnson
 *
 */
public class ManualController extends JPanel{

	/**
	 * Constructor creates instance of individual components(InputFilePanel, GCodeConsolPanel, and
	 * ProcessJobPanel), combines them into one panel, and defines button functionality
	 */
	public ManualController() {
		initUI();
	}

	/**
	 * Add all component views to display
	 * Views: Input Selection, G Code Console Display, and Job Processing Buttons
	 */
	private void initUI(){
		//Jog controls
		jogTitle = new SettingsTitleView("Manual Jog Controls");
		jogTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		jogButtonView = new JogButtonView();
		jogButtonView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Home controls
		homeTitle = new SettingsTitleView("Home Machine");
		homeTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		homeButtonView = new HomeButtonView();
		homeButtonView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//Panel that lets user send 1 instruction to PNP
		manualInstructionTitle = new SettingsTitleView("Manually Send Commands to PNP Machine");
		manualInstructionTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		manualInstructionView = new ManualInstructionView("Enter G-Code Command: ");
		manualInstructionView.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		
		//Add individual components to this.JPanel for final display, laid vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(jogTitle);
		this.add(jogButtonView);
		this.add(homeTitle);
		this.add(homeButtonView);
		this.add(manualInstructionTitle);
		this.add(manualInstructionView);
	}


	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class variables
	 */
	private JogButtonView jogButtonView;
	private HomeButtonView homeButtonView;
	private ManualInstructionView manualInstructionView;
	private SettingsTitleView manualInstructionTitle;
	private SettingsTitleView jogTitle;
	private SettingsTitleView homeTitle;

}

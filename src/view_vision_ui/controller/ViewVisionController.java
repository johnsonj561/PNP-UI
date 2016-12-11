package view_vision_ui.controller;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pnp_main.model.PNPConstants;

import view_vision_ui.view.CVImageView;

import connection_settings_ui.view.SettingsTitleView;
public class ViewVisionController extends JPanel{

	public ViewVisionController() {
		initUI();
	}

	private void initUI(){
		//view title
		visionTitle = new SettingsTitleView(CV_DISPLAY_TITLE);
		visionTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//image display description
		imageViewer = new CVImageView();
		//Add individual components to this.JPanel for final display, laid vertically along y Axis
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		this.add(visionTitle);
		this.add(imageViewer);
	}

	/**
	 * Assigns image at path to imageViewer display object
	 * @param path
	 */
	public void setImage(String path){
		imageViewer.setImage(path);
	}

	/**
	 * Update the Visions Display View descriptors
	 * @param double x center error
	 * @param double y center error
	 * @param double orientation angle error
	 */
	public void setImageDetails(double x, double y, double orientation){
		boolean adjusting = false;
		if(Math.abs(x) > PNPConstants.IMAGE_POSITION_THRESHOLD ||
				Math.abs(y) > PNPConstants.IMAGE_POSITION_THRESHOLD ||
				Math.abs(orientation) > PNPConstants.IMAGE_ORIENTATION_THRESHOLD ){
			adjusting = true;
		}
		imageViewer.setPositionDetails(x + "", y + "");
		imageViewer.setAngleDetails(orientation + "");
		if(adjusting){
			imageViewer.setRoutineDescription("Adjusting For Error");
		}
		else{
			imageViewer.setRoutineDescription("Vision Routine Complete");
		}
		
	}
	
	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class variables
	 */
	private SettingsTitleView visionTitle;
	private CVImageView imageViewer;
	private final String CV_DISPLAY_TITLE = "Computer Vision Display";
}

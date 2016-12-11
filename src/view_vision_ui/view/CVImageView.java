package view_vision_ui.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CVImageView extends JPanel {

	/**
	 * Constructor initializes Image View for CV Display
	 */
	public CVImageView() {
		initUI();
	}


	/**
	 * Initialize Views
	 */
	private void initUI(){
		//create panel for storing description
		imageViewDescriptionPanel = new JPanel();
		imageViewDescriptionPanel.setLayout(new BoxLayout(imageViewDescriptionPanel, BoxLayout.Y_AXIS));
		imageViewDescriptionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		imageViewDescriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		imageLabel = new JLabel(DESCRPTION);
		imageLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
		imageViewDescriptionPanel.add(imageLabel);
		//create panel for Image Box and Coordinate Features
		imageDisplayPanel = new JPanel();
		imageDisplayPanel.setLayout(new BoxLayout(imageDisplayPanel, BoxLayout.X_AXIS));
		imageDisplayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		imageDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		//create image box
		try {
			bufferredImage = ImageIO.read(getClass().getResource("/images/vision-default-480x480.jpg"));
		} catch (IOException e) {
			System.out.println("Error loading CVImageView Class -> unable to load default image");
		}
		imageDisplayLabel = new JLabel(new ImageIcon(bufferredImage));
		imageDisplayLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imageDisplayLabel.setMaximumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imageDisplayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		//create part details labels
		orientationPanel = new OrientationDetailsView();
		orientationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		//add image and details to panel
		imageDisplayPanel.add(imageDisplayLabel);
		imageDisplayPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		imageDisplayPanel.add(orientationPanel);
		//add components to this.JPanel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(imageViewDescriptionPanel);
		this.add(imageDisplayPanel);
	}

	/**
	 * Assigns new image to imageDisplay
	 * @param String path of new image
	 */
	public void setImage(String path){
		imageDisplayLabel.setIcon(new ImageIcon(path));
	}
	
	/**
	 * Set value of component's position, as detected by computer vision
	 * @param String x position
	 * @param String y position
	 */
	public void setPositionDetails(String x, String y){
		orientationPanel.setPositionLabel("( " + x + " , " + y + " ) mm");
	}

	/**
	 * Set the value of component's angle of rotation, as detected by computer vision
	 * @param String r angle of rotation
	 */
	public void setAngleDetails(String r){
		orientationPanel.setAngleLabel("( " + r + " ) degrees");
	}
	
	/**
	 * Set the routine description text
	 * @param String description of current routine
	 */
	public void setRoutineDescription(String description){
		orientationPanel.setRoutineDescription(description);
	}
	
	/**
	 * Deletes old image from disk to free space
	 * @param String path to image being deleted
	 */
	public void deleteImage(String path){
		//TODO
	}

	
	private BufferedImage bufferredImage;
	private JLabel imageDisplayLabel;
	private JPanel imageViewDescriptionPanel;
	private JPanel imageDisplayPanel;
	private OrientationDetailsView orientationPanel;
	private JLabel imageLabel;
	private final String DESCRPTION = "Displaying vision system's most recent data";
	private final int IMAGE_WIDTH = 480;
	private final int IMAGE_HEIGHT = 480;
	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;
}

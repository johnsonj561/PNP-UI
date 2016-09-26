package manual_control_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *Displays slider that allows user to adjust step size for jogging purposes
 * @author Justin Johnson
 *
 */
public class JogSizeSlider extends JPanel{

	/**
	 * Constructor builds custom JPanel that contains a heading, file path text field, and filechooser button
	 * @param heading
	 */
	public JogSizeSlider(String heading) {
		this.heading = heading;
		initUI();
	}

	/**
	 * Initialize UI elements for JPanel
	 */
	private void initUI(){
		//Input heading panel
		stepSizeHeadingPanel = new JPanel();
		stepSizeHeadingPanel.setLayout(new BoxLayout(stepSizeHeadingPanel, BoxLayout.X_AXIS));
		stepSizeHeadingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		stepSizeHeadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 3));
		stepSizeLabel = new JLabel(heading);
		stepSizeHeadingPanel.add(stepSizeLabel);
		//Step Size Slider Panel
		inputSliderPanel = new JPanel();
		inputSliderPanel.setLayout(new BoxLayout(inputSliderPanel, BoxLayout.X_AXIS));
		inputSliderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputSliderPanel.setBorder(BorderFactory.createEmptyBorder(17, 0, 10, 10));
		stepSizeInput = new JSlider(STEP_MIN, STEP_MAX, STEP_DEF);
		stepSizeInput.setMaximumSize(new Dimension(SLIDER_WIDTH, stepSizeInput.getPreferredSize().height + 35));
		stepSizeInput.setPreferredSize(new Dimension(SLIDER_WIDTH, stepSizeInput.getPreferredSize().height + 35));
		stepSizeInput.setMinimumSize(new Dimension(SLIDER_WIDTH, stepSizeInput.getPreferredSize().height + 35));
		stepSizeInput.setMajorTickSpacing(5);
		stepSizeInput.setMinorTickSpacing(1);
		stepSizeInput.setPaintTicks(true);
		stepSizeInput.setPaintLabels(true);
		stepSizeInput.setFont(new Font("Serif", Font.ITALIC, 12));
		inputSliderPanel.add(stepSizeInput);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(stepSizeHeadingPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(inputSliderPanel);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
	}

	/**
	 * Get step size value from slider
	 * @return int Step Size
	 */
	public int getStepSizeValue(){
		return stepSizeInput.getValue();
	}

	/**
	 * Set step size value
	 * @param step
	 */
	public void setStepSizeValue(int step){
		stepSizeInput.setValue(step);
	}
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Members
	 */
	private JPanel stepSizeHeadingPanel;
	private JPanel inputSliderPanel;
	private JLabel stepSizeLabel;
	private JSlider stepSizeInput;
	private String heading;
	public JButton sendInstructionButton;
	private final int STEP_MIN = 0;
	private final int STEP_MAX = 20;
	private final int STEP_DEF = 10;
	private final int SLIDER_WIDTH = 275;
}

package manual_control_ui.view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;

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
		stepSizeInput.setMajorTickSpacing(1);
		stepSizeInput.setPaintTicks(true);
		stepSizeInput.setPaintLabels(true);
		//custom labels
		Hashtable<Integer, JLabel> sliderLabels = new Hashtable<Integer, JLabel>();
		sliderLabels.put(SLIDER_0_25, new JLabel("0.25"));
		sliderLabels.put(SLIDER_0_50, new JLabel("0.50"));
		sliderLabels.put(SLIDER_1, new JLabel("1.0"));
		sliderLabels.put(SLIDER_5, new JLabel("5"));
		sliderLabels.put(SLIDER_10, new JLabel("10"));
		sliderLabels.put(SLIDER_20, new JLabel("20"));
		sliderLabels.put(SLIDER_50, new JLabel("50"));
		sliderLabels.put(SLIDER_100, new JLabel("100"));
		stepSizeInput.setLabelTable(sliderLabels);
		setStepSizeValue(SLIDER_5);
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
	public double getStepSizeValue(){
		switch(stepSizeInput.getValue()){
		case SLIDER_0_25:
			return 0.25;
		case SLIDER_0_50:
			return 0.50;
		case SLIDER_1:
			return 1.0;
		case SLIDER_5:
			return 5.0;
		case SLIDER_10:
			return 10.0;
		case SLIDER_20:
			return 20.0;
		case SLIDER_50:
			return 50.0;
		case SLIDER_100:
			return 100.0;
		default:
			return 0.0;
		}
	}

	/**
	 * Set step size value
	 * @param step
	 */
	public void setStepSizeValue(int stepSize){
		stepSizeInput.setValue(stepSize);
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
	private final int STEP_MAX = 7;
	private final int STEP_DEF = 1;
	private final int SLIDER_WIDTH = 275;
	public static final int SLIDER_0_25 = 0;
	public static final int SLIDER_0_50 = 1;
	public static final int SLIDER_1 = 2;
	public static final int SLIDER_5 = 3;
	public static final int SLIDER_10 = 4;
	public static final int SLIDER_20 = 5;
	public static final int SLIDER_50 = 6;
	public static final int SLIDER_100 = 7;
	
}

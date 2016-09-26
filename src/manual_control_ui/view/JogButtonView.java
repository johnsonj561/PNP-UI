package manual_control_ui.view;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JoxXYButtonView Panel constructs a panel that displays buttons: ++Y, --X, ++X, --Y
 * @author Justin Johnson
 */
public class JogButtonView extends JPanel{

	/**
	 * JogXYButtonView creates Y+, Y-, X+, X- buttons in a cross layout 
	 */
	public JogButtonView() {
		initUI();
		addButtonsToGrid();
	}

	/**
	 * Initialize JogXY UI Elements
	 */
	private void initUI(){
		//initialize images for Buttons
		try {
			initButtonImages();
		} catch (IOException e) {
			System.out.println("JogXYBUttonView:\ninitUI(): Error initializing JButton Image Icons");
			e.printStackTrace();
		}
		
		//create 3x3 grid to display to display XZ Jog Buttons at N S E W cells
		jogXYPanel = new JPanel();
		jogXYPanel.setLayout(new GridLayout(3, 3, 10, 10));
		//jogXYPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jogXYPanel.setMinimumSize(new Dimension(185,185));
		jogXYPanel.setMaximumSize(new Dimension(185,185));
		jogXYPanel.setPreferredSize(new Dimension(185,185));

		//create 3x1 grid to display to display Z Jog Buttons at top/bottom
		jogZPanel = new JPanel();
		jogZPanel.setLayout(new GridLayout(3, 1, 10, 10));
		//jogZPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jogZPanel.setMinimumSize(new Dimension(50,185));
		jogZPanel.setMaximumSize(new Dimension(50,185));
		jogZPanel.setPreferredSize(new Dimension(50,185));
	
		//create 3x1 gridto display spindle rotation controls
		spindlePanel = new JPanel();
		spindlePanel.setLayout(new GridLayout(3, 1, 10, 10));
		//spindlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		spindlePanel.setMinimumSize(new Dimension(50,185));
		spindlePanel.setMaximumSize(new Dimension(50,185));
		spindlePanel.setPreferredSize(new Dimension(50,185));
		
		//initialize buttons
		jogZMinusButton = new JButton(new ImageIcon(jogZMinusImage));
		jogZPlusButton = new JButton(new ImageIcon(jogZPlusImage));
		jogXMinusButton = new JButton(new ImageIcon(jogXMinusImage));
		jogXPlusButton = new JButton(new ImageIcon(jogXPlusImage));
		jogYMinusButton = new JButton(new ImageIcon(jogYMinusImage));
		jogYPlusButton = new JButton(new ImageIcon(jogYPlusImage));
		spindleCWButton = new JButton(new ImageIcon(spindleCWImage));
		spindleCCWButton = new JButton(new ImageIcon(spindleCCWImage));
		
		//add elements to this layout
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(jogXYPanel);
		this.add(Box.createRigidArea(new Dimension(30, 0)));
		this.add(jogZPanel);
		this.add(Box.createRigidArea(new Dimension(30, 0)));
		this.add(spindlePanel);
	}

	/**
	 * Add jog buttons to 3x3 grid with buttons located N E S W
	 */
	private void addButtonsToGrid(){
		jogXYPanel.add(new JLabel());
		jogXYPanel.add(jogYPlusButton);
		jogXYPanel.add(new JLabel());
		jogXYPanel.add(jogXMinusButton);
		jogXYPanel.add(new JLabel());
		jogXYPanel.add(jogXPlusButton);
		jogXYPanel.add(new JLabel());
		jogXYPanel.add(jogYMinusButton);
		jogXYPanel.add(new JLabel());
		jogZPanel.add(jogZPlusButton);
		jogZPanel.add(new JLabel());
		jogZPanel.add(jogZMinusButton);
		spindlePanel.add(spindleCWButton);
		spindlePanel.add(new JLabel());
		spindlePanel.add(spindleCCWButton);
	}

	/**
	 * Initialize the image icons to be added to jog buttons
	 * @throws IOException
	 */
	private void initButtonImages() throws IOException{
		jogXMinusImage = ImageIO.read(getClass().getResource("/images/jogXMinus-40x20.png"));
		jogXPlusImage = ImageIO.read(getClass().getResource("/images/jogXPlus-40x20.png"));
		jogYMinusImage = ImageIO.read(getClass().getResource("/images/jogYMinus-40x20.png"));
		jogYPlusImage = ImageIO.read(getClass().getResource("/images/jogYPlus-40x20.png"));
		jogZMinusImage = ImageIO.read(getClass().getResource("/images/jogZMinus-40x20.png"));
		jogZPlusImage = ImageIO.read(getClass().getResource("/images/jogZPlus-40x20.png"));
		spindleCWImage = ImageIO.read(getClass().getResource("/images/spindleCW-20x20.png"));
		spindleCCWImage = ImageIO.read(getClass().getResource("/images/spindleCCW-20x20.png"));
	}

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class Variables
	 */
	private JPanel jogXYPanel;
	private JPanel jogZPanel;
	private JPanel spindlePanel;
	public JButton jogXMinusButton;
	public JButton jogXPlusButton;
	public JButton jogYMinusButton;
	public JButton jogYPlusButton;
	public JButton spindleCWButton;
	public JButton spindleCCWButton;
	public JButton jogZMinusButton;
	public JButton jogZPlusButton;
	private Image jogXMinusImage;
	private Image jogXPlusImage;
	private Image jogYMinusImage;
	private Image jogYPlusImage;
	private Image jogZMinusImage;
	private Image jogZPlusImage;
	private Image spindleCWImage;
	private Image spindleCCWImage;
}

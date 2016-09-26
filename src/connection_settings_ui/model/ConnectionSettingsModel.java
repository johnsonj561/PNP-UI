package connection_settings_ui.model;

/**
 * Class stores Connection Settings
 * @author Justin Johnson
 *
 */
public class ConnectionSettingsModel {

	public ConnectionSettingsModel() {
		baudRate = DEF_BAUD;
		feedRate = DEF_FEED_RATE;
		width = DEF_WIDTH;
		depth = DEF_DEPTH;
		height = DEF_HEIGHT;
		stepSize = DEF_STEP_SIZE;
		connected = false;
	}
	
	/**
	 * Restore all values to default
	 */
	public void restoreDefaultValues(){
		baudRate = DEF_BAUD;
		feedRate = DEF_FEED_RATE;
		width = DEF_WIDTH;
		depth = DEF_DEPTH;
		height = DEF_HEIGHT;
		stepSize = DEF_STEP_SIZE;
	}
	
	/**
	 * Get Baud Rate
	 * @return int baudrate
	 */
	public int getBaudRate() {
		return baudRate;
	}
	/**
	 * Set Baud Rate
	 * @param baudRate
	 */
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	/**
	 * Get Feed Rate
	 * @return int feed rate
	 */
	public int getFeedRate() {
		return feedRate;
	}
	/**
	 * Set Feed Rate
	 * @param feedRate
	 */
	public void setFeedRate(int feedRate) {
		this.feedRate = feedRate;
	}
	/**
	 * Get Workspace Width (X)
	 * @return int workspace width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Set Workspace Width (X)
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * Get Workspace Depth (Y)
	 * @return int workspace depth
	 */
	public int getDepth() {
		return depth;
	}
	/**
	 * Set Workspace Depth (Y)
	 * @param depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	/**
	 * Get Workspace Height (Z)
	 * @return int workspace height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * Set Workspace Height (Z)
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * Get step size for jog control
	 * @param int stepSize
	 */
	public void setStepSize(int stepSize){
		this.stepSize = stepSize;
	}
	/**
	 * Set step size for jog control
	 * @return int stepsize
	 */
	public int getStepSize(){
		return this.stepSize;
	}
	/**
	 * Return connection status
	 * @return True if connected
	 */
	public boolean isConnected() {
		return connected;
	}
	/**
	 * Set connection status
	 * @param connected
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Get Default Baud Rate
	 * @return
	 */
	public int getDefaultBaudRate() {
		return DEF_BAUD;
	}

	/**
	 * Get Default Feed Rate
	 * @return
	 */
	public int getDefaultFeedRate() {
		return DEF_FEED_RATE;
	}
	
	/**
	 * Get Default Width
	 * @return
	 */
	public int getDefaultWidth() {
		return DEF_WIDTH;
	}

	/**
	 * Get Default Depth
	 * @return
	 */
	public int getDefaultDepth() {
		return DEF_DEPTH;
	}

	/**
	 * Get Default Height
	 * @return
	 */
	public int getDefaultHeight() {
		return DEF_HEIGHT;
	}


	

	private int baudRate;
	private int feedRate;
	private int width;
	private int depth;
	private int height;
	private int stepSize;
	private boolean connected;
	public static final int DEF_BAUD = 9600;
	public static final int DEF_FEED_RATE = 0;
	public static final int DEF_WIDTH = 500;
	public static final int DEF_DEPTH = 500;
	public static final int DEF_HEIGHT = 50;
	public static final int DEF_STEP_SIZE = 10;
	
	
}

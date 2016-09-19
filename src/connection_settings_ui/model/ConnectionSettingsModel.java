package connection_settings_ui.model;

/**
 * Class stores Connection Settings
 * @author Justin Johnson
 *
 */
public class ConnectionSettingsModel {

	public ConnectionSettingsModel() {
		baudRate = defaultBaudRate;
		feedRate = defaultFeedRate;
		width = defaultWidth;
		depth = defaultDepth;
		height = defaultHeight;
		connected = false;
	}
	
	
	/**
	 * Get Baud Rate
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
		return defaultBaudRate;
	}

	/**
	 * Get Default Feed Rate
	 * @return
	 */
	public int getDefaultFeedRate() {
		return defaultFeedRate;
	}
	
	/**
	 * Get Default Width
	 * @return
	 */
	public int getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * Get Default Depth
	 * @return
	 */
	public int getDefaultDepth() {
		return defaultDepth;
	}

	/**
	 * Get Default Height
	 * @return
	 */
	public int getDefaultHeight() {
		return defaultHeight;
	}



	private int baudRate;
	private int feedRate;
	private int width;
	private int depth;
	private int height;
	private final int defaultBaudRate = 9600;
	private final int defaultFeedRate = 0;
	private final int defaultWidth = 500;
	private final int defaultDepth = 500;
	private final int defaultHeight = 50;
	private boolean connected;
	
}

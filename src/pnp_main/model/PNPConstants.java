package pnp_main.model;

/**
 * PNP Machine Constants
 * @author Justin Johnson
 *
 */
public class PNPConstants {

	//connection status
	public static final int STATUS_CONNECTED = 1;
	public static final int STATUS_DISCONNECTED = 2;
	public static final int STATUS_CONNECT_ERROR = 3;
	public static final int STATUS_DISCONNECT_ERROR = 4;
	public static final int STATUS_SEND_ERROR = 5;
	//commands
	public static final String HOME_X = "G1 X0";
	public static final String HOME_Y = "G1 Y0";
	public static final String HOME_Z = "G1 Z0";
	public static final String HOME_ALL = "G28";
	public static final String EMERGENCY_STOP = "!!!";
	//threshold for Computer vision routine
	public static final double IMAGE_ORIENTATION_THRESHOLD = 1.0;
	//PNP Main's JFrame Location
	public static final int JFRAME_X_LOC = 50;
	public static final int JFRAME_Y_LOC = 25;
	//PNP Main's JFrame Preferred Size
	public static final int PREFERRED_WIDTH = 700;
	public static final int PREFERRED_HEIGHT = 700;

}
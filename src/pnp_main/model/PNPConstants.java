package pnp_main.model;

/**
 * PNP Machine Constants
 * @author Justin Johnson
 *
 */
public final class PNPConstants {

	//connection status messages
	public static final int STATUS_CONNECTED = 1;
	public static final int STATUS_DISCONNECTED = 2;
	public static final int STATUS_CONNECT_ERROR = 3;
	public static final int STATUS_DISCONNECT_ERROR = 4;
	public static final int STATUS_SEND_ERROR = 5;
	public static final int STATUS_LOG_FILE_ERROR = 6;
	public static final int STATUS_EMERGENCY_STOPPED =7;
	//Homing commands
	public static final String HOME_X = "G28 X";
	public static final String HOME_Y = "G28 Y";
	public static final String HOME_ALL = "G28";
	//emergency stop
	public static final String EMERGENCY_STOP = "!!!";
	//lightbox and computer vision
	public static final String MOVE_TO_LIGHTBOX = "G1 X42 Y49 ; Move to lightbox\n";
	public static final String LIGHT_ON = "M12 ; Light On\n";
	public static final String LIGHT_OFF = "M13 ; Light Off\n";
	public static final String LIFT_ABOVE_LIGHTBOX = "G1 Z7 ; Lift Above Lightbox\n";
	public static final String LOWER_TO_LIGHTBOX = "G1 Z5 ; Lower To Lightbox\n";
	public static final String LIGHT_BOX_X = "42.0";
	public static final String LIGHT_BOX_Y = "49.0";
	public static final String CV_IMAGE_PATH = "C:/PNPLog/CV/";
	//PCB location
	public static final String PCB_X = "105";
	public static final String PCB_Y = "18";
	//threshold for Computer vision routine
	public static final double IMAGE_ORIENTATION_THRESHOLD = 1.0;
	public static final double IMAGE_POSITION_THRESHOLD = 0.5;
	public static final int LIGHTBOX_HEIGHT = 6;
	//Vacuum controls
	public static final String VACUUM_ON = "M10";
	public static final String VACUUM_OFF = "M11";
	//PNP Main's JFrame Location
	public static final int JFRAME_X_LOC = 50;
	public static final int JFRAME_Y_LOC = 25;
	//PNP Main's JFrame Preferred Size
	public static final int PREFERRED_WIDTH = 750;
	public static final int PREFERRED_HEIGHT = 700;

}

package jssc_usb;

/**
 * UsbEvent interface enables PNPMainController to listen to UsbDevice for read/connection events
 * @author Justin Johnson
 *
 */
public interface UsbEvent {
	
	/**
	 * Read event
	 * @param message
	 */
	public void UsbReadEvent(String message);
	
	
	
}

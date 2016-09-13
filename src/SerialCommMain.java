import jssc.SerialPortException;
import jssc_usb.UsbDevice;

//jssc testing
public class SerialCommMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UsbDevice mUsbDevice = new UsbDevice();
		String[] deviceList = mUsbDevice.getSerialPortList();

		mUsbDevice.updatePortID(deviceList[0]);
		
		mUsbDevice.openPort();
		try {
			mUsbDevice.dumpPortDescription();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		
	}
}

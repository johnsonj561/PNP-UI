package jssc_usb;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class UsbDevice {

	/**
	 * UsbDevice Constructor gets list of system's current serial ports and initializes portID to NULL
	 * Note: portID must be set with updatePortID(String) before establishing connection
	 */
	public UsbDevice(){
		System.out.println("\nUsbDevice constructor: this.portID initialized to null");
		updateSerialPortList();
		portID = null;
	}
	
	/**
	 * UsbDevice Constructor gets list of system's current serial ports and initializes portID to passed value
	 * Assumes String port is accurate identifier of system port ie) 'COM5'
	 * @param String port assigned for connection
	 */
	public UsbDevice(String port){
		System.out.println("\nUsbDevice constructor: this.portID initialized to " + port);
		updateSerialPortList();
		portID = port;
		serialPort = new SerialPort(portID);
	}
	
	/**
	 * Initialize SerialPortEventListener to handle incoming data
	 * @throws SerialPortException
	 */
	public void initListener() throws SerialPortException{
		serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent event) {
				//TODO check for read event
				readMessage();
			}
		});
	}
	
	/**
	 * Send message to assigned port
	 * @param message to be sent
	 * @return true if message sent successful
	 */
	public boolean writeMessage(String message){
		try {
	        System.out.println("\nPort opened: " + serialPort.openPort());
	        //setParams(int baudRate, int dataBits, int stopBits, int parity)
	        System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
	        //Write byte array to port
	        boolean result = serialPort.writeBytes(message.getBytes());
	        if(result){
		        System.out.println("Message sent successful: " + message);
	        }
	        System.out.println("Port closed: " + serialPort.closePort());
	        return true;
	    }
	    catch (SerialPortException ex){
	        System.out.println(ex);
	        return false;
	    }
	}
	
	/**
	 * Read all available bytes from port
	 * @return byte[] from port or null if read fails
	 */
	public byte[] readMessage(){
		byte[] message = null;
		try {
			message = serialPort.readBytes();
			return message;
		} catch (SerialPortException e) {
			System.out.println("UsbDevice serialEvent: Unable to readBytes()");
			e.printStackTrace();
			return message;
		}
	}
	
	/**
	 * Update portID to allow for connection for specified device
	 * @param port String identifier of specified port
	 */
	public void updatePortID(String port){
		System.out.println("\nUpdating this.portID = " + port);
		portID = port;
		serialPort = new SerialPort(portID);
	}
	
	/**
	 * Update the array of serial ports in the system using default settings
	 * @return String array of serial port names
	 */
	private void updateSerialPortList(){
		System.out.println("\nUpdating list of system's available serial ports");
		serialPortList = SerialPortList.getPortNames();
		for(String port : serialPortList){
			System.out.println(port);
		}
	}
	
	/**
	 * Print various port attributes for debugging
	 * @throws SerialPortException
	 */
	public void dumpPortDescription() throws SerialPortException{
		System.out.println("\nPort Description Dump\nSerial Port: " + serialPort.getPortName());
		System.out.println("Is Opened: " + serialPort.isOpened());
		if(serialPort.isOpened()){
			System.out.println("Flow Control Mode: " + serialPort.getFlowControlMode());
			System.out.println("Clear To Send Line: " + serialPort.isCTS());
			System.out.println("Data Set Ready Line: " + serialPort.isDSR());
			System.out.println("RING Line: " + serialPort.isRING());
			System.out.println("RLSD Line: " + serialPort.isRLSD());
		}
	}
	
	/**
	 * Get String array list of serial ports in the system
	 * @return
	 */
	public String[] getSerialPortList(){;
		return serialPortList;
	}
	
	/**
	 * Open port corresponding to this.portID
	 * @return true if port opened successfully
	 */
	public boolean openPort(){
		if(!serialPort.isOpened()){
			System.out.println("Trying to open port " + portID);
			try {
				serialPort.openPort();
				return true;
			} catch (SerialPortException e) {
				System.out.println("UsbDevice openPort(): Unableto open port");
				e.printStackTrace();
			}
			return false;
		}
		System.out.println("UsbDevice openPort(): Port " + portID + " is already open");
		return true;
	}

	/**
	 * Close port corresponding to this.portID
	 * @return true if port closes successfully
	 */
	public boolean closePort(){
		if(serialPort.isOpened()){
			try {
				serialPort.closePort();
				return true;
			} catch (SerialPortException e) {
				System.out.println("\nUsbDevice closePort():Error closing port");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Check if this.serialPort is open
	 * @return true if port is open
	 */
	public boolean isOpen(){
		if(serialPort.isOpened()){
			return true;
		}
		return false;
	}
	

	
	private SerialPort serialPort;
	private String[] serialPortList;
	private String portID;
	
	

}

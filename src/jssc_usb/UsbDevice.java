package jssc_usb;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * UsbDevice provides system's available Ports and read/write functionality
 * @author Justin Johnson
 *
 */
public class UsbDevice {

	/**
	 * UsbDevice Constructor gets list of system's current serial ports and initializes portID
	 * Assumes String port is accurate identifier of system port ie) 'COM5'
	 * Sets default params: DATABITS_8, STOPBITS_1, PARITY_NONE
	 * @param String port assigned for connection
	 * @param int baudRate
	 */
	public UsbDevice(String port, int baudRate){
		System.out.println("\nUsbDevice constructor: this.portID initialized to " + port);
		updateSerialPortList();
		portID = port;
		serialPort = new SerialPort(portID);
		this.baudRate = baudRate;
		openPort();
		
	}

	/**
	 * Send message to assigned port and verify that message was sent successfully
	 * @param message to be sent
	 * @return true if message sent successful
	 */
	public boolean writeMessage(String message){
		if(isOpen){
			try {
				if(/*serialPort.writeBytes(message.getBytes())*/serialPort.writeString(message)){
					System.out.println("UsbDevice:\nwriteMessage(): Message sent successful -> " + message);
					return true;
				}
				return false;	//else message did not send successful!
			}
			catch (SerialPortException ex){
				System.out.println("UsbDevice:\nwriteMessage(): Error sending message -> " + message);
				System.out.println(ex);
				return false;
			}
		}
		else{
			System.out.println("UsbDevice: In writeMessage()\nPort is not open, unable to send");
			return false;
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
	 * Static method that returns system's available Serial Ports
	 * @return String[] of system's available Serial Ports
	 */
	public static String[] findSerialPorts(){
		return SerialPortList.getPortNames();
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
	 * Return UsbDevice port name currently assigned
	 * @return
	 */
	public String getPortName(){
		return serialPort.getPortName();
	}
	
	/**
	 * Get String array list of serial ports in the system
	 * @return
	 */
	public String[] getSerialPortList(){	
		return serialPortList;
	}

	
	/**
	 * Open port corresponding to this.portID
	 * Set default serialport params and baudrate as defined in UsbDevice constructor
	 * @return true if port opened successfully
	 */
	public boolean openPort(){
		try{
			serialPort.openPort();
			serialPort.setParams(baudRate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.addEventListener(new SerialPortReader());
			isOpen = true;
			System.out.println("UsbDevice:\nopen(): Port defined and opened without error: " + portID);
			return true;
		}catch(SerialPortException e) {
			isOpen = false;
			System.out.println("UsbDevice:\nopen():Unable to open port: " + portID + "\n" + e);
			return false;
		}
	}

	/**
	 * Close port corresponding to this.portID
	 * @return true if port closes successfully
	 */
	public boolean close(){
		if(serialPort.isOpened()){
			try {
				serialPort.closePort();
				System.out.println("UsbDevice:\nclose(): port closed with out error");
				isOpen = false;
				return true;
			} catch (SerialPortException e) {
				System.out.println("\nUsbDevice:\nclosePort():Error closing port");
				e.printStackTrace();
			}
		}
		isOpen = false;
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

	public static SerialPort serialPort;
	private String[] serialPortList;
	private String portID;
	private boolean isOpen;
	private int baudRate;
	

	static class SerialPortReader implements SerialPortEventListener{

		String inputBuffer = "";
		
		@Override
		public void serialEvent(SerialPortEvent event) {
			System.out.println("UsbDevice:SerialPortReader:\nserialevent(event):");
			if(event.isRXCHAR()){
				System.out.println("event.isRXCHAR()");
				
				readMessage();
			}
			
		}
		
		/**
		 * Read all available bytes from port
		 * @return byte[] from port or null if read fails
		 */
		public String readMessage(){
			
			try {
				System.out.println("SerialPortReader:\nreadMessage():");
				inputBuffer += serialPort.readString();
				if(inputBuffer.endsWith("\n")){
					System.out.println(inputBuffer);
					inputBuffer = "";
				}
				return inputBuffer;
			} catch (SerialPortException e) {
				System.out.println("SerialPortReader:\nreadMessage(): Unable to readBytes()");
				e.printStackTrace();
				return null;
			}
		}
		
	}
}

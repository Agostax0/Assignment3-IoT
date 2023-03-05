

package main;



import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import main.DataPoint;
import com.fazecast.jSerialComm.*;


public class RoomState {
	private enum Actuators {
	    LED(1),
	    SERVO(2),
	    PIR(3),
	    LL(4);

	    private int id;

	    private Actuators(int id) {
	        this.id = id;
	    }

	    public int getId() {
	        return id;
	    }
	}
	private LinkedList<DataPoint> history = new LinkedList<>();
	private LinkedList<Device> device_states =  new LinkedList<>(
			List.of(new Device("LED"), new Device("SERVO"), new Device("PIR"), new Device("LL")));
	

	private final int MAX_STATES = 100;
	
	private final int LIGHT_LEVEL_THRESHOLD = 500;
	
	private final String FRONTEND = "F";
	private final String ESP = "E";
	private final String SMARTPHONE = "S";
	private final String ARDUINO = "A";
	private final String BACKEND = "B";
	
	private boolean firstTimeEntering = true;
	
	private final LocalTime _8AM = LocalTime.of(8, 0);
	private final LocalTime _7PM = LocalTime.of(19, 0);
	
	private SerialCommunication comm;
	
	public RoomState(SerialCommunication comm) {
		this.comm = comm;
	}
	
	public void addToHistory(DataPoint d) {
		history.addFirst(d);
		if (history.size() > MAX_STATES) {
			history.removeLast();
		}
		
		updateState(d);
	}

	
	private void updateState(DataPoint d) {
		
		
		Device subj = device_states.get(d.getDeviceID());
		
		if(subj.getValue() != d.getValue()) {
			//this calls for an update on the stored info
			
			if(d.getSource().equals(FRONTEND)) {
				//actions are to be taken
				String cmd = "B,A{"+d.getDeviceID()+"}["+d.getValue()+"]|";
				comm.write(cmd);
			}
			
			LocalTime currentTime = LocalTime.now();
			
			if(currentTime.isAfter(_8AM) && currentTime.isBefore(_7PM)) {
				//8AM TO 7PM

			}
			else {
				//7PM TO 8AM
			}
			
			device_states.get(d.getDeviceID() - 1).update(d.getValue());
		}
		

		/*device_states.get(p.getDeviceID() - 1).update(p.getValue());
		if(device_states.stream().allMatch(elem -> elem.getValue()!=-1)) {
			System.out.println("updating room");
			device_states.forEach(device -> System.out.println(device.toString()));
			RoomState.update();
		}*/
		
		/*if(getDeviceRefValue(Device_Enum.PIR_ID)==0) {
			SerialMonitor.write("B,A{1}[000]".getBytes());
		}else {
			if(getDeviceRefValue(Device_Enum.LED_ID)==0 && getDeviceRefValue(Device_Enum.LL_ID)/4 < LIGHT_LEVEL_THRESHOLD) {
				SerialMonitor.write("B,A{1}[111]".getBytes());
			}
		}
		
		int timeOfDay = new Date().getHours()*10 + new Date().getMinutes();
		
		if(timeOfDay > 800 && timeOfDay < 1900) {
			if(firstTimeOpening && getDeviceRefValue(Device_Enum.PIR_ID)!=0) {
				firstTimeOpening = false;
				SerialMonitor.write("B,A{2}[000]".getBytes()); //OPENING THE BLINDERS
			}
		}
		else {
			if(getDeviceRefValue(Device_Enum.PIR_ID)==0 && getDeviceRefValue(Device_Enum.SERVO_ID)<180) {
				firstTimeOpening = true;
				SerialMonitor.write("B,A{2}[180]".getBytes());//CLOSING THE BLINDERS
				//Comm.write("B,A{2}[180]"); 
			}
		}*/
	}
	
	
}




package main;




import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.DataPoint;
import com.fazecast.jSerialComm.*;
import java.time.*;

public class RoomState {
	private Map<Long,LinkedList<Device>> history = new HashMap<>();
	private LinkedList<Device> device_states =  new LinkedList<>(
			List.of(new Device("Dummy"),new Device("LED"), new Device("SERVO"), new Device("PIR"), new Device("LL")));
	

	private final int MAX_STATES = 100;
	
	private final int LIGHT_LEVEL_THRESHOLD = 500;
	
	private final String FRONTEND = "F";
	private final String ESP = "E";
	private final String SMARTPHONE = "S";
	private final String ARDUINO = "A";
	private final String BACKEND = "B";
	
	private final int LED = 1;
	private final int SERVO = 2;
	private final int PIR = 3;
	private final int LL = 4;
	
	private boolean firstTimeEntering = true;
	
	private final int _8AM = 800;
	private final int _7PM = 1900;
	
	private SerialCommunication comm;
	
	public RoomState(SerialCommunication comm) {
		this.comm = comm;
	}
	
	public Map<Long, LinkedList<Device>> getHistory(){
		return this.history;
	}
	
	public LinkedList<Device> getDeviceStates(){
		return this.device_states;
	}
	
	public void addToHistory(DataPoint d) {
		
		updateState(d);
	}
	
	private void updateState(DataPoint d) {
		
		
		Device subj = device_states.get(d.getDeviceID());
		
		if(subj.getValue() != d.getValue()) {
			//this calls for an update on the stored info
			
			if(d.getSource().equals(FRONTEND)) {
				//actions are to be taken
				String cmd = "B,A{"+d.getDeviceID()+"}["+d.getValue()+"11]|";
				device_states.get(d.getDeviceID()).update(d.getValue());
				//comm.write("B,A{1}[111]|".strip());
				
				if(d.getDeviceID() == LED) {
					comm.write("B,A{1}["+ (d.getValue()>0 ? "111" : "000") +"]|".strip());
				}
				else {
					comm.write("B,A{"+d.getDeviceID()+"}["+ d.getValue() +"]|".strip());
				}
			}
			else {
				device_states.get(d.getDeviceID()).update(d.getValue());
				checkIntegrity();
			}
			
			
			//adding the current state of the machine
			var state = new LinkedList<>(
					List.of(new Device("Dummy"),new Device("LED"), new Device("SERVO"), new Device("PIR"), new Device("LL")));
			
			//System.out.println("??");
			
			state.get(LED).update(device_states.get(LED).getValue());
			state.get(SERVO).update(device_states.get(SERVO).getValue());
			state.get(PIR).update(device_states.get(PIR).getValue());
			state.get(LL).update(device_states.get(LL).getValue());
			
			this.history.put(System.currentTimeMillis(), state);
			
		}
		
		
	}

	private void checkIntegrity() {
		
		Device PIR_Device = device_states.get(PIR);
		Device LL_Device = device_states.get(LL);
		Device LED_Device = device_states.get(LED);
		Device SERVO_Device = device_states.get(SERVO);
		
		if(PIR_Device.getValue()!=0) {
			//someone is detected
			if(LED_Device.getValue()==0 && LL_Device.getValue()/4 < LIGHT_LEVEL_THRESHOLD) {
				//LED is off and Light is low enough
				comm.write("B,A{"+LED+"}"+"["+"111"+"]|");
				LED_Device.update(111);
				//turn on the led
			}
		}
		else {
			//none is detected
			comm.write("B,A{"+LED+"}"+"["+"000"+"]|");
			LED_Device.update(000);
		}
		
		int currentTime = new Date().getHours()*100 + new Date().getMinutes();
		
		if(currentTime > (_8AM) && currentTime<(_7PM)) {
			//8AM TO 7PM
			if(firstTimeEntering && PIR_Device.getValue()!=0) {
				firstTimeEntering = false;
				//opening the curtains
				comm.write("B,A{"+SERVO+"}"+"["+"000"+"]|");
				SERVO_Device.update(000);
			}
		}
		else {
			//7PM TO 8AM
			if(PIR_Device.getValue()==0 && SERVO_Device.getValue()<180) {
				//closing the curtains
				comm.write("B,A{"+SERVO+"}"+"["+"180"+"]|");
				SERVO_Device.update(180);
			}
		}
		
	}
	
	
}


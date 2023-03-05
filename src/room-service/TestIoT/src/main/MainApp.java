package main;

import java.util.LinkedList;
import java.util.List;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class MainApp {

	private final String FRONTEND = "F";
	private final String ARDUINO = "A";
	private final String SMARTPHONE = "S";
	private final String ESP = "E";

	public static void main(String[] args) {
		// uso due thread che comunicano con MainApp per inviare alla roomstate cosa ricevono, uno si focalizza su vertx e l'altro sulla seriale
		SerialCommunication serialComm = new SerialCommunication("COM9");
		RoomState roomState = new RoomState(serialComm);
		
		ServerThread server = new ServerThread(roomState);
		
		server.start();
		
		
		serialComm.open();
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
}

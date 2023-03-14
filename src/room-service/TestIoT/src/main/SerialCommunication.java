package main;

import com.fazecast.jSerialComm.*;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class SerialCommunication {
    
    private SerialPort serialPort;
    private String responseString = null;
    private WebClient client = WebClient.create(Vertx.vertx());
    
    public SerialCommunication(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    byte[] readBuffer = new byte[1024];
                    int numRead = serialPort.readBytes(readBuffer, readBuffer.length);

                    String responseChunk = new String(readBuffer, 0, numRead);
                    //System.out.println("Received Piece: " + responseChunk);
                    if (responseString == null) {
                        responseString = responseChunk;
                    } else {
                        responseString += responseChunk;
                    }
                    
                    // Check if the response contains the terminating character
                    int terminatorIndex = responseString.indexOf("|");
                    if (terminatorIndex != -1) {
                        String response = responseString.substring(0, terminatorIndex);
                        responseString = responseString.substring(terminatorIndex + 1);
                        //System.out.println("Received String: " + response + "\t enable debug: " + String.valueOf(response.charAt(0)=='#'));
                       
                        if(response.charAt(0)=='#') {
                        	//System.out.println("DEBUG Received String: " + response);
                        }
                        else {
                        	JsonObject item = new JsonObject();
                    		item.put("command",response.strip());
                           
                    		//System.out.println("sending: "+response);
                    		
                            client
                    		.post(8080, "localhost", "/api/data")
                    		.sendJson(item);
                        }
                        
                        
                    }

                    
                    
                }
            }
        });
    }
    
    public boolean open() {
        return serialPort.openPort();
    }
    
    public void close() {
        serialPort.closePort();
    }
    
    public void write(String outputString) {
        byte[] outputBytes = outputString.getBytes();
        serialPort.writeBytes(outputBytes, outputBytes.length);
        System.out.println("Sent String: " + outputString + " lenght: " + outputBytes.length);
    }
}


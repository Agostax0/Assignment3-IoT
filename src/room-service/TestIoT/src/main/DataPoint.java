package main;

import java.nio.charset.Charset;


public class DataPoint {
	private long time;
	private String command;
	
	private String source;
	private int device;
	private int value;
	
	
	public DataPoint(String command) {
		this.time = System.currentTimeMillis();
		this.command = command;
		
		//System.out.println(command);
		
		this.source = String.valueOf(this.command.charAt(0));
		
		//A,B{1}[1000]
		
		this.device = Integer.parseInt(String.valueOf(this.command.charAt(4)));
		
		this.value = Integer.parseInt(this.command.substring(7,this.command.length()-1));
		
	}
	
	public long getTime() {
		return this.time;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public int getDeviceID() {
		return this.device;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "source: "+this.source +"\t"+ "target: " + this.device+ "\t" + "value: " + this.value;
		
	}
	
	
}

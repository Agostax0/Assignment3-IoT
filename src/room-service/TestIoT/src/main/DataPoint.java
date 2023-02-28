package main;

class DataPoint {
	private long time;
	private String command;
	
	public DataPoint(long time, String command) {
		this.time = time;
		this.command = command;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getCommand() {
		return command;
	}
	
	
}

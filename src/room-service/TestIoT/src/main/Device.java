package main;

public class Device {
	private int value = 0;
	public String name = null;

	public Device(String name) {
		this.name = name;
	}

	public void update(int value) {
			//System.out.println("Updated: " + this.name + " with: " + value);
			this.value = value;
	}

	public int getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}

	public String toString() {
		return this.name + "\t value: " + this.value;

	}
}
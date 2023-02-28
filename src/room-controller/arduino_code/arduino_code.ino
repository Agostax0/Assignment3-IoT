
#include <Servo.h>

Servo servo; 
const int ledPin = 8; // Built in LED in Arduino board
String msg, cmd;

void setup() {
  // Initialization
  pinMode(ledPin, OUTPUT);
  Serial.begin(9600); // Communication rate of the Bluetooth Module
  servo.attach(6);
  msg = "";
}

void loop() {

  // To read message received from other Bluetooth Device
  if (Serial.available() > 0) { // Check if there is data coming
    msg = Serial.readStringUntil(','); // Read the message as String

    String x = msg;    
    
    Serial.println("Android Command: " + msg);

    // Control LED in Arduino board
  if (msg == "<turn on>") {
    digitalWrite(ledPin, HIGH); // Turn on LED
    Serial.println("LED is turned on\n"); // Then send status message to Android
    msg = ""; // reset command
  }
  if (msg == "<turn off>") {
    digitalWrite(ledPin, LOW); // Turn off LED
    Serial.println("LED is turned off\n"); // Then send status message to Android
    msg = ""; // reset command
  }

  if (msg.substring(0,8) == "<change>") {
    String value = x.substring(8);
    int val = value.toInt();
    if(val!=0 && val<181){
      Serial.println("moved to " + value); // Then send status message to Android
      servo.write(val);
    }

    msg = ""; // reset command
  }
  }

  
}

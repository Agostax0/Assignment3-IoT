#include<Arduino.h>
#include<Servo.h>
#include<MyMsg.h>
Servo servo; 
const int ledPin = 2;
const int servoPin = 3;

void setup() {
  // Initialization
  pinMode(ledPin, OUTPUT);
  Serial.begin(9600);
  servo.attach(servoPin);
  servo.write(0);
}

void loop() {

  if (Serial.available() > 0) {
    String read = Serial.readStringUntil('|');

    //Serial.println("#read was: " + read);

    Action action = MyMsg(read).interpret();

    switch(action.ID){
      case LED_ID:
        digitalWrite(ledPin,action.value>0 ? HIGH : LOW);
        break;
      case SERVO_ID:
        servo.write(action.value);
        break;
      default:
        break;
    }

    Serial.println(MyMsg("").createMessage(BACKEND,action.ID,action.value));
  }

  
}

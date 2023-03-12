#include"SoftwareSerial.h"
#include <Arduino.h>
#include <ServoTimer2.h>
#include <MyMsg.h>
SoftwareSerial bluetooth_serial(8,9);

const int ledPin = 4;
const int servoPin = 5;
int debug = HIGH;
ServoTimer2 servo;

void setup()
{
  Serial.begin(9600);
  bluetooth_serial.begin(9600);

  pinMode(ledPin,OUTPUT);
  pinMode(servoPin,OUTPUT);

  servo.attach(servoPin);
  servo.write(map(0, 0, 180, 750, 2250));

  pinMode(13, OUTPUT);
}

void processSerial(Stream &serial, String source)
{
  if (serial.available())
  {
    String message = serial.readStringUntil('|');

    digitalWrite(13, debug);
    debug = !debug;


    Action action = MyMsg(message).interpret();

    //Serial.print(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));

    if (action.source.equals(SMARTPHONE))
    {
      Serial.print(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));
      // Serial.println(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));
    }

    switch (action.ID)
    {
    case LED_ID:
      // Serial.println("led");
      digitalWrite(ledPin, action.value > 0 ? HIGH : LOW);
      break;
    case SERVO_ID:
      //Serial.println("servo");
      servo.write(map(action.value, 0, 180, 750, 2250));
      break;
    default:
      // Serial.println("default");
      break;
    }

    if(source.equals("PC")){
      String update = MyMsg("").createMessage(HARDUINO,SMARTPHONE,action.ID,action.value);
      Serial.print("#"+update);
      bluetooth_serial.print(update);
    }
  }
}

void loop()
{
  processSerial(Serial, "PC");
  processSerial(bluetooth_serial, "BT");

  //Serial.begin(9600);
  //while(!Serial){}
  //bluetooth_serial.begin(9600);
  //while(!bluetooth_serial){}
}
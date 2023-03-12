#include "SoftwareSerial.h"
#include <Arduino.h>
#include <ServoTimer2.h>
#include <MyMsg.h>
#include <EventQueue.h>
SoftwareSerial bluetooth_serial(8, 9);

const int ledPin = 4;
const int servoPin = 5;
int debug = HIGH;
ServoTimer2 servo;

EventQueue eventQueue;

void setup()
{
  Serial.begin(9600);
  bluetooth_serial.begin(9600);

  pinMode(ledPin, OUTPUT);
  pinMode(servoPin, OUTPUT);

  servo.attach(servoPin);
  servo.write(map(0, 0, 180, 750, 2250));

  pinMode(13, OUTPUT);

  eventQueue = EventQueue();
}

void processSerial(Stream &serial, String source)
{
  if (serial.available())
  {
    String message = serial.readStringUntil('|');

    digitalWrite(13, debug);
    debug = !debug;

    eventQueue.enqueue(Event(message));
  } 
}
void handleEvent(Event &ev)
{
  Action action = MyMsg(ev.getCommand()).interpret();

  // Serial.print(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));

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
    // Serial.println("servo");
    servo.write(map(action.value, 0, 180, 750, 2250));
    break;
  default:
    // Serial.println("default");
    break;
  }

  if (action.source.equals("PC"))
  {
    String update = MyMsg("").createMessage(HARDUINO, SMARTPHONE, action.ID, action.value);
    Serial.print("#" + update);
    bluetooth_serial.print(update);
  }
}

void loop()
{
  processSerial(Serial, "PC");
  processSerial(bluetooth_serial, "BT");

  while(!eventQueue.isEmpty()){
    Event event = eventQueue.dequeue();
    handleEvent(event);
  }
}


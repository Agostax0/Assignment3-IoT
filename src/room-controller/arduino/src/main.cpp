#include "SoftwareSerial.h"
#include <Arduino.h>
#include <ServoTimer2.h>
#include <MyMsg.h>
#include <EventQueue.h>
SoftwareSerial bluetooth_serial(8, 9);

const int ledPin = 7;
const int servoPin = 5;
int debug = HIGH;
ServoTimer2 servo;

enum State
{
  IDLE,
  COMMAND_RECEIVED,
  SERVO_MOVEMENT,
  LED_ON,
  LED_OFF,
  SEND_TO_APP,
  SEND_TO_PC,
  ERROR
};

int currentState = IDLE;
Action action;
void setup()
{
  Serial.begin(9600);
  bluetooth_serial.begin(9600);

  pinMode(ledPin, OUTPUT);
  pinMode(servoPin, OUTPUT);

  servo.attach(servoPin);
  servo.write(map(0, 0, 180, 750, 2250));

  pinMode(13, OUTPUT);


  digitalWrite(ledPin,HIGH);
  delay(300);
  digitalWrite(ledPin,LOW);
}

void processSerial(Stream &serial, String source)
{
  if (serial.available())
  {
    String message = serial.readStringUntil('|');

    digitalWrite(13, debug);
    debug = !debug;

    action = MyMsg(message).interpret();

    currentState = COMMAND_RECEIVED;
    Serial.print(MyMsg("#").createMessage("COMMAND_RECEIVED", action.source, action.ID, action.value));
  }
}

int redirectInfo(String Source)
{
  if (Source.equals(BACKEND))
  {
    Serial.print(MyMsg("#").createMessage("SEND_TO_APP", action.source, action.ID, action.value));
    return SEND_TO_APP;
  }
  else
  {
    Serial.print(MyMsg("#").createMessage("SEND_TO_PC", action.source, action.ID, action.value));
    return SEND_TO_PC;
  }
}

void loop()
{
  switch (currentState)
  {
  case IDLE:
    // Serial.print(MyMsg("#").createMessage("IDLE", "", 0, 0));
    processSerial(Serial, "PC");
    processSerial(bluetooth_serial, "BT");
    break;
  case COMMAND_RECEIVED:
    Serial.print(MyMsg("#").createMessage("COMMAND_RECEIVED", action.source, action.ID, action.value));
    switch (action.ID)
    {
    case LED_ID:
      if (action.value > 0)
      {
        Serial.print(MyMsg("#").createMessage("LED_ON", action.source, action.ID, action.value));
        currentState = LED_ON;
      }
      else
      {
        Serial.print(MyMsg("#").createMessage("LED_OFF", action.source, action.ID, action.value));
        currentState = LED_OFF;
      }
      break;
    case SERVO_ID:
      Serial.print(MyMsg("#").createMessage("SERVO_MOVEMENT", action.source, action.ID, action.value));
      currentState = SERVO_MOVEMENT;
      break;
    }
    break;
    // currentState = ERROR;
  case LED_ON:
    
    digitalWrite(ledPin, HIGH);
    Serial.print(MyMsg("#").createMessage("LED_TURNED_ON", action.source, action.ID, action.value));
    currentState = redirectInfo(action.source);
    break;
  case LED_OFF:
    digitalWrite(ledPin, LOW);
    Serial.print(MyMsg("#").createMessage("LED_TURNED_OFF", action.source, action.ID, action.value));
    currentState = redirectInfo(action.source);
    break;
  case SERVO_MOVEMENT:
    servo.write(map(action.value, 0, 180, 750, 2250));
    currentState = redirectInfo(action.source);
    break;
  case SEND_TO_APP:
    bluetooth_serial.print(MyMsg("").createMessage(HARDUINO, SMARTPHONE, action.ID, action.value));
    currentState = IDLE;
    break;
  case SEND_TO_PC:
    Serial.print(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));
    currentState = IDLE;
    break;
  case ERROR:
    Serial.print(MyMsg("#").createMessage("ERROR", "", -1, -1));
    currentState = IDLE;
    break;
  default:
    Serial.print(MyMsg("#").createMessage("ERROR", "def", -2, -2));
    currentState = IDLE;
    break;
  }
}

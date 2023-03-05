#include <Arduino.h>
#include <Servo.h>
#include <MyMsg.h>
Servo servo;
const int ledPin = 2;
const int servoPin = 3;
int debug = HIGH;

void setup()
{
  // Initialization
  pinMode(ledPin, OUTPUT);
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  servo.attach(servoPin);
  servo.write(0);
}

void loop()
{

  if (Serial.available() > 0)
  {

    digitalWrite(13, debug);
    debug = !debug;

    String read = Serial.readStringUntil('|');
    Serial.begin(9600);
    // Serial.println("#read was: " + read);

    Action action = MyMsg(read).interpret();

    if (action.source == SMARTPHONE)
    {
      Serial.print(MyMsg("").createMessage(SMARTPHONE, BACKEND, action.ID, action.value));
    }

    switch (action.ID)
    {
    case LED_ID:
      digitalWrite(ledPin, action.value > 0 ? HIGH : LOW);
      break;
    case SERVO_ID:
      servo.write(action.value);
      break;
    default:
      break;
    }
  }
}

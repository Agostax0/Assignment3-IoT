
#include <WiFi.h>
#include "time.h"
#include <HTTPClient.h>
#define PIR_ID 3
#define LS_ID 4

const char *ssid = "AGOSTAH";
const char *password = "MatteoHS";
const char *serviceURI = "http://192.168.43.150:8080";

const int ll_sensor_pin = 4;
const int pir_sensor_pin = 5;

void setup()
{
  Serial.begin(115200);

  Serial.printf("Connecting to %s ", ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println("# CONNECTED");
  // put your setup code here, to run once:
  pinMode(ll_sensor_pin, INPUT);
  pinMode(pir_sensor_pin, INPUT);
}
int sendData(String address, String command)
{

  HTTPClient http;
  http.begin(address + "/api/data");
  http.addHeader("Content-Type", "application/json");

  String msg = "{" + String("\"") + "command" + String("\"") + ": " + String("\"") + command + String("\"") + "}";

  int retCode = http.POST(msg);
  http.end();

  return retCode;
}
void loop()
{
  // put your main code here, to run repeatedly:

  int retCode = 0;

  if (digitalRead(pir_sensor_pin) == HIGH) // someone is in the room
  {
    Serial.print("#E,B{PIR_ID}[111] ");
    retCode = sendData(serviceURI, "E,B{" + String(PIR_ID) + "}[111]");
    Serial.println(retCode == 200 ? "Success" : "Failure");
  }
  else
  { // none is in the room
    Serial.print("#E,B{PIR_ID}[000]");
    retCode = sendData(serviceURI, "E,B{"+ String(PIR_ID) +"}[000]");
    Serial.println(retCode == 200 ? "Success" : "Failure");
  }

  int light_level = analogRead(ll_sensor_pin);
  Serial.print("#E,B{LS_ID}[+" + String(light_level) + String("] "));
  retCode = sendData(serviceURI, "E,B{" + String(LS_ID) + "}["+String(light_level)+"]");

  Serial.println(retCode == 200 ? "Success" : "Failure");

  delay(30000);
}

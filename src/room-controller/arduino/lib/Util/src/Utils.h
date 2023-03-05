#ifndef __UTILS_
#define __UTILS__
#include<Arduino.h>
#include<string.h>
struct Action{
    String source;
    int ID;
    int value;
};

#define LED_ID 1
#define SERVO_ID 2

const String HARDUINO = "A";
const String BACKEND = "B";
const String SMARTPHONE = "S";


#endif
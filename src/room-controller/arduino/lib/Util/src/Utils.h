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

const String BACKEND = "B";

#endif
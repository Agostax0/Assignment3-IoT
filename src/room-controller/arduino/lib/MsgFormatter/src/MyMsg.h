#ifndef __MYMSG__
#define __MYMSG__
#include "Utils.h"
class MyMsg
{
public:
    MyMsg(String input);
    
    String createMessage(String Source, String Dest, int ID, int value);

    Action interpret();

private:
    String s;
};

#endif
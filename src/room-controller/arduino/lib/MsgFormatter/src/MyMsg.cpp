#include"MyMsg.h"

MyMsg:: MyMsg(String input){
    this->s = input;
};

String MyMsg::createMessage(String D,int ID,int value){
    this->s = "A,"+D+"{"+String(ID)+"}";

    String formatted_value = "";

    if(value<10){
        formatted_value = "00"+String(value);
    }
    else if(value >= 10 && value < 100){
        formatted_value = "0"+String(value);
    }
    else{
        formatted_value = String(value);
    }
    
    this->s += "["+formatted_value+"]";

    return this->s;
}

Action MyMsg::interpret(){
    Action res;

    //Serial.println(this->s);

    res.source = this->s.substring(0,1);
    res.ID = this->s.substring(4,5).toInt();
    res.value = this->s.substring(7,10).toInt();

    return res;
}
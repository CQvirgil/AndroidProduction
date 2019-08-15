package com.virgil.study.cqplayer;

public class EventMsg {
    public final String message;

    private EventMsg(String msg){
        this.message = msg;
    }

    public static EventMsg getInstance(String message){
        return new EventMsg(message);
    }
}

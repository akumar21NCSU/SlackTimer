package com.ak;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kumar on 8/31/16.
 */
public class Message {

    private int id;
    private String sender;
    private String receiver;
    private String message;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static String formatDate(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR)+ "-"+ cal.get(Calendar.MONTH)+"-"+ cal.get(Calendar.DAY_OF_MONTH) + " "+ cal.get(Calendar.HOUR_OF_DAY)+":" + cal.get(Calendar.MINUTE);
    }
}

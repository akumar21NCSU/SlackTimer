package com.ak;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by kumar on 8/31/16.
 */
public class ScheduledTask extends TimerTask {

    private Date now;

    private static final MessageService service = new MessageService();

    public void run(){

        now = new Date();
        List<Message> messages = service.getMessages(Message.formatDate(now));
        SlackRestClient client = new SlackRestClient();
        try {
            for (Message msg : messages) {
                client.sendMessage(msg.getSender(), msg.getReceiver(), msg.getMessage());
            }
        }catch(Exception e){
            System.out.println("Error Sending message. "+ e.getMessage());
        }
    }
}

package com.ak;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by kumar on 8/31/16.
 */
public class ScheduledTask extends TimerTask {

    private Date now;

    private static final MessageService retriever = new MessageService();

    public void run(){

        now = new Date();
        List<Message> messages = retriever.getMessages(Message.formatDate(now));

    }
}

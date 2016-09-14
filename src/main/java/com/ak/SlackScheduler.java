package com.ak;

import java.util.Timer;

/**
 * Created by kumar on 8/31/16.
 */
public class SlackScheduler {

    public static void main(String ...args) throws Exception {

        Timer timer = new Timer();
        ScheduledTask st = new ScheduledTask();
        timer.schedule(st, 0, 60000);
    }

}


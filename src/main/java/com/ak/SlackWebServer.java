package com.ak;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SlackWebServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static String joinMessage(String[] parts){

        String result = "";
        for(int i=2;i< parts.length;i++)
            result = result + parts[i]+"%20";

        return result;
    }


    static class MyHandler implements HttpHandler {

        public void handle(HttpExchange t) throws IOException {

            InputStream is = t.getRequestBody();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            String[] keyValuePair = line.split("&");

            HashMap<String, String> data = new HashMap<String, String>();

            for (String pair : keyValuePair) {

                String[] arr = pair.split("=");
                data.put(arr[0], arr[1]);

            }

            String sender = data.get("user_name");
            String text = data.get("text");

            boolean valid = validateText(text);

            String message = "";

            if (!valid) {
                message = ". Invalid message: " + text + ". Please follow this format: /timedmessage <reciever> <time_in_minutes> <message>";
            } else {
                String[] parts = text.split("\\++");
                message = ". Your message has been saved. It will be delivered to " + parts[0] +" at your specified time.";
                int minutes = Integer.parseInt(parts[1]);
                MessageService ms = new MessageService();
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.MINUTE, minutes);
                ms.insertMessage(sender, parts[0], joinMessage(parts), Message.formatDate(cal.getTime()));
            }

            String response = "{\"text\": \"Hi " + sender + message + "\"}";
            String encoding = "UTF-8";
            t.getResponseHeaders().set("Content-Type", "application/json; charset=" + encoding);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        static public boolean validateTime(String timeString) {

            return true;
        }

        static public boolean validateRecepient(String reciever) {

            return true;
        }

        static private boolean validateText(String text) {
            //text = text.replace("+", " ");
            String[] parts = text.split("\\++");
            if (parts.length < 3)
                return false;

            return validateTime(parts[1]) && validateRecepient(parts[0]);
        }

    }

}

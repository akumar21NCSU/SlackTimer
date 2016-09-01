package com.ak;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MessageService {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/slack_requests";

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "root";

    public List<Message> getMessages(String compareDate) {

        Connection conn = null;
        Statement stmt = null;

        List<Message> messages = new LinkedList<Message>();
        try{
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //Execute query
            System.out.println("Creating statement...");
            String sql;
            sql = "SELECT * FROM requests where delivery_time ='"+ compareDate+"'";
            stmt = conn.createStatement();
            System.out.println(compareDate);

            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                System.out.print("hererere");
                int id  = rs.getInt("id");
                String sender = rs.getString("sender");
                String reciever = rs.getString("reciever");
                String message = rs.getString("message");

                String time = rs.getString("delivery_time");

                Message newMessage = new Message();
                newMessage.setId(id);
                newMessage.setSender(sender);
                newMessage.setReceiver(reciever);
                newMessage.setMessage(message);
                newMessage.setTime(time);

                messages.add(newMessage);

            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return messages;
    }

    public static void main(String[] args) {

        MessageService ex = new MessageService();
        java.util.Date now = new java.util.Date();
        List<Message> result = ex.getMessages(Message.formatDate(now));
        //System.out.println("message "+ result.get(0).getMessage());
    }//end main
}//end class
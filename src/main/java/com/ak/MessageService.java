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

    private Connection makeConnection() {

        Connection conn = null;

        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            System.out.println("Exception in establishing Connection");
        }
        return conn;
    }

    public void insertMessage(String sender, String receiver, String message, String time) {

        Statement stmt = null;
        Connection conn = null;

        try {

            conn = makeConnection();
            //Execute query
            System.out.println("Creating statement...");
            String sql;
            sql = "insert into requests(sender, reciever, message, delivery_time) values('"+sender+"','"+receiver+"','"+message+"','"+time+"')";
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

    }


    public List<Message> getMessages(String compareDate) {

        List<Message> messages = new LinkedList<Message>();
        Statement stmt = null;
        Connection conn = null;

        try {


            conn = makeConnection();
            //Execute query
            System.out.println("Creating statement...");
            String sql;
            sql = "SELECT * FROM requests where delivery_time ='" + compareDate + "'";
            stmt = conn.createStatement();
            System.out.println(compareDate);

            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                System.out.print("hererere");
                int id = rs.getInt("id");
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
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        return messages;
    }

    public static void main(String[] args) {

        MessageService ex = new MessageService();
        java.util.Date now = new java.util.Date();
        ex.insertMessage("akumar21","akash","Wassup","2016-8-31 23:35");
        List<Message> result = ex.getMessages(Message.formatDate(now));
        //System.out.println("message "+ result.get(0).getMessage());
    }//end main
}//end class
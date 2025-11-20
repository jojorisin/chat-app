package se.sprinto.hakan.chatapp.model;

import java.time.LocalDateTime;

public class Message {
    private int id;
    //använder endast userId här, eftersom hela Usern inte behövs
    private int userId;
    private String text;
    private LocalDateTime timestamp;

    public Message(int userId, String text, LocalDateTime timestamp) {
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }


    public String toString() {
        return id + " " + userId + " " + text + " " + timestamp;
    }


    public int getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    //skapade en setId som sparas när man lagrar meddelandet i databasen
   /* public void setId(int id) {
        this.id = id;
    }*/


}


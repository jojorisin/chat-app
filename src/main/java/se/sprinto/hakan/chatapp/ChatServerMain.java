package se.sprinto.hakan.chatapp;

public class ChatServerMain {
    public static void main(String[] args) {
        new ChatServer(5555).start();
    }
} //OBS behöver korrekt lösenord i application.properties innan start

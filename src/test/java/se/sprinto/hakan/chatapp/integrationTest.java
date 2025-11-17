package se.sprinto.hakan.chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sprinto.hakan.chatapp.dao.MessageDAO;
import se.sprinto.hakan.chatapp.dao.UserDAO;
import se.sprinto.hakan.chatapp.dao.databasedao.MessageDatabaseDao;
import se.sprinto.hakan.chatapp.dao.databasedao.UserDatabaseDao;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;
import se.sprinto.hakan.chatapp.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class integrationTest {

    @BeforeEach
    void setup() {

        String sql = "CREATE TABLE users (" +
                "user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(50) NOT NULL" +
                ")";

        String sql2 = "CREATE TABLE messages(" +
                "message_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "user_id INT NOT NULL REFERENCES users(user_id)," +
                "message TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";


        try (Connection test = DatabaseConnector.getInstance().getConnection();
             Statement stmt = test.createStatement()) {
            System.out.println("Using URL: " + test.getMetaData().getURL());

            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println("error connection");
        }

    }

    @Test
    void test() {
        UserDAO userDAO = new UserDatabaseDao();
        MessageDAO messageDAO = new MessageDatabaseDao();

        User user = new User("lisa", "lisa");
        userDAO.register(user);
        Message m1 = new Message(user.getId(), "hej", LocalDateTime.now());
        Message m2 = new Message(user.getId(), "då", LocalDateTime.now());
        messageDAO.saveMessage(m1);
        messageDAO.saveMessage(m2);
        List<Message> userMessages = messageDAO.getMessagesByUserId(user.getId());

        assertTrue(userMessages.size() == 2, "Should be 2 messages");

    }
}

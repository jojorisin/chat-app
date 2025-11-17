package se.sprinto.hakan.chatapp.dao.databasedao;

import se.sprinto.hakan.chatapp.constants.DBConstants;
import se.sprinto.hakan.chatapp.constants.ErrorMessages;
import se.sprinto.hakan.chatapp.dao.MessageDAO;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseDao implements MessageDAO {

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE user_id=?";
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapMessage(rs));

                }
            }
        } catch (SQLException e) {
            System.out.println(ErrorMessages.DATA_ACCESS_ERROR);
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void saveMessage(Message message) {
        String sql = "INSERT INTO messages (user_id, message, created_at) VALUES(?,?,?)";
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, message.getUserId());
            pstmt.setString(2, message.getText());
            pstmt.setTimestamp(3, Timestamp.valueOf(message.getTimestamp()));

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(DBConstants.ROWS_AFFECTED + rowsAffected);
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(ErrorMessages.DATA_ACCESS_ERROR);
            e.printStackTrace();
        }

    }


    private Message mapMessage(ResultSet rs) throws SQLException {
        int messageId = rs.getInt("message_id");
        int userId = rs.getInt("user_id");
        String text = rs.getString("message");
        LocalDateTime timestamp = rs.getTimestamp("created_at").toLocalDateTime();

        return new Message(messageId, userId, text, timestamp);


    }
}










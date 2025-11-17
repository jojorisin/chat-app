package se.sprinto.hakan.chatapp.dao.databasedao;

import se.sprinto.hakan.chatapp.constants.DBConstants;
import se.sprinto.hakan.chatapp.constants.ErrorMessages;
import se.sprinto.hakan.chatapp.dao.UserDAO;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;
import se.sprinto.hakan.chatapp.util.DatabaseConnector;

import java.sql.*;

public class UserDatabaseDao implements UserDAO {

    @Override
    public User login(String username, String password) {
        User user = null;
        String sql = "SELECT u.user_id,u.username, u.password, m.message_id, m.message, m.created_at\n" +
                " FROM users u\n" +
                " LEFT JOIN messages m ON u.user_id=m.user_id WHERE u.username=? AND u.password=?";
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);


            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                user = mapUser(rs);

                do {
                    if (rs.getObject("message_id") != null) {
                        Message message = new Message(rs.getInt("message_id"), rs.getInt("user_id"),
                                rs.getString("message"), rs.getTimestamp("created_at").toLocalDateTime());
                        user.addMessage(message);

                    }


                } while (rs.next());

                return user;


            }
        } catch (SQLException e) {
            System.out.println(ErrorMessages.DATA_ACCESS_ERROR);
            e.printStackTrace();
        }
        return user;


    }


    @Override
    public User register(User user) {
        String sql = "INSERT INTO users (username,password) VALUES(?,?)";
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(DBConstants.ROWS_AFFECTED + rowsAffected);

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(ErrorMessages.DATA_ACCESS_ERROR);
            e.printStackTrace();
        }
        return user;
    }


    private User mapUser(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");

        return new User(userId, username, password);

    }

}

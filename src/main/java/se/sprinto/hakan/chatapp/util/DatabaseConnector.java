package se.sprinto.hakan.chatapp.util;

import se.sprinto.hakan.chatapp.constants.DBConstants;
import se.sprinto.hakan.chatapp.constants.ErrorMessages;
import se.sprinto.hakan.chatapp.exception.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private final Properties properties;
    private static DatabaseConnector instance;

    private DatabaseConnector() {
        this.properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(DBConstants.DB_RESOURCE)) {
            if (inputStream == null) {
                throw new DataAccessException(ErrorMessages.DATA_FILE_ERROR + DBConstants.DB_RESOURCE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new DataAccessException(ErrorMessages.PROPERTIES_LOAD_ERROR, e);

        }
    }


    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;

    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getProperty(DBConstants.DB_URL), getProperty(DBConstants.DB_USER), getProperty(DBConstants.DB_PASS));

    }
}

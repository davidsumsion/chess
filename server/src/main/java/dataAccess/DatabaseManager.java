package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

            var userTableStatment = "CREATE TABLE IF NOT EXISTS " + databaseName +  ".UserTable (" +
                    "username VARCHAR(32) not null primary key," +
                    "    hashedPassword VARCHAR(255) not null," +
                    "    email VARCHAR(32) not null," +
                    "    authToken VARCHAR(50)" +
                    ");";

            try (var preparedStatement = conn.prepareStatement(userTableStatment)) {
                preparedStatement.executeUpdate();
            }

            var authTableStatment = "CREATE TABLE IF NOT EXISTS " + databaseName +".AuthDataTable (" +
                    "authToken VARCHAR(50) not null primary key," +
                    "    username VARCHAR(32) not null" +
                    ");";

            try (var preparedStatement = conn.prepareStatement(authTableStatment)) {
                preparedStatement.executeUpdate();
            }

            var gameDataTableStatement = "CREATE TABLE IF NOT EXISTS "+ databaseName + ".GameDataTable (" +
                    "    gameID INT not null primary key auto_increment," +
                    "    whiteUsername VARCHAR(32)," +
                    "    blackUsername VARCHAR(32)," +
                    "    gameName VARCHAR(32)," +
                    "    chessGame JSON," +
                    "    foreign key(whiteUsername) references UserTable(username)," +
                    "    foreign key(blackUsername) references UserTable(username)" +
                    ");";

            try (var preparedStatement = conn.prepareStatement(gameDataTableStatement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Cannot Create DB or tables");
        }
    }



    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}

package services;

import dataAccess.*;
import models.GameData;
import models.UserData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.*;
import java.util.Properties;

public class DeleteAllService {

    public DeleteAllService() {}

    public void deleteAll(){
        try (Connection conn = DatabaseManager.getConnection()){
            String databaseName = null;
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            var userTableStatement = "DROP DATABASE IF EXISTS " + databaseName + ";";
            try (PreparedStatement stmt = conn.prepareStatement(userTableStatement)) {
                stmt.executeUpdate();
            }
            DatabaseManager.createDatabase();

        } catch (SQLException | DataAccessException e) {
            System.out.println("Failed to connect to server");
        }

    }
}

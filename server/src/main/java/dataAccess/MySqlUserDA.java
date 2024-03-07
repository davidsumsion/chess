package dataAccess;

import java.sql.Connection;

public class MySqlUserDA {
    public MySqlUserDA() {};







//    public void createSession(Connection connection, AuthData authData) throws DataAccessException {
//        String sql = "insert into authDataTable (username, authToken) values (?, ?)";
//
//        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, authData.getUsername());
//            stmt.setString(2, authData.getAuthToken());
//
//            if(stmt.executeUpdate() == 1) {
//                try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                    generatedKeys.next();
//                    int id = generatedKeys.getInt(1); // ID of the inserted book
//                    authData.setId(id);
//                }
//
//                System.out.println("Inserted User into authDataTable " + authData.toString());
//            } else {
//                System.out.println("Failed to insert User into authDataTable");
//            }
//            } catch (SQLException e) {
//                throw new DataAccessException(e.getMessage());
//        }
//    }
}

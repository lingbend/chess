package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserAccess implements DataAccess{

    public SQLUserAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        UserData userData = (UserData) obj;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT users (username, email, password) VALUES(?, ?, ?)";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getEmail());
            preparedStatement.setString(3, userData.getPassword());

            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }


    };
    public boolean find(Object index) throws DataAccessException {
        String username = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username FROM users WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, username);

            var userRecord = preparedStatement.executeQuery();

            while(userRecord.next()) {
                if (userRecord.getString("username").equals(username)) {
                    return true;
                }
            }

            return false;
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }


    };

    public boolean update(Object index) throws DataAccessException {
        return false;
    };

    public boolean delete(Object index) throws DataAccessException {
        return false;
    };

    public Object read(String index) throws DataAccessException {
        String username = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, email, password FROM users WHERE username=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, username);

            var userRecord = preparedStatement.executeQuery();

            if (userRecord.next()) {
                return new UserData(username, userRecord.getString("password"),
                        userRecord.getString("email"));
            }
            else {
                return false;
            }
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }

    };
    public boolean deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE users";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;

        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    };
}

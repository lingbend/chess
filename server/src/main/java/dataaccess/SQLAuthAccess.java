package dataaccess;

import model.AuthData;
import java.sql.*;

public class SQLAuthAccess implements DataAccess, AuthAccessInter {

    public SQLAuthAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        if (obj == null) {
            return false;
        }
        var authData = (AuthData) obj;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authData.getUsername());
            preparedStatement.setString(2, authData.getAuthToken());

            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    };

    public boolean find(Object index) throws DataAccessException {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            var authRecord = preparedStatement.executeQuery();

            if (authRecord.next() && authRecord.getString("authToken").equals(authToken)) {
                return true;
            }
            else {
                return false;
            }

        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }



    };

    public boolean delete(Object index) throws DataAccessException {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }

    };

    public Object read(String index) throws DataAccessException {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, authToken FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            var readResponse = preparedStatement.executeQuery();

            if (readResponse.next() && readResponse.getString("authToken").equals(authToken)
            && readResponse.getString("username") != null) {
                return new AuthData(readResponse.getString("username"), authToken);
            }
            else {
                return null;
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }

    };

    public boolean deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE auth";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    };
}

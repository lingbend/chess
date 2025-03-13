package dataaccess;

import model.AuthData;
import java.sql.*;

public class SQLAuthAccess implements DataAccess{

    public SQLAuthAccess(){}

    public boolean create(Object obj) {
        var authData = (AuthData) obj;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authData.getUsername());
            preparedStatement.setString(2, authData.getAuthToken());

            preparedStatement.executeUpdate();

            return true;
        }
        catch (Exception ex) {
            return false;
        }
    };

    public boolean find(Object index) {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            var authRecord = preparedStatement.executeQuery();

            authRecord.next();

            if (authRecord.getString("authToken").equals(authToken)) {
                return true;
            }
            else {
                return false;
            }

        }
        catch (Exception ex) {
            return false;
        }


    };

    public boolean update(Object index){return false;};

    public boolean delete(Object index) {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            preparedStatement.executeUpdate();

            return true;
        }
        catch (Exception ex) {
            return false;
        }

    };

    public Object read(String index) {
        String authToken = (String) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, authToken FROM auth WHERE authToken=?";

            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);

            var readResponse = preparedStatement.executeQuery();

            readResponse.next();

            if (readResponse.getString("authToken").equals(authToken)
            && readResponse.getString("username") != null) {
                return new AuthData(readResponse.getString("username"), authToken);
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            return null;
        }

    };

    public boolean deleteAll() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE auth";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;
        }
        catch (Exception ex) {
            return false;
        }
    };
}

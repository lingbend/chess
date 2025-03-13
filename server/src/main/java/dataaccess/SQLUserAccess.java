package dataaccess;

import model.UserData;

public class SQLUserAccess implements DataAccess{

    public SQLUserAccess(){}

    public boolean create(Object obj){
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
        catch (Exception ex) {
            return false;
        }


    };
    public boolean find(Object index){
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

        catch (Exception ex) {
            return false;
        }


    };

    public boolean update(Object index){
        return false;
    };

    public boolean delete(Object index){
        return false;
    };

    public Object read(String index){
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

        catch (Exception ex) {
            return null;
        }

    };
    public boolean deleteAll(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE users";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;

        }

        catch (Exception ex) {
            return false;
        }
    };
}

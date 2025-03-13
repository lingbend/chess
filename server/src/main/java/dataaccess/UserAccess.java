package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class UserAccess implements DataAccess {

    public UserAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        var userData = (UserData) obj;
        if (DB.users.add(userData)) {
            return true;
        }
        else {
            return false;
        }
    };
    public boolean find(Object index) throws DataAccessException {
        String username = (String) index;
        for (var i : DB.users) {
            if (i.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    };

    public Object read(String index) throws DataAccessException {
        String username = (String) index;
        for (var i : DB.users) {
            if (i.getUsername().equals(username)) {
                return i;
            }
        }
        return null;
    }

    public boolean deleteAll() throws DataAccessException {
        DB.users = new ArrayList<>();
        return true;
    }
}

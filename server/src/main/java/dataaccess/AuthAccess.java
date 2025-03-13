package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthAccess implements DataAccess, AuthAccessInter {

    public AuthAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        var authData = (AuthData) obj;
        if (DB.auth.add(authData)) {
            return true;
        }
        else {
            return false;
        }
    }

    //To implement:
    public boolean find(Object index) throws DataAccessException {
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return true;
            }
        }
        return false;
    };
    public boolean delete(Object index) throws DataAccessException {
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return DB.auth.remove(i);
            }
        }
        return false;
    };
    public Object read(String index) throws DataAccessException {
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return i;
            }
        }
        return null;
    };
    public boolean deleteAll() throws DataAccessException {
        DB.auth = new ArrayList<>();
        return true;
    }

}

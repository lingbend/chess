package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthAccess implements DataAccess {

    public AuthAccess(){}

    public boolean create(Object obj){
        var authData = (AuthData) obj;
        if (DB.auth.add(authData)) {
            return true;
        }
        else {
            return false;
        }
    }

    //To implement:
    public boolean find(Object index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return true;
            }
        }
        return false;
    };
    public boolean update(Object index){
        return false;
    };
    public boolean delete(Object index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return DB.auth.remove(i);
            }
        }
        return false;
    };
    public Object read(String index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.getAuthToken().equals(auth)) {
                return i;
            }
        }
        return null;
    };
    public boolean deleteAll(){
        DB.auth = new ArrayList<>();
        return true;
    }

}

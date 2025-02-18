package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthAccess implements dataAccess {

    public AuthAccess(){}

    public boolean Create(Object obj){
        var authData = (AuthData) obj;
        if (DB.auth.add(authData)) {
            return true;
        }
        else {
            return false;
        }
    }

    //To implement:
    public boolean Find(Object index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.GetAuthToken().equals(auth)) {
                return true;
            }
        }
        return false;
    };
    public boolean Update(Object index){
        return false;
    };
    public boolean Delete(Object index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.GetAuthToken().equals(auth)) {
                return DB.auth.remove(i);
            }
        }
        return false;
    };
    public boolean Match(Object index){
        return false;
    };
    public Object Read(String index){
        String auth = (String) index;
        for (var i : DB.auth) {
            if (i.GetAuthToken().equals(auth)) {
                return i;
            }
        }
        return null;
    };
    public boolean DeleteAll(){
        DB.auth = new ArrayList<>();
        return true;
    }

}

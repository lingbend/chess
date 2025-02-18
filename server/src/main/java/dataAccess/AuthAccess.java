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
        return false;
    };
    public boolean Update(String index){
        return false;
    };
    public boolean Delete(Object index){
        return false;
    };
    public boolean Match(Object index){
        return false;
    };
    public Object Read(String index){
        return null;
    };
    public boolean DeleteAll(){
        DB.auth = new ArrayList<>();
        return true;
    }

}

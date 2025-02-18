package dataAccess;

import model.AuthData;

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
    public boolean DeleteAll(Class type){
        return false;
    };

}

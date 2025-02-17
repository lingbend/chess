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
    public boolean Find(Object index){};
    public boolean Update(String index){};
    public boolean Delete(Object index){};
    public boolean Match(Object index){};
    public Object Read(String index){};
    public boolean DeleteAll(Class type){};

}

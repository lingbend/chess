package dataAccess;

import model.UserData;

import java.util.ArrayList;

public class UserAccess implements dataAccess {

    public UserAccess(){}

    public boolean Create(Object obj){
        var userData = (UserData) obj;
        if (DB.users.add(userData)) {
            return true;
        }
        else {
            return false;
        }
    };
    public boolean Find(Object index){
        String username = (String) index;
        for (var i : DB.users) {
            if (i.GetUsername().equals(username)) {
                return true;
            }
        }
        return false;
    };

    //To implement:
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
        DB.users = new ArrayList<>();
        return true;
    }
}

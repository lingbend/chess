package DataAccess;

import model.UserData;

import java.util.ArrayList;

public class UserAccess implements DataAccess {

    public UserAccess(){}

    public boolean create(Object obj){
        var userData = (UserData) obj;
        if (DB.users.add(userData)) {
            return true;
        }
        else {
            return false;
        }
    };
    public boolean find(Object index){
        String username = (String) index;
        for (var i : DB.users) {
            if (i.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    };

    //To implement:
    public boolean update(Object index){
        return false;
    };
    public boolean delete(Object index){
        return false;
    };

    public Object read(String index){
        String username = (String) index;
        for (var i : DB.users) {
            if (i.getUsername().equals(username)) {
                return i;
            }
        }
        return null;
    }

    public boolean deleteAll(){
        DB.users = new ArrayList<>();
        return true;
    }
}

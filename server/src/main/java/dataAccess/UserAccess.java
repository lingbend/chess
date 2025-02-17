package dataAccess;

import model.UserData;

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
    public boolean Update(String index){};
    public boolean Delete(Object index){};
    public boolean Match(Object index){};
    public Object Read(String index){};
    public boolean DeleteAll(Class type){};

}

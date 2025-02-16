package model;

import java.lang.String;

public class UserData {
    String username = null;
    String password = null;
    String email = null;

    public UserData(){}

    public void SetUsername(String newUsername) {
        username = newUsername;
    }

    public String GetUsername(){
        return username;
    }

    public void SetPassword(String newPass) {
        password = newPass;
    }

    public String GetPassword(){
        return password;
    }

    public void SetEmail(String newEmail) {
        email = newEmail;
    }

    public String GetEmail() {
        return email;
    }

}

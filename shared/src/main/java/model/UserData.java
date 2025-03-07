package model;

import java.lang.String;

public class UserData {
    String username = null;
    String password = null;
    String email = null;

    public UserData(String user, String pass, String mail){
        username = user;
        password = pass;
        email = mail;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String newPass) {
        password = newPass;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public String getEmail() {
        return email;
    }

}

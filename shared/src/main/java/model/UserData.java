package model;

import java.lang.String;
import java.util.Objects;

public class UserData {
    String username = null;
    String password = null;
    String email = null;

    public UserData(String user, String pass, String mail){
        username = user;
        password = pass;
        email = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) && Objects.equals(password, userData.password) && Objects.equals(email, userData.email);
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

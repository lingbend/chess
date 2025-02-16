package model;

import java.lang.String;

public class AuthData {

    String authToken = null;
    String username = null;

    public AuthData(){}

    public void SetAuthToken(String newToken) {
        authToken = newToken;
    }

    public String GetAuthToken() {
        return authToken;
    }

    public void SetUsername(String name) {
        username = name;
    }

    public String GetUsername() {
        return username;
    }

}

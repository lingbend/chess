package service;

import java.lang.String;
import java.util.Map;
import java.lang.Integer;

public class RequestObj implements ServiceObj{
    String username;
    String password;
    String email;
    String authToken;
    String gameName;
    String color;
    String gameID;

    public RequestObj(Map map) {
        if (map != null) {
            username = value(map.get("username"));
            password = value(map.get("password"));
            email = value(map.get("email"));
            authToken = value(map.get("authToken"));
            gameName = value(map.get("gameName"));
            color = value(map.get("playerColor"));
            Object tempID = map.get("gameID");
            if (tempID != null) {
                gameID = value((int) (double) tempID);
            }
        }
    }

    private String value(Object obj) {
        if (obj == null) {
            return null;
        }
        else {
            return String.valueOf(obj);
        }
    }

    public void SetUsername(String newUsername) {
        username = newUsername;
    }

    public String GetUsername() {
        return username;
    }

    public void SetPassword(String newPassword) {
        password = newPassword;
    }

    public String GetPassword() {
        return password;
    }

    public void SetEmail(String newEmail) {
        email = newEmail;
    }

    public String GetEmail() {
        return email;
    }

    public void SetAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }

    public String GetAuthToken(){
        return authToken;
    }

    public void SetGameName(String newName) {
        gameName = newName;
    }

    public String GetGameName() {
        return gameName;
    }

    public void SetColor(String newColor) {
        color = newColor;
    }

    public String GetColor(){
        return color;
    }

    public void SetGameID(String newID) {
        gameID = newID;
    }

    public String GetGameID(){
        return gameID;
    }
}

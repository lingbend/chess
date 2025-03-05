package service;

import java.lang.String;
import java.util.Map;

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
            username = String.valueOf(map.get("username"));
            password = String.valueOf(map.get("password"));
            email = String.valueOf(map.get("email"));
            authToken = String.valueOf(map.get("authToken"));
            gameName = String.valueOf(map.get("gameName"));
            color = String.valueOf(map.get("playerColor"));
            gameID = String.valueOf(map.get("gameID"));
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

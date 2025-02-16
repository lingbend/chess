package service;

import java.lang.String;
import java.util.Map;

public class RequestObj implements ServiceObj{
    String username = null;
    String password = null;
    String email = null;
    String authToken = null;
    String gameName = null;
    String color = null;
    String gameID = null;

    public RequestObj(Map map) {
        username = (String) map.get("username");
        password = (String) map.get("password");
        email = (String) map.get("email");
        authToken = (String) map.get("authToken");
        gameName = (String) map.get("gameName");
        color = (String) map.get("color");
        gameID = (String) map.get("gameID");
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

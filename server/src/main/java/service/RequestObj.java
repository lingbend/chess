package service;

import java.lang.String;
import java.util.Map;
import java.util.function.ToDoubleFunction;

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
            String tempID = value(map.get("gameID"));
            if (tempID != null) {
                gameID = value(Double.valueOf(tempID).intValue());
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

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }

    public String getAuthToken(){
        return authToken;
    }

    public void setGameName(String newName) {
        gameName = newName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setColor(String newColor) {
        color = newColor;
    }

    public String getColor(){
        return color;
    }

    public void setGameID(String newID) {
        gameID = newID;
    }

    public String getGameID(){
        return gameID;
    }
}

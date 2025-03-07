package service;

import model.GameData;

import java.lang.String;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ResultObj implements ServiceObj{
    String code;
    String message;
    String gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    String username;
    String authToken;
    ArrayList<TreeMap> games;

    public ResultObj(Map map){
        code = (String) map.get("code");
        message = (String) map.get("message");
        gameID = (String) map.get("gameID");
        whiteUsername = (String) map.get("whiteUsername");
        blackUsername = (String) map.get("blackUsername");
        gameName = (String) map.get("gameName");
        username = (String) map.get("username");
        authToken = (String) map.get("authToken");
        var tempGames = (ArrayList<GameData>) map.get("games");
        if (tempGames != null) {
            games = new ArrayList<TreeMap>();
            for (var i : tempGames) {
                games.add(i.getMap());
            }
        }
    }


    public void setCode(String newCode) {
        code = newCode;
    }

    public String getCode(){
        return code;
    }

    public void setMessage(String newMessage){
        message = newMessage;
    }

    public String getMessage(){
        return message;
    }

    public void setWhiteUsername(String newUser){
        whiteUsername = newUser;
    }

    public String getWhiteUsername(){
        return whiteUsername;
    }

    public void setBlackUsername(String newUser){
        blackUsername = newUser;
    }

    public String getBlackUsername(){
        return blackUsername;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setGameID(String newID) {
        gameID = newID;
    }

    public String getGameID(){
        return gameID;
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
}

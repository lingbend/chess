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
                games.add(i.GetMap());
            }
        }
    }


    public void SetCode(String newCode) {
        code = newCode;
    }

    public String GetCode(){
        return code;
    }

    public void SetMessage(String newMessage){
        message = newMessage;
    }

    public String GetMessage(){
        return message;
    }

    public void SetWhiteUsername(String newUser){
        whiteUsername = newUser;
    }

    public String GetWhiteUsername(){
        return whiteUsername;
    }

    public void SetBlackUsername(String newUser){
        blackUsername = newUser;
    }

    public String GetBlackUsername(){
        return blackUsername;
    }

    public void SetUsername(String newUsername) {
        username = newUsername;
    }

    public String GetUsername() {
        return username;
    }

    public void SetGameID(String newID) {
        gameID = newID;
    }

    public String GetGameID(){
        return gameID;
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
}

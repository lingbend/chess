package model;

import java.lang.String;
import chess.ChessGame;
import java.util.TreeMap;

public class GameData {

    int gameID = 0;
    String whiteUsername = null;
    String blackUsername = null;
    String gameName = null;
    ChessGame game = null;

    public GameData(int id, String name){
        gameID = id;
        gameName = name;
        game = new ChessGame();
    }

    public GameData(int id, String name, ChessGame inputGame) {
        gameID = id;
        gameName = name;
        game = inputGame;
    }

    public void setGameID(int num) {
        gameID = num;
    }

    public int getGameID() {
        return gameID;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setBlackUsername(String username) {
        blackUsername = username;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setGameName(String name) {
        gameName = name;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGame(ChessGame newGame) {
        game = newGame;
    }

    public ChessGame getGame() {
        return game;
    }

    public TreeMap getMap() {
        var map = new TreeMap();
        map.put("gameID", gameID);
        map.put("gameName", gameName);
        map.put("whiteUsername", whiteUsername);
        map.put("blackUsername", blackUsername);
        return map;
    }

}

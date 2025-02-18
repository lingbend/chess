package model;

import java.lang.String;
import chess.ChessGame;

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

    public void SetGameID(int num) {
        gameID = num;
    }

    public int GetGameID() {
        return gameID;
    }

    public void SetWhiteUsername(String username) {
        whiteUsername = username;
    }

    public String GetWhiteUsername() {
        return whiteUsername;
    }

    public void SetBlackUsername(String username) {
        blackUsername = username;
    }

    public String GetBlackUsername() {
        return blackUsername;
    }

    public void SetGameName(String name) {
        gameName = name;
    }

    public String GetGameName() {
        return gameName;
    }

    public void SetGame(ChessGame newGame) {
        game = newGame;
    }

    public ChessGame GetGame() {
        return game;
    }

}

package ui;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class ServerFacadeLocal implements ServerFacadeInterface{

    State currentState;
    String username;
    String authToken;
    String currentGameID;
    ArrayList<GameData> existingGames;


    public ServerFacadeLocal() {
        currentState = State.LoggedOut;
        authToken = "";
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
    }

    public static void main(String[] args) {

    }


    public void run() {

    }

    public String getHelp() {
        return "";
    }

    public void quit() {

    }

    public String login(String username, String password) {
        return "";
    }

    public String register(String username, String password, String email) {
        return "";
    }

    public boolean logout() {
        return false;
    }

    public String createGame(String gameName) {
        return "";
    }

    public ArrayList<GameData> listGames() {
        return null;
    }

    public String playGame(int gameNumber, ChessGame.TeamColor color) {
        return "";
    }

    public boolean observeGame(int gameNumber) {
        return false;
    }
}

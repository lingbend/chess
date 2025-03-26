package ui;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface ServerFacadeInterface {
    void run() throws Exception;
    String getHelp();
    void quit();
    void login(String username, String password) throws Exception;
    String register(String username, String password, String email);
    boolean logout();
    String createGame(String gameName);
    ArrayList<GameData> listGames();
    String playGame(int gameNumber, ChessGame.TeamColor color);
    boolean observeGame(int gameNumber);

    enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

}

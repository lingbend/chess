package ui;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface ServerFacadeInterface {
    void run();
    String getHelp();
    void quit();
    String login(String username, String password);
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

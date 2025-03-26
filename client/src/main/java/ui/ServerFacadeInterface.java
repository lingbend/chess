package ui;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface ServerFacadeInterface {
    void run();
    String getHelp(State state);
    void quit(State state);
    String login(State state, String username, String password);
    String register(State state, String username, String password, String email);
    boolean logout(State state, String authToken);
    String createGame(State state, String authToken, String gameName);
    ArrayList<GameData> listGames(State state, String authToken);
    String playGame(State state, String authToken, int gameNumber, ChessGame.TeamColor color);
    boolean observeGame(State state, String authToken, int gameNumber);

    enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

}

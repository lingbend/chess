package ui;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface ServerFacadeInterface {
    void run();
    String getHelp(Enum state);
    void quit(Enum state);
    String login(Enum state, String username, String password);
    String register(Enum state, String username, String password, String email);
    boolean logout(Enum state, String authToken);
    String createGame(Enum state, String authToken, String gameName);
    ArrayList<GameData> listGames(Enum state, String authToken);
    String playGame(Enum state, String authToken, int gameNumber, ChessGame.TeamColor color);
    boolean observeGame(Enum state, String authToken, int gameNumber);

    enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

}

package ui;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface ServerFacadeInterface {
    void run(int port) throws Exception;
    String getHelp() throws Exception;
    void login(String username, String password) throws Exception;
    void register(String username, String password, String email) throws Exception;
    void logout() throws Exception;
    String createGame(String gameName) throws Exception;
    void listGames() throws Exception;
    void playGame(int gameNumber, ChessGame.TeamColor color) throws Exception;
    void observeGame(int gameNumber) throws Exception;

    enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

}

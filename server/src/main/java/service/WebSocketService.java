package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthAccess;
import dataaccess.SQLGameAccess;
import handler.Handler;
import handler.WebSocketHandler;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import spark.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

public class WebSocketService {

    public WebSocketService(){}

    public String run(UserGameCommand request, Session session, TreeMap<String, ArrayList<Session>> liveGames)
            throws DataAccessException {
        UserGameCommand.CommandType command = request.commandType;
        String sessionID = session.id();
        var authAccess = new SQLAuthAccess();
        var gameAccess = new SQLGameAccess();



        if (command == UserGameCommand.CommandType.CONNECT) {
            if (liveGames.get(request.gameID).isEmpty()) {
                liveGames.put(request.gameID, new ArrayList<Session>());
            }
            liveGames.get(request.gameID).add(session);
        }
        else if (command == UserGameCommand.CommandType.MAKE_MOVE) {

        }
        else if (command == UserGameCommand.CommandType.LEAVE) {

        }
        else if (command == UserGameCommand.CommandType.RESIGN) {

        }

        if (request.getGameID() == null || request.getAuthToken() == null) {
            throw new DataAccessException("bad request");
        }

        if (!authAccess.find(request.getAuthToken())) {
            throw new DataAccessException("unauthorized");
        }

        if (!gameAccess.find(request.getGameID())) {
            throw new DataAccessException("bad request");
        }


        var game = (GameData) gameAccess.read(request.getGameID());
        var auth = (AuthData) authAccess.read(request.getAuthToken());
        if ((request.getColor().equals("WHITE") && game.getWhiteUsername() != null
                && !game.getWhiteUsername().equals(auth.getUsername())) || (request.getColor().equals("BLACK")
                && game.getBlackUsername() != null && !game.getBlackUsername().equals(auth.getUsername()))) {
            throw new DataAccessException("already taken");
        }

        if (request.getColor().equals("WHITE")) {
            game.setWhiteUsername(auth.getUsername());
        }
        else {
            game.setBlackUsername(auth.getUsername());
        }
        if (!gameAccess.update(game)) {
            throw new DataAccessException("unable to alter game");
        };

        var result = new ResultObj(Map.of( "code", "200"));
        return handler.serialize(result);
    return null;
    }

    public void registerHandler(WebSocketHandler handler) {

    };


    private void sendMessage(TreeMap<String, ArrayList<Session>> liveGames) {

    }




}

package service;

import dataaccess.DataAccessException;
import dataaccess.*;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.*;

import java.util.Map;


public class JoinGameService implements Service{

    JsonHandler handler = null;

    public JoinGameService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new SQLAuthAccess();
        var gameAccess = new SQLGameAccess();
        if (request.getColor() == null || request.getGameID() == null
                || request.getAuthToken() == null || (!request.getColor().equals("WHITE")
                && !request.getColor().equals("BLACK"))) {
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
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

package service;

import dataaccess.AuthAccess;
import dataaccess.DataAccessException;
import dataaccess.GameAccess;
import handler.Handler;
import handler.JsonHandler;

import java.util.Map;


public class FindGamesService implements Service{

    JsonHandler handler = null;

    public FindGamesService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var gameAccess = new GameAccess();
        if (!authAccess.find(request.getAuthToken())) {
            throw new DataAccessException("unauthorized");
        }
        var games = gameAccess.findAll();
        var result = new ResultObj(Map.of("games", games, "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

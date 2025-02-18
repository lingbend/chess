package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;

import java.util.Map;


public class FindGamesService implements Service{

    JsonHandler handler = null;

    public FindGamesService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var gameAccess = new GameAccess();
        if (request.GetUsername() == null || request.GetPassword() == null
                || request.GetEmail() == null || request.GetAuthToken() == null) {
            throw new DataAccessException("bad request");
        }
        if (!authAccess.Find(request.GetAuthToken())) {
            throw new DataAccessException("unauthorized");
        }
        var games = gameAccess.FindAll();
        var result = new ResultObj(Map.of("games", games, "code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

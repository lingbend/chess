package service;

import DataAccess.AuthAccess;
import DataAccess.DataAccessException;
import DataAccess.GameAccess;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.*;

import java.util.Map;


public class MakeGameService implements Service{

    JsonHandler handler = null;

    public MakeGameService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var gameAccess = new GameAccess();
        if (request.getGameName() == null || request.getAuthToken() == null) {
            throw new DataAccessException("bad request");
        }
        if (!authAccess.find(request.getAuthToken())) {
            throw new DataAccessException("unauthorized");
        }

        String gameID = AuthData.makeAuthToken();
        if (!gameAccess.create(new GameData(Integer.parseInt(gameID), request.getGameName()))) {
            throw new DataAccessException("unable to create new game");
        }

        var result = new ResultObj(Map.of("gameID", gameID, "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.GameAccess;
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
        if (request.GetGameName() == null || request.GetAuthToken() == null) {
            throw new DataAccessException("bad request");
        }
        if (!authAccess.Find(request.GetAuthToken())) {
            throw new DataAccessException("unauthorized");
        }

        String gameID = AuthData.makeAuthToken();
        if (!gameAccess.Create(new GameData(Integer.valueOf(gameID), request.GetGameName()))) {
            throw new DataAccessException("unable to create new game");
        }

        var result = new ResultObj(Map.of("gameID", gameID, "code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

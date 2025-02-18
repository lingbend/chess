package service;

import dataAccess.*;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;

import java.util.Map;


public class ClearService implements Service{

    JsonHandler handler = null;

    public ClearService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        var gameAccess = new GameAccess();
        if (request.GetUsername() == null || request.GetPassword() == null || request.GetEmail() == null) {
            throw new DataAccessException("bad request");
        }
        if (!userAccess.DeleteAll()) {
            throw new DataAccessException("already taken");
        }
        if (!gameAccess.DeleteAll()) {
            throw new DataAccessException("unable to create user");
        }
        if (!authAccess.DeleteAll()) {
            throw new DataAccessException("unable to store authToken");
        }

        var result = new ResultObj(Map.of("code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

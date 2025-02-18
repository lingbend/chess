package service;

import dataAccess.*;
import handler.Handler;
import handler.JsonHandler;
import java.util.Map;


public class ClearService implements Service{

    JsonHandler handler = null;

    public ClearService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        var gameAccess = new GameAccess();
        if (!userAccess.DeleteAll()) {
            throw new DataAccessException("unable to delete user data");
        }
        if (!gameAccess.DeleteAll()) {
            throw new DataAccessException("unable to delete games");
        }
        if (!authAccess.DeleteAll()) {
            throw new DataAccessException("unable to delete auth tokens");
        }

        var result = new ResultObj(Map.of("code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

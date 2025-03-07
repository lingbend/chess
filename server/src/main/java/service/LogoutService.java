package service;

import dataaccess.AuthAccess;
import dataaccess.DataAccessException;
import handler.Handler;
import handler.JsonHandler;

import java.util.Map;


public class LogoutService implements Service{

    JsonHandler handler = null;

    public LogoutService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        if (!authAccess.find(request.getAuthToken()) || request.getAuthToken() == null) {
            throw new DataAccessException("unauthorized");
        }

        if (!authAccess.delete(request.getAuthToken())) {
            throw new DataAccessException("unable to delete authToken");
        }

        var result = new ResultObj(Map.of(  "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

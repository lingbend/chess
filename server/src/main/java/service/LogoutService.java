package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;

import java.util.Map;


public class LogoutService implements Service{

    JsonHandler handler = null;

    public LogoutService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        if (request.GetAuthToken() == null) {
            throw new DataAccessException("bad request");
        }
        if (!authAccess.Find(request.GetAuthToken())) {
            throw new DataAccessException("unauthorized");
        }
        // add authorization token generator call here
        String token = "1234";
        if (!authAccess.Delete(request.GetAuthToken())) {
            throw new DataAccessException("unable to delete authToken");
        }

        var result = new ResultObj(Map.of(  "code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

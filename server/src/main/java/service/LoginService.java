package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;

import java.util.Map;


public class LoginService implements Service{

    JsonHandler handler = null;

    public LoginService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        if (request.GetUsername() == null || request.GetPassword() == null) {
            throw new DataAccessException("bad request");
        }
        if (!userAccess.Find(request.GetUsername())) {
            throw new DataAccessException("unauthorized");
        }
        var user = (UserData) userAccess.Read(request.GetUsername());
        if (!user.GetPassword().equals(request.GetPassword())) {
            throw new DataAccessException("unauthorized");
        }
        // add authorization token generator call here
        String token = "1234";
        if (!authAccess.Create(new AuthData(request.GetUsername(), token))) {
            throw new DataAccessException("unable to store authToken");
        }

        var result = new ResultObj(Map.of( "authToken", token, "code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

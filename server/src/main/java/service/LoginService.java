package service;

import dataaccess.AuthAccess;
import dataaccess.DataAccessException;
import dataaccess.UserAccess;
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
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new DataAccessException("bad request");
        }
        if (!userAccess.find(request.getUsername())) {
            throw new DataAccessException("unauthorized");
        }
        var user = (UserData) userAccess.read(request.getUsername());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new DataAccessException("unauthorized");
        }
        String token = AuthData.makeAuthToken();
        if (!authAccess.create(new AuthData(request.getUsername(), token))) {
            throw new DataAccessException("unable to store authToken");
        }

        var result = new ResultObj(Map.of("username", user.getUsername(),"authToken", token, "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

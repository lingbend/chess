package service;

import model.*;
import dataaccess.*;
import handler.*;

import java.util.Map;


public class RegisterService implements Service{

    JsonHandler handler = null;

    public RegisterService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new SQLAuthAccess();
        var userAccess = new SQLUserAccess();
        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            throw new DataAccessException("bad request");
        }
        if (userAccess.find(request.getUsername())) {
            throw new DataAccessException("already taken");
        }
        if (!userAccess.create(new UserData(request.getUsername(),
                request.getPassword(), request.getEmail()))) {
            throw new DataAccessException("unable to create user");
        }
        String token = AuthData.makeAuthToken();
        if (!authAccess.create(new AuthData(request.getUsername(), token))) {
            throw new DataAccessException("unable to store authToken");
        }

        var result = new ResultObj(Map.of("username",
                request.getUsername(), "authToken", token, "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

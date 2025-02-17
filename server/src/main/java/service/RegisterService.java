package service;

import model.*;
import dataAccess.*;
import handler.*;

import java.util.Map;


public class RegisterService implements Service{

    JsonHandler handler = null;

    public RegisterService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        if (userAccess.Find(request.GetUsername())) {
            throw new DataAccessException("already taken");
        }
        if (!userAccess.Create(new UserData(request.GetUsername(),
                request.GetPassword(), request.GetEmail()))) {
            throw new DataAccessException("unable to create user");
        }
        // add authorization token generator call here
        String token = "1234";
        if (!authAccess.Create(new AuthData(request.GetUsername(), token))) {
            throw new DataAccessException("unable to store authToken");
        }

        var result = new ResultObj(Map.of(new String( "username"),
                request.GetUsername(), new String("authToken"), token));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

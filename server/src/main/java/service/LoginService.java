package service;

import dataaccess.*;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Map;


public class LoginService implements Service{

    JsonHandler handler = null;

    public LoginService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new SQLAuthAccess();
        var userAccess = new SQLUserAccess();
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new DataAccessException("bad request");
        }
        if (!userAccess.find(request.getUsername())) {
            throw new DataAccessException("unauthorized");
        }
        var user = (UserData) userAccess.read(request.getUsername());
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
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

package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import dataAccess.*;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.UserData;
import model.*;

import java.util.Map;


public class JoinGameService implements Service{

    JsonHandler handler = null;

    public JoinGameService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new AuthAccess();
        var userAccess = new UserAccess();
        var gameAccess = new GameAccess();
        if (request.GetColor() == null || request.GetGameID() == null
                || request.GetAuthToken() == null || (!request.GetColor().equals("WHITE")
                && !request.GetColor().equals("BLACK"))) {
            throw new DataAccessException("bad request");
        }
        if (authAccess.Find(request.GetAuthToken())) {
            throw new DataAccessException("unauthorized");
        }
        if (!gameAccess.Find(request.GetGameID())) {
            throw new DataAccessException("bad request");
        }
        var game = (GameData) gameAccess.Read(request.GetGameID());
        if ((request.GetColor().equals("WHITE") && game.GetWhiteUsername() != null)
        || (request.GetColor().equals("BLACK") && game.GetBlackUsername() != null)) {
            throw new DataAccessException("already taken");
        }
        var auth = (AuthData) authAccess.Read(request.GetAuthToken());

        if (request.GetColor().equals("WHITE")) {
            game.SetWhiteUsername(auth.GetUsername());
        }
        else {
            game.SetBlackUsername(auth.GetUsername());
        }
        if (!gameAccess.Update(game)) {
            throw new DataAccessException("unable to alter game");
        };

        var result = new ResultObj(Map.of( "code", "200"));
        return handler.Serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

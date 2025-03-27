package service;

import chess.ChessGame;
import dataaccess.*;
import handler.Handler;
import handler.JsonHandler;
import model.AuthData;
import model.*;

import java.util.Map;


public class MakeGameService implements Service{

    JsonHandler handler = null;

    public MakeGameService(){}

    public String[] run(ServiceObj serviceObj) throws DataAccessException {
        RequestObj request = (RequestObj) serviceObj;
        var authAccess = new SQLAuthAccess();
        var gameAccess = new SQLGameAccess();
        if (request.getGameName() == null || request.getAuthToken() == null) {
            throw new DataAccessException("bad request");
        }
        if (!authAccess.find(request.getAuthToken())) {
            throw new DataAccessException("unauthorized");
        }

        String gameID = AuthData.makeAuthToken();
        GameData newGame = new GameData(Integer.parseInt(gameID), request.getGameName());
        newGame.setGame(new ChessGame());
        if (!gameAccess.create(newGame)) {
            throw new DataAccessException("unable to create new game");
        }

        var result = new ResultObj(Map.of("gameID", gameID, "code", "200"));
        return handler.serialize(result);
    }

    public void registerHandler(Handler newHandler) {
        handler = (JsonHandler) newHandler;
    }

}

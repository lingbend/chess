package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthAccess;
import dataaccess.SQLGameAccess;
import handler.Handler;
import handler.WebSocketHandler;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;

import spark.*;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;



import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

public class WebSocketService {

    ChessGame currentGame;

    public WebSocketService(){}

    public String run(UserGameCommand request, Session session, TreeMap<String, ArrayList<Session>> liveGames)
            throws Exception {
        UserGameCommand.CommandType command = request.commandType;
        var authAccess = new SQLAuthAccess();
        var gameAccess = new SQLGameAccess();
        var auth = (AuthData) authAccess.read(request.getAuthToken());
        var game = (GameData) gameAccess.read(request.gameID);
        ChessGame gameObj = game.getGame();
        var username = auth.getUsername();
        String whiteUsername = game.getWhiteUsername();
        String blackUsername = game.getBlackUsername();

        if (request.getGameID() == null || request.getAuthToken() == null) {
            throw new DataAccessException("bad request");
        }

        if (!authAccess.find(request.getAuthToken())) {
            throw new DataAccessException("unauthorized");
        }

        if (!gameAccess.find(request.getGameID())) {
            throw new DataAccessException("bad request");
        }



        if (command == UserGameCommand.CommandType.CONNECT) {
            if (liveGames.get(request.gameID).isEmpty()) {
                liveGames.put(request.gameID, new ArrayList<Session>());
            }
            liveGames.get(request.gameID).add(session);
            if (!whiteUsername.equals(username) && !blackUsername.equals(username)) {
                sendMessage(liveGames.get(request.gameID), session, username + " joined game as observer",
                        ServerMessage.ServerMessageType.NOTIFICATION);
            }
            else if (whiteUsername.equals(username)) {
                sendMessage(liveGames.get(request.gameID), session, username + " joined game as white",
                        ServerMessage.ServerMessageType.NOTIFICATION);
            }
            else {
                sendMessage(liveGames.get(request.gameID), session, username + " joined game as black",
                        ServerMessage.ServerMessageType.NOTIFICATION);
            }
        }
        else if (command == UserGameCommand.CommandType.MAKE_MOVE &&
                (username.equals(whiteUsername) || username.equals(blackUsername))) {
            ChessPosition start = request.move.getStartPosition();
            ChessMove move = request.move;
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) gameObj.validMoves(start);
            if (validMoves.contains(move)) {
                gameObj.makeMove(move);
                if (gameAccess.update(gameObj)) {
                    currentGame = gameObj;
                    sendMessage(liveGames.get(request.gameID), session, username + " moved " + move,
                            ServerMessage.ServerMessageType.NOTIFICATION);
                    sendMessage(liveGames.get(request.gameID), session, "moved",
                            ServerMessage.ServerMessageType.LOAD_GAME);
                }
                else {
                    throw new DataAccessException("server failed move");
                }
            }
            else {
                throw new DataAccessException("illegal move");
            }
        }
        else if (command == UserGameCommand.CommandType.LEAVE) {
            liveGames.get(request.gameID).remove(session);
            sendMessage(liveGames.get(request.gameID), session, username + " disconnected from the game",
                    ServerMessage.ServerMessageType.NOTIFICATION);
        }
        else if (command == UserGameCommand.CommandType.RESIGN) {
            if (whiteUsername.equals(username)) {
                game.setWhiteUsername(null);
                gameAccess.delete(request.gameID);
            }
            else if (blackUsername.equals(username)) {
                game.setBlackUsername(null);
                gameAccess.delete(request.gameID);
            }
            else {
                throw new DataAccessException("observers cannot resign");
            }
            currentGame = gameObj;
            sendMessage(liveGames.get(request.gameID), session, username + " resigned",
                    ServerMessage.ServerMessageType.NOTIFICATION);
        }
        else {
            throw new DataAccessException("bad request");
        }
        return null;
    }

    public void registerHandler(WebSocketHandler handler) {

    };


    private void sendMessage(ArrayList<Session> liveGame, Session session,
                                      String message, ServerMessage.ServerMessageType type) throws Exception {
        var serverMessage = new ServerMessage(type);
        serverMessage.setMessage(message);
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            serverMessage.setGame(currentGame);
        }
        for (Session otherSession : liveGame) {
            if (!otherSession.equals(session)) {
                otherSession.getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }
    }




}

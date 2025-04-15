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

    public void run(UserGameCommand request, Session session, TreeMap<String, ArrayList<Session>> liveGames)
            throws Exception {
        UserGameCommand.CommandType command = request.commandType;
        var authAccess = new SQLAuthAccess();
        var gameAccess = new SQLGameAccess();
        var auth = (AuthData) authAccess.read(request.getAuthToken());
        var game = (GameData) gameAccess.read(request.gameID);
        ChessGame gameObj = game.getGame();
        currentGame = gameObj;
        var username = auth.getUsername();
        String whiteUsername = game.getWhiteUsername() != null ? game.getWhiteUsername() : "";
        String blackUsername = game.getBlackUsername() != null ? game.getBlackUsername() : "";

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
            if (liveGames.get(request.gameID) == null || liveGames.get(request.gameID).isEmpty()) {
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
            sendOne(session, null, ServerMessage.ServerMessageType.LOAD_GAME);
        }
        else if (command == UserGameCommand.CommandType.MAKE_MOVE &&
                ((username.equals(whiteUsername) && gameObj.getTeamTurn() == ChessGame.TeamColor.WHITE)
                        || (username.equals(blackUsername) && gameObj.getTeamTurn() == ChessGame.TeamColor.BLACK)) &&
                game.getState().equals("active")) {
            ChessPosition start = request.move.getStartPosition();
            ChessMove move = request.move;
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) gameObj.validMoves(start);
            if (validMoves.contains(move)) {
                gameObj.makeMove(move);
                if (gameAccess.update(game)) {
                    currentGame = gameObj;
                    sendMessage(liveGames.get(request.gameID), session, username + " moved " + move,
                            ServerMessage.ServerMessageType.NOTIFICATION);
                    sendMessage(liveGames.get(request.gameID), session, null,
                            ServerMessage.ServerMessageType.LOAD_GAME);
                    String otherUsername;
                    if (gameObj.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                        otherUsername = game.getWhiteUsername();
                    }
                    else {
                        otherUsername = game.getBlackUsername();
                    }
                    if (gameObj.isInCheckmate(gameObj.getTeamTurn())) {
                        game.setState("inactive");
                        sendAll(liveGames.get(request.gameID), session, otherUsername + " is in checkmate",
                                ServerMessage.ServerMessageType.NOTIFICATION);
                    }
                    else if (gameObj.isInCheck(gameObj.getTeamTurn())) {
                        sendAll(liveGames.get(request.gameID), session, otherUsername + " is in check",
                                ServerMessage.ServerMessageType.NOTIFICATION);
                    }
                    else if (gameObj.isInStalemate(gameObj.getTeamTurn())) {
                        game.setState("inactive");
                        sendAll(liveGames.get(request.gameID), session, otherUsername
                                        + " is in stalemate", ServerMessage.ServerMessageType.NOTIFICATION);
                    }
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
            if (whiteUsername.equals(username)) {
                game.setWhiteUsername(null);
            }
            else if (blackUsername.equals(username)) {
                game.setBlackUsername(null);
            }
            if (liveGames.get(request.gameID) == null || liveGames.get(request.gameID).isEmpty()) {
                gameAccess.delete(Integer.valueOf(request.gameID));
            }
            else {
                gameAccess.update(game);
            }
        }
        else if (command == UserGameCommand.CommandType.RESIGN) {
            game.setState("inactive");
            if (whiteUsername.equals(username)) {
                game.setWhiteUsername(null);
                gameAccess.update(game);
            }
            else if (blackUsername.equals(username)) {
                game.setBlackUsername(null);
                gameAccess.update(game);
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
    }


    private void sendMessage(ArrayList<Session> liveGame, Session session,
                                      String message, ServerMessage.ServerMessageType type) throws Exception {
        var serverMessage = new ServerMessage(type);
        serverMessage.setMessage(message);
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            serverMessage.setGame(currentGame);
        }
        for (Session otherSession : liveGame) {
            if (!otherSession.equals(session) || type != ServerMessage.ServerMessageType.NOTIFICATION) {
                otherSession.getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }
    }

    private void sendAll(ArrayList<Session> liveGame, Session session,
                         String message, ServerMessage.ServerMessageType type) throws Exception {
        var serverMessage = new ServerMessage(type);
        serverMessage.setMessage(message);
        for (Session otherSession : liveGame) {
            otherSession.getRemote().sendString(new Gson().toJson(serverMessage));
        }
    }

    private void sendOne(Session session, String message, ServerMessage.ServerMessageType type) throws Exception {
        var serverMessage = new ServerMessage(type);
        serverMessage.setMessage(message);
        serverMessage.setGame(currentGame);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }
}

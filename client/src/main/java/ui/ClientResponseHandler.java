package ui;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class ClientResponseHandler {

    ServerMessage.ServerMessageType type;
    ChessGame game;
    String message;
    ClientStorage clientDB;

    public ClientResponseHandler(ServerMessage serverMessage, ClientStorage storage) {
        type = serverMessage.getServerMessageType();
        game = serverMessage.getGame();
        message = serverMessage.getMessage();
        clientDB = storage;
    }

    public void run() throws Exception {
        if (type == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(message);
        }
        else if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            clientDB.drawer.drawBoard(game, null);
        }
        else {
            System.out.println(message);
        }
    }




}

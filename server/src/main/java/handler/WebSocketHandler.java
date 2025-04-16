package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import service.RequestObj;
import service.ResultObj;
import service.Service;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;


import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import spark.*;
import websocket.messages.ServerMessage;

import java.util.ArrayList;


public class WebSocketHandler {

    WebSocketService service;

    public WebSocketHandler(WebSocketService serviceType){
        service = serviceType;
    }

    public void deserialize(UserGameCommand request, Session session, TreeMap<String, ArrayList<Session>> liveGames) {
        try {
            initializeDB();
            service.run(request, session, liveGames);
        }
        catch (Exception ex) {
            String code = "500";
            if (ex.getMessage().equals("already taken")) {
                code = "403";
            }
            else if (ex.getMessage().equals("bad request")){
                code = "400";
            }
            else if (ex.getMessage().equals("unauthorized")){
                code = "401";
            }
            String result =  "Error: " + ex.getMessage();
            try {
                sendMessage(session, result, ServerMessage.ServerMessageType.ERROR);
            }
            catch (Exception ex2){}
        }
    }

    private void sendMessage(Session session,
                             String message, ServerMessage.ServerMessageType type) throws Exception {
        var serverMessage = new ServerMessage(type);
        serverMessage.setErrorMessage(message);

        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    public static void initializeDB() throws DataAccessException{
//        new DB();
        DatabaseManager.initializeEntireDatabase();
    }
}

package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import service.RequestObj;
import service.ResultObj;
import service.Service;
import service.WebSocketService;
import websocket.commands.UserGameCommand;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import spark.*;
import java.util.ArrayList;


public class WebSocketHandler {

    WebSocketService service;

    public WebSocketHandler(WebSocketService serviceType){
        service = serviceType;
    }

    public String deserialize(UserGameCommand request, Session session, TreeMap<String, ArrayList<Session>> liveGames) {
        try {
            initializeDB();
            return service.run(request, session, liveGames);
        }
        catch (DataAccessException ex) {
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
            var result = new ResultObj(Map.of("code", code, "message", "Error: " + ex.getMessage()));
            return serialize(result);
        }
    }

    public String serialize(ResultObj obj) {
        String code = obj.getCode();
        obj.setCode(null);
        String json;
        if (code != null) {
            json = new Gson().toJson(obj);
        }
        else {
            json = "";
        }
        String[] response = {code, json};
        return response;
    }

    public static void initializeDB() throws DataAccessException{
//        new DB();
        DatabaseManager.initializeEntireDatabase();
    }
}

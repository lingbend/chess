package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.*;


import spark.*;
import service.*;
import service.Service;
import handler.*;
import dataaccess.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.lang.String;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.TreeMap;

@WebSocket
public class Server {

    private TreeMap<String, ArrayList<Session>> liveGames = new TreeMap<>();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", Server.class);
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::findGames);
        Spark.post("/game", this::makeGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        var service =  new RegisterService();
        return passToHandler(req, res, service);
    }

    private Object clear(Request req, Response res) {
        var service = new ClearService();
        return passToHandler(req, res, service);
    }

    private Object login(Request req, Response res) {
        var service = new LoginService();
        return passToHandler(req, res, service);
    }

    private Object logout(Request req, Response res) {
        var service = new LogoutService();
        return passToHandler(req, res, service);
    }

    private Object findGames(Request req, Response res) {
        var service = new FindGamesService();
        return passToHandler(req, res, service);
    }

    private Object makeGame(Request req, Response res) {
        var service = new MakeGameService();
        return passToHandler(req, res, service);
    }

    private Object joinGame(Request req, Response res) {
        var service = new JoinGameService();
        return passToHandler(req, res, service);
    }

    private Object passToHandler(Request req, Response res, Service service) {
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        String auth;
        if (req.headers("authorization") != null) {
            auth = req.headers("authorization");
        }
        else {
            auth = req.headers("authToken");
        }

        String[] handlerRes = handler.deserialize(req.body(), auth);
        res.type("application/json");
        res.status(Integer.valueOf(handlerRes[0]));
        return handlerRes[1];
    }



    @OnWebSocketMessage
    private void onMessage(Session session, String msg) {
        UserGameCommand request = new Gson().fromJson(msg, UserGameCommand.class);
        var service = new WebSocketService();
        var handler = new WebSocketHandler(service);
        handler.deserialize(request, session, liveGames);
        //Then return the response here
    }
}

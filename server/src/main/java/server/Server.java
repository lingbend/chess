package server;

import spark.*;
import service.*;
import service.Service;
import handler.*;
import dataAccess.DB;
import java.lang.String;
import java.lang.Integer;

public class Server {

    public int run(int desiredPort) {
        new DB();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        //This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();

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

    private Object passToHandler(Request req, Response res, Service service) {
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        String[] handlerRes = handler.Deserialize(req.body());
        res.type("application/json");
        res.status(Integer.valueOf(handlerRes[0]));
        return handlerRes[1];

    }
}

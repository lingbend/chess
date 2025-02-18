package server;

import spark.*;
import service.*;
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
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        String[] handlerRes = handler.Deserialize(req.body());
        res.type("application/json");
        res.status(Integer.valueOf(handlerRes[0]));
        return handlerRes[1];
    }
}

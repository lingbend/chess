package ui;

import websocket.commands.UserGameCommand;
import com.google.gson.Gson;

import javax.websocket.*;
import java.net.URI;

public class WebSocketConnector extends Endpoint{

    ClientStorage clientDB;
    Session session;


    public WebSocketConnector(ClientStorage clientDB) throws Exception {
        this.clientDB = clientDB;
        clientDB.webSocket = this;
        URI uri = new URI("ws://" + clientDB.baseUri + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
        session.addMessageHandler(new MessageHandler.Whole<String>(){
            public void onMessage(String msg) {

            };
        });



    }

    public void transmit(UserGameCommand gameCommand) throws Exception {
        String msg = new Gson().toJson(gameCommand);
        session.getBasicRemote().sendText(msg);
    }


    public void onOpen(Session session, EndpointConfig config) {}

    public void close() throws Exception {
        session.close();
    }



}

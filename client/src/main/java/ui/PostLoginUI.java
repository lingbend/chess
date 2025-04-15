package ui;

import chess.ChessGame;
import websocket.commands.UserGameCommand;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PostLoginUI {

    public HttpURLConnection connection;
    public TreeMap<String, String> header;
    public TreeMap<String, String> body;
    public ArrayList<String> parameters;
    private ClientStorage clientDB;

    public PostLoginUI(ClientStorage clientDB){
        this.clientDB = clientDB;
    }


    public void run(String command, ArrayList<String> parameters) throws Exception {
        this.parameters = parameters;
            if (command.equalsIgnoreCase("help")) {
                System.out.print(getHelp());
            }
            else if (command.equalsIgnoreCase("logout")) {
                checkState(ServerFacadeLocal.State.LoggedIn, 0);
                System.out.println("Logging out...");
                logout();
            }
            else if (command.equalsIgnoreCase("create")) {
                checkState(ServerFacadeLocal.State.LoggedIn, 1);
                System.out.println("Creating game...");
                createGame(parameters.get(0));
            }
            else if (command.equalsIgnoreCase("join")) {
                checkState(ServerFacadeLocal.State.LoggedIn, 2);
                System.out.println("Joining game...");
                if (clientDB.existingGames.isEmpty()) {
                    throw new Exception("Game not found. Use 'list' to find games and 'create' if none exist");
                }
                playGame(Integer.parseInt(parameters.get(0)),
                        Enum.valueOf(ChessGame.TeamColor.class, parameters.get(1).toUpperCase()));
            }
            else if (command.equalsIgnoreCase("observe")) {
                checkState(ServerFacadeLocal.State.LoggedIn, 1);
                System.out.println("Loading game as observer...");
                observeGame(Integer.parseInt(parameters.get(0)));
            }
            else if (command.equalsIgnoreCase("list")) {
                checkState(ServerFacadeLocal.State.LoggedIn, 0);
                System.out.println("Retrieving current games...");
                listGames();
            }
            else if (command.equals("")) {}
            else {
                System.out.println("Error: Not a valid input");
            }
    }

    private void checkState(ServerFacadeLocal.State state, int size) throws Exception {
        if (state == ServerFacadeLocal.State.LoggedOut && clientDB.currentState != state) {
            throw new Exception("Must logout first");
        }
        else if (state == ServerFacadeLocal.State.LoggedIn && clientDB.currentState == ServerFacadeLocal.State.LoggedOut) {
            throw new Exception("Not logged in. Login first");
        }
        else if (parameters.size() != size) {
            throw new FacadeException("Error: Wrong number of inputs");
        }
    }

    private String getHelp() {
        return
                """
                Available Commands:                                               Format:
                Log out of the Chess System                                       | logout
                Create a new chess game by naming it                              | create (game name)
                Get a list of all chess games currently ongoing and their numbers | list
                Join a chess game to start playing                                | join (game number) color
                Watch a chess game                                                | observe (game number)
                View currently available options                                  | help
                """;
    }

    public void logout() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap();
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/session", "DELETE");
        connector.getResponse();
        clientDB.currentState = ServerFacadeLocal.State.LoggedOut;
        System.out.print("\u001b[95m");
        System.out.println("...Logged out successfully");
        clientDB.username = "";
    }

    public String createGame(String gameName) throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap(Map.of("gameName", gameName));
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/game", "POST");
        TreeMap response = connector.getResponse();
        String gameID = (String) response.get("gameID");
        System.out.println("...Created game successfully");
        return gameID;
    }

    public void listGames() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap();
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/game", "GET");
        clientDB.existingGames = connector.getArrayResponse();
        System.out.println("...Created list of games successfully");
        System.out.println("Current Games: ");
        for (int i = 1; i < clientDB.existingGames.size() + 1; i++) {
            String white = clientDB.existingGames.get(i-1).getWhiteUsername();
            String black = clientDB.existingGames.get(i-1).getBlackUsername();
            System.out.printf("Game %s: %s | White's username: %s | Black's username: %s\n",
                    i, clientDB.existingGames.get(i - 1).getGameName(),
                    white != null ? white : "none",
                    black != null ? black : "none");
        }
    }

    public void playGame(int gameNumber, ChessGame.TeamColor color) throws Exception {
        String id;

        try {
            id = String.valueOf(clientDB.existingGames.get(gameNumber - 1).getGameID());
        }
        catch (IndexOutOfBoundsException ex) {
            throw new FacadeException("Error: Game doesn't exist. Choose a different number or use list to load games");
        }
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap(Map.of("playerColor", color, "gameID", id));
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/game", "PUT");
        connector.getResponse();

        if (color == ChessGame.TeamColor.WHITE) {
            clientDB.existingGames.get(gameNumber - 1).setWhiteUsername(clientDB.username);
        }
        else {
            clientDB.existingGames.get(gameNumber - 1).setBlackUsername(clientDB.username);
        }
        clientDB.currentGameID = id;
        clientDB.currentGame = clientDB.existingGames.get(gameNumber - 1);
        clientDB.currentState = ServerFacadeLocal.State.InGame;

        clientDB.webSocket = new WebSocketConnector(clientDB);
        clientDB.webSocket.transmit(new UserGameCommand(UserGameCommand.CommandType.CONNECT, clientDB.authToken,
                clientDB.currentGameID));

        System.out.printf("...Joined game %s\n", clientDB.currentGame.getGameName());
        System.out.print("\u001b[34;49m");
    }

    public void observeGame(int gameNumber) throws Exception {
        try {
            clientDB.currentGame = clientDB.existingGames.get(gameNumber - 1);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new FacadeException("Game doesn't exist. Choose a different number");
        }
        clientDB.currentGameID = String.valueOf(clientDB.currentGame.getGameID());
        clientDB.currentState = ServerFacadeLocal.State.Observing;

        clientDB.webSocket = new WebSocketConnector(clientDB);
        clientDB.webSocket.transmit(new UserGameCommand(UserGameCommand.CommandType.CONNECT, clientDB.authToken,
                clientDB.currentGameID));

        System.out.printf("...Observing game %s\n", clientDB.currentGame.getGameName());
        System.out.print(clientDB.drawer.drawBoard(new ChessGame(), null));
        System.out.print("\u001b[34;49m");
    }

}

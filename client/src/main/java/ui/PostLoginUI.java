package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PostLoginUI {

    public String username;
    public int authToken;
//    public String baseUri;
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
        getConnection("/session", "DELETE");
        getResponse();
        clientDB.currentState = ServerFacadeLocal.State.LoggedOut;
        System.out.print("\u001b[95m");
        System.out.println("...Logged out successfully");
        clientDB.username = "";
    }

    public String createGame(String gameName) throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap(Map.of("gameName", gameName));
        getConnection("/game", "POST");
        TreeMap response = getResponse();
        String gameID = (String) response.get("gameID");
        System.out.println("...Created game successfully");
        return gameID;
    }

    public void listGames() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(clientDB.authToken)));
        body = new TreeMap();
        getConnection("/game", "GET");
        clientDB.existingGames = getArrayResponse();
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
        getConnection("/game", "PUT");
        getResponse();

        if (color == ChessGame.TeamColor.WHITE) {
            clientDB.existingGames.get(gameNumber - 1).setWhiteUsername(clientDB.username);
        }
        else {
            clientDB.existingGames.get(gameNumber - 1).setBlackUsername(clientDB.username);
        }
        clientDB.currentGameID = id;
        clientDB.currentGame = clientDB.existingGames.get(gameNumber - 1);
        clientDB.currentState = ServerFacadeLocal.State.InGame;

        System.out.printf("...Joined game %s\n", clientDB.currentGame.getGameName());
        System.out.print(clientDB.drawer.drawBoard(new ChessGame(), null));
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

        System.out.printf("...Observing game %s\n", clientDB.currentGame.getGameName());
        System.out.print(clientDB.drawer.drawBoard(new ChessGame(), null));
        System.out.print("\u001b[34;49m");
    }

    private void getConnection(String path, String method) throws Exception {
        HttpURLConnection newConnection = (HttpURLConnection) (new URI(clientDB.baseUri + path)).toURL().openConnection();
        newConnection.setRequestMethod(method);
        newConnection.setDoOutput(true);
        newConnection.setDoInput(true);
        for (var entry : header.entrySet()) {
            newConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }

        if (!body.isEmpty()) {
            var outStream = newConnection.getOutputStream();
            String json = new Gson().toJson(body);
            outStream.write(json.getBytes());
            outStream.close();
        }

        newConnection.connect();
        connection = newConnection;
    }

    private TreeMap getResponse() throws Exception {
        int responseCode = connection.getResponseCode();
        TreeMap responseMap = getResponseHelper();

        responseMap.put("code", Integer.toString(responseCode));

        return responseMap;
    }

    private ArrayList<GameData> getArrayResponse() throws Exception {
        TreeMap responseMap = getResponseHelper();

        ArrayList rawArray = (ArrayList) responseMap.get("games");
        ArrayList<GameData> arrayResponse = new ArrayList<>();

        for (var i : rawArray) {
            arrayResponse.add(new Gson().fromJson((new Gson().toJson(i)), GameData.class));
        }

        return arrayResponse;
    }

    private TreeMap getResponseHelper() throws IOException, FacadeException {
        int responseCode = connection.getResponseCode();
        InputStream responseStream;
        if (responseCode != 200) {
            responseStream = connection.getErrorStream();
        }
        else {
            responseStream = connection.getInputStream();
        }
        InputStreamReader reader = new InputStreamReader(responseStream);

        TreeMap responseMap = new Gson().fromJson(reader, TreeMap.class);

        reader.close();
        responseStream.close();
        if (responseCode != 200) {
            throw new FacadeException((String) responseMap.get("message"));
        }
        return responseMap;
    }
}

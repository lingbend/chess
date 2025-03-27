package ui;

import chess.ChessGame;
import model.GameData;
import server.Server;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.net.http.*;


public class ServerFacadeLocal implements ServerFacadeInterface{

    State currentState;
    String username;
    int authToken;
    String currentGameID;
    ArrayList<GameData> existingGames;
    GameData currentGame;
    String baseUri;
    HttpURLConnection connection;
    TreeMap<String, String> header;
    TreeMap<String, String> body;
    Scanner input;


    public ServerFacadeLocal() throws URISyntaxException {
        currentState = State.LoggedOut;
        authToken = 0;
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
        baseUri = "http://localhost:";
        header = new TreeMap<>();
        body = new TreeMap<>();
        input = new Scanner(System.in);
    }

    public static void main(String[] args) throws Exception {
        try {
            var server = new Server();
            var port = server.run(0);
            new ServerFacadeLocal().run(port);
            server.stop();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void run(int port) throws Exception {
        baseUri += String.valueOf(port);
        System.out.print("\u001b[95m");
        System.out.println("Welcome to Chess!");

        String input = "";
        ArrayList<String> parameters = new ArrayList<>();

        while (true) {
            try {
                if (input.equalsIgnoreCase("help")) {
                    System.out.print(getHelp());
                }
                else if (input.equalsIgnoreCase("login")) {
                    if (currentState != State.LoggedOut) {
                        throw new Exception("Already logged in. Logout first");
                    }
                    else if (parameters.size() != 2) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Logging in...");
                    login(parameters.get(0), parameters.get(1));
                }
                else if (input.equalsIgnoreCase("register")) {
                    if (currentState != State.LoggedOut) {
                        throw new Exception("Logout before registering a new account");
                    }
                    else if (parameters.size() != 3) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Registering user...");
                    register(parameters.get(0), parameters.get(1), parameters.get(2));
                }
                else if (input.equalsIgnoreCase("logout")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    else if (parameters.size() != 0) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Logging out...");
                    logout();
                }
                else if (input.equalsIgnoreCase("create")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    else if (parameters.size() != 1) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Creating game...");
                    createGame(parameters.get(0));
                }
                else if (input.equalsIgnoreCase("join")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    else if (parameters.size() != 2) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Joining game...");
                    if (existingGames.isEmpty()) {
                        throw new Exception("Game not found. Make sure to list" +
                                " existing games and create one if none exist");
                    }
                    playGame(Integer.parseInt(parameters.get(0)),
                            Enum.valueOf(ChessGame.TeamColor.class, parameters.get(1).toUpperCase()));
                }
                else if (input.equalsIgnoreCase("observe")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    else if (parameters.size() != 1) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Loading game as observer...");
                    observeGame(Integer.parseInt(parameters.get(0)));
                }
                else if (input.equalsIgnoreCase("list")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    else if (parameters.size() != 0) {
                        throw new FacadeException("Error: Wrong number of inputs");
                    }
                    System.out.println("Retrieving current games...");
                    listGames();
                    System.out.println("Current Games: ");
                    for (int i = 1; i < existingGames.size() + 1; i++) {
                        String white = existingGames.get(i-1).getWhiteUsername();
                        String black = existingGames.get(i-1).getBlackUsername();
                        System.out.printf("Game %s: %s | White's username: %s | Black's username: %s\n",
                                i, existingGames.get(i - 1).getGameName(),
                                white != null ? white : "none",
                                black != null ? black : "none");
                    }
                }
                else if (input.equalsIgnoreCase("quit")) {
                    if (currentState != State.LoggedOut) {
                        throw new Exception("Logout before quitting");
                    }
                    break;
                }
                else if (input.equals("")) {}
                else {
                    System.out.println("Error: Not a valid input");
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("Error: " + ex.getMessage() + ". Not a number");
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Error: one or more of your data fields is the wrong type");
            }
            catch (FacadeException ex) {
                System.out.println(ex.getMessage());
            }
            catch (IndexOutOfBoundsException ex) {
                System.out.println("Need more information to be inputted. Try again. Print help for more information.");
            }
            catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            finally {
                try {
                    header.clear();
                    body.clear();
                    parameters.clear();
                    if ((!input.equalsIgnoreCase("quit")) || currentState != State.LoggedOut) {
                        parameters = getInput();
                        input = parameters.get(0);
                        parameters.remove(0);
                    }
                }
                catch (IndexOutOfBoundsException ex) {
                    System.out.println("Please input properly formatted commands. Print help for more information.");
                }

            }
        }
        System.out.println("...Disconnected");
        if (connection != null) {
            connection.disconnect();
        }
    }

    public String getHelp() {
        if (currentState == State.LoggedOut) {
            return
                """
                Available Commands:              Format:
                Log in to Chess System           | login username password
                Register a new account for Chess | register username password email
                Close the Chess program          | quit
                View currently available options | help
                """;
        }
        else {
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

    }

    public void login(String username, String password) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password));
        getConnection("/session", "POST");
        TreeMap response = getResponse();
        authToken = Integer.parseInt((String) response.get("authToken"));
        currentState = State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Logged in successfully");
    }

    public void register(String username, String password, String email) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password, "email", email));
        getConnection("/user", "POST");
        TreeMap response = getResponse();
        authToken = Integer.parseInt((String) response.get("authToken"));
        currentState = State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Registered and logged in successfully");
    }

    public void logout() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap();
        getConnection("/session", "DELETE");
        getResponse();
        currentState = State.LoggedOut;
        System.out.print("\u001b[95m");
        System.out.println("...Logged out successfully");
    }

    public String createGame(String gameName) throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap(Map.of("gameName", gameName));
        getConnection("/game", "POST");
        TreeMap response = getResponse();
        String gameID = (String) response.get("gameID");
        System.out.println("...Created game successfully");
        return gameID;
    }

    public void listGames() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap();
        getConnection("/game", "GET");
        existingGames = getArrayResponse();
//        existingGames = (ArrayList<GameData>) response.get("games");
        System.out.println("...Created list of games successfully");
    }

    public void playGame(int gameNumber, ChessGame.TeamColor color) throws Exception {
        String id;

        try {
            id = String.valueOf(existingGames.get(gameNumber - 1).getGameID());
        }
        catch (IndexOutOfBoundsException ex) {
            throw new FacadeException("Error: Game doesn't exist. Choose a different number");
        }
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap(Map.of("playerColor", color, "gameID", id));
        getConnection("/game", "PUT");
        getResponse();

        currentGameID = id;
        currentGame = existingGames.get(gameNumber - 1);
        currentState = State.InGame;

        System.out.printf("...Joined game #%s: %s\n", currentGameID, currentGame.getGameName());
    }

    public void observeGame(int gameNumber) throws Exception {
        try {
            currentGame = existingGames.get(gameNumber - 1);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new FacadeException("Game doesn't exist. Choose a different number");
        }
        currentGameID = String.valueOf(currentGame.getGameID());
        currentState = State.Observing;

        System.out.printf("...Observing game %s\n", currentGame.getGameName());
    }

    private ArrayList<String> getInput(){
        String line = input.nextLine();
        ArrayList<String> output = new ArrayList<>((List.of(line.split(" "))));
        for (int i=0;i < output.size();i++) {
            output.set(i, output.get(i));
        }
        return output;
    }

    private void getConnection(String path, String method) throws Exception {
        HttpURLConnection newConnection = (HttpURLConnection) (new URI(baseUri + path)).toURL().openConnection();
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

        responseMap.put("code", Integer.toString(responseCode));

        return responseMap;
    }

    private ArrayList<GameData> getArrayResponse() throws Exception {
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

        ArrayList rawArray = (ArrayList) responseMap.get("games");
        ArrayList<GameData> arrayResponse = new ArrayList<>();

        for (var i : rawArray) {
            arrayResponse.add(new Gson().fromJson((new Gson().toJson(i)), GameData.class));
        }

        return arrayResponse;
    }
}

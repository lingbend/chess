package ui;

import chess.ChessGame;
import model.GameData;

import com.google.gson.Gson;

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
    String authToken;
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
        authToken = "";
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
        baseUri = "http://localhost://8080";
        header = new TreeMap<>();
        body = new TreeMap<>();
        input = new Scanner(System.in);
    }

    public static void main(String[] args) throws Exception {
        new ServerFacadeLocal().run();
    }


    public void run() throws Exception {
        System.out.println("Welcome to Chess!");

        String input = "";
        ArrayList<String> parameters = new ArrayList<>();

        while (!input.equals("quit")) {
            try {
                if (input.equals("help")) {
                    getHelp();
                }
                else if (input.equals("login")) {
                    if (currentState != State.LoggedOut) {
                        throw new Exception("Already logged in. Logout first");
                    }
                    System.out.println("Logging in...");
                    login(parameters.get(0), parameters.get(1));
                }
                else if (input.equals("register")) {
                    System.out.println("Registering user...");
                    register(parameters.get(0), parameters.get(1), parameters.get(2));
                }
                else if (input.equals("logout")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    System.out.println("Logging out...");
                    logout();
                }
                else if (input.equals("create")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    System.out.println("Creating game...");
                    createGame(parameters.get(0));
                }
                else if (input.equals("join")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    System.out.println("Joining game...");
                    if (existingGames.isEmpty()) {
                        throw new Exception("Game not found. Make sure to list" +
                                " existing games and create one if none exist");
                    }
                    playGame(Integer.parseInt(parameters.get(0)), Enum.valueOf(ChessGame.TeamColor.class, parameters.get(1)));
                }
                else if (input.equals("observe")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    System.out.println("Loading game as observer...");
                    observeGame(Integer.parseInt(parameters.get(0)));
                }
                else if (input.equals("list")) {
                    if (currentState == State.LoggedOut) {
                        throw new Exception("Not logged in. Login first");
                    }
                    System.out.println("Retrieving current games...");
                    listGames();
                    System.out.println("Current Games: ");
                    for (int i = 0; i < existingGames.size(); i++) {
                        System.out.printf("Game %s: %s || White: %s Black: %s\n", i, existingGames.get(i).getGameName(),
                                existingGames.get(i).getWhiteUsername(), existingGames.get(i).getBlackUsername());
                    }
                }
                else if (input.equals("")) {}
                else {
                    System.out.println("Error: Bad input");
                }
            }
            catch (FacadeException ex) {
                System.out.println("Server Connection Error: " + ex.getMessage());
            }
            catch (IndexOutOfBoundsException ex) {
                System.out.println("Need more information to be inputted. Try again. Print help for more information.");
            }
            catch (Exception ex) {
                System.out.println("General Error: " + ex.getMessage());
            }
            finally {
                try {
                    header.clear();
                    body.clear();
                    parameters.clear();
                    parameters = getInput();
                    input = parameters.get(0);
                    parameters.remove(0);
                }
                catch (IndexOutOfBoundsException ex) {
                    System.out.println("Please input properly formatted commands. Print help for more information.");
                }

            }
        }
        System.out.println("Quitting Chess...");
        connection.disconnect();
    }

    public String getHelp() {
        return "";
    }

    public void login(String username, String password) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password));
        getConnection("/session", "POST");
        TreeMap response = getResponse();
        authToken = (String) response.get("authToken");
        currentState = State.LoggedIn;
        System.out.println("...Logged in successfully");
    }

    public void register(String username, String password, String email) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password, "email", email));
        getConnection("/user", "POST");
        TreeMap response = getResponse();
        authToken = (String) response.get("authToken");
        currentState = State.LoggedIn;
        System.out.println("...Registered and logged in successfully");
    }

    public void logout() throws Exception {
        header = new TreeMap(Map.of("authToken", authToken));
        body = new TreeMap();
        getConnection("/session", "DELETE");
        getResponse();
        currentState = State.LoggedOut;
        System.out.println("...Logged out successfully");
    }

    public String createGame(String gameName) throws Exception {
        header = new TreeMap(Map.of("authToken", authToken));
        body = new TreeMap(Map.of("gameName", gameName));
        getConnection("/game", "POST");
        TreeMap response = getResponse();
        String gameID = (String) response.get("gameID");
        System.out.println("...Created game successfully");
        return gameID;
    }

    public void listGames() throws Exception {
        header = new TreeMap(Map.of("authToken", authToken));
        body = new TreeMap();
        getConnection("/game", "GET");
        TreeMap response = getResponse();
        existingGames = (ArrayList<GameData>) response.get("games");
        System.out.println("...Created list of games successfully");
    }

    public void playGame(int gameNumber, ChessGame.TeamColor color) throws Exception {
        String id = String.valueOf(existingGames.get(gameNumber).getGameID());
        header = new TreeMap(Map.of("authToken", authToken));
        body = new TreeMap(Map.of("playerColor", color, "gameID", id));
        getConnection("/game", "PUT");
        getResponse();

        currentGameID = id;
        currentGame = existingGames.get(gameNumber);
        currentState = State.InGame;

        System.out.printf("...Joined game #%s: %s\n", currentGameID, currentGame.getGameName());
    }

    public void observeGame(int gameNumber) {
        currentGame = existingGames.get(gameNumber);
        currentGameID = String.valueOf(currentGame.getGameID());
        currentState = State.Observing;

        System.out.printf("...Observing game #%s: %s\n", currentGameID, currentGame.getGameName());
    }

    private ArrayList<String> getInput(){
        String line = input.nextLine();
        ArrayList<String> output = new ArrayList<>((List.of(line.split(" "))));
        for (int i=0;i < output.size();i++) {
            output.set(i, output.get(i).toLowerCase(Locale.ROOT));
        }
        return output;
    }

    private void getConnection(String path, String method) throws Exception {
        HttpURLConnection newConnection = (HttpURLConnection) (new URI(baseUri + path)).toURL().openConnection();
        newConnection.setRequestMethod(method);
        newConnection.setDoOutput(true);
        for (var entry : header.entrySet()) {
            newConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        var outStream = newConnection.getOutputStream();
        var json = new Gson().toJson(body);
        outStream.write(json.getBytes());
        outStream.close();
        newConnection.connect();
        connection = newConnection;
    }

    private TreeMap getResponse() throws Exception {
        int responseCode = connection.getResponseCode();
        var responseStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(responseStream);

        TreeMap responseMap = new Gson().fromJson(reader, TreeMap.class);

        reader.close();
        responseStream.close();

        if (responseCode != 200) {
            throw new FacadeException(responseCode + ": " + responseMap.get("message"));
        }

        responseMap.put("code", Integer.toString(responseCode));

        return responseMap;
    }
}

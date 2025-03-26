package ui;

import chess.ChessGame;
import model.GameData;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.net.http.*;
import java.util.List;
import java.util.TreeMap;
import java.util.Scanner;



public class ServerFacadeLocal implements ServerFacadeInterface{

    State currentState;
    String username;
    String authToken;
    String currentGameID;
    ArrayList<GameData> existingGames;
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
                    System.out.println("Logging in...");
                    login(parameters.get(0), parameters.get(1));
                }
                else if (input.equals("register")) {
                    System.out.println("Registering user...");
                    connection.setRequestMethod("POST");
                }
                else if (input.equals("logout")) {
                    System.out.println("Logging out...");
                    connection.setRequestMethod("DELETE");
                }
                else if (input.equals("create")) {
                    System.out.println("Creating game...");
                    connection.setRequestMethod("POST");
                }
                else if (input.equals("join")) {
                    System.out.println("Joining game...");
                    connection.setRequestMethod("PUT");
                }
                else if (input.equals("observe")) {
                    System.out.println("Loading game as observer...");
                    connection.setRequestMethod("GET");
                }
                else if (input.equals("list")) {
                    System.out.println("Retrieving current games...");
                    connection.setRequestMethod("GET");
                }
                else if (input.equals("")) {}
                else {
                    System.out.println("Bad input");
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
        connection.disconnect();
        System.out.println("Quitting Chess...");
    }

    public String getHelp() {
        return "";
    }

    public void quit() {

    }

    public void login(String username, String password) throws Exception {
        getConnection("/session", "POST");
        TreeMap response = getResponse();
        authToken = (String) response.get("authToken");
        currentState = State.LoggedIn;
        System.out.println("...Logged in successfully");
        // message user they are logged in
    }

    public String register(String username, String password, String email) {
        return "";
    }

    public boolean logout() {
        return false;
    }

    public String createGame(String gameName) {
        return "";
    }

    public ArrayList<GameData> listGames() {
        return null;
    }

    public String playGame(int gameNumber, ChessGame.TeamColor color) {
        return "";
    }

    public boolean observeGame(int gameNumber) {
        return false;
    }

    private ArrayList<String> getInput(){
//        while (!input.hasNext()) {
//        }
        String line = input.nextLine();
        ArrayList<String> output = new ArrayList<>((List.of(line.split(" "))));
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

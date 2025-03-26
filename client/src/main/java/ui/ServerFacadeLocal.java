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
import java.util.TreeMap;



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


    public ServerFacadeLocal() throws URISyntaxException {
        currentState = State.LoggedOut;
        authToken = "";
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
        baseUri = "http://localhost://8080";
        header = new TreeMap<>();
        body = new TreeMap<>();
    }

    public static void main(String[] args) {

    }


    public void run() throws Exception {
        String input = "";
        ArrayList<String> parameters = new ArrayList<>();


        while (input != "quit") {
            try {

                if (input == "help") {
                    getHelp();
                }
                else if (input == "login") {
                    login(parameters.get(0), parameters.get(1));

                }
                else if (input == "register") {
                    connection.setRequestMethod("POST");


                }
                else if (input == "logout") {
                    connection.setRequestMethod("DELETE");


                }
                else if (input == "create") {
                    connection.setRequestMethod("POST");


                }
                else if (input == "join") {
                    connection.setRequestMethod("PUT");


                }
                else if (input == "observe") {
                    connection.setRequestMethod("GET");


                }
                else if (input == "list") {
                    connection.setRequestMethod("GET");


                }
                else {
                    // bad param(s)
                    continue;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                //check your parameters user!
            }
            catch (FacadeException ex) {
                //Server error
            }
            catch (Exception ex) {

            }
            finally {
                header.clear();
                body.clear();
                parameters.clear();
                parameters = getInput();
                input = parameters.get(0);
                parameters.remove(0);
            }
        }
        connection.disconnect();
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
        //message user they are logged in
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

        return null;
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

package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

public class PreLoginUI {
    public HttpURLConnection connection;
    public TreeMap<String, String> header;
    public TreeMap<String, String> body;
    public ArrayList<String> parameters;
    private ClientStorage clientDB;

    public PreLoginUI(ClientStorage clientDB){
        this.clientDB = clientDB;
    }

    public void run(String command, ArrayList<String> parameters) throws Exception {
        this.parameters = parameters;
        if (command.equals("help")) {
            System.out.print(getHelp());
        }
        else if (command.equals("login")) {
            checkParameters(2);
            System.out.println("Logging in...");
            login(parameters.get(0), parameters.get(1));
        }
        else if (command.equals("register")) {
            checkParameters(3);
            System.out.println("Registering user...");
            register(parameters.get(0), parameters.get(1), parameters.get(2));
        }
        else if (command.equals("")) {}
        else {
            System.out.println("Error: Not a valid input");
        }
    }


    private void checkParameters(int size) throws Exception {
        if (parameters.size() != size) {
            throw new FacadeException("Error: Wrong number of inputs");
        }
    }

    private String getHelp() {
        return
                """
                Available Commands:              Format:
                Log in to Chess System           | login username password
                Register a new account for Chess | register username password email
                Close the Chess program          | quit OR exit
                View currently available options | help
                """;
    }

    public void login(String username, String password) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password));
        getConnection("/session", "POST");
        TreeMap response = getResponse();
        clientDB.authToken = Integer.parseInt((String) response.get("authToken"));
        clientDB.currentState = ServerFacadeLocal.State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Logged in successfully");
        clientDB.username = username;
    }

    public void register(String username, String password, String email) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password, "email", email));
        getConnection("/user", "POST");
        TreeMap response = getResponse();
        clientDB.authToken = Integer.parseInt((String) response.get("authToken"));
        clientDB.currentState = ServerFacadeLocal.State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Registered and logged in successfully");
        clientDB.username = username;
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



package ui;

import java.net.HttpURLConnection;
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
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/session", "POST");
        TreeMap response = connector.getResponse();
        clientDB.authToken = Integer.parseInt((String) response.get("authToken"));
        clientDB.currentState = ServerFacadeLocal.State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Logged in successfully");
        clientDB.username = username;
    }

    public void register(String username, String password, String email) throws Exception {
        header = new TreeMap();
        body = new TreeMap(Map.of("username", username, "password", password, "email", email));
        ClientConnector connector = new ClientConnector(header, body, clientDB);
        connector.getConnection("/user", "POST");
        TreeMap response = connector.getResponse();
        clientDB.authToken = Integer.parseInt((String) response.get("authToken"));
        clientDB.currentState = ServerFacadeLocal.State.LoggedIn;
        System.out.print("\u001b[34m");
        System.out.println("...Registered and logged in successfully");
        clientDB.username = username;
    }

}



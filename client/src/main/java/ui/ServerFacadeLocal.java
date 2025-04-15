package ui;
import model.GameData;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacadeLocal {

    public HttpURLConnection connection;
    public TreeMap<String, String> header;
    public TreeMap<String, String> body;
    public Scanner input;
    public int port;
    public String command;
    public ArrayList<String> parameters;
    public ClientStorage clientDB;
    public PreLoginUI preLoginUI;
    public PostLoginUI postLoginUI;
    public InGameUI inGameUI;

    public enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

    public ServerFacadeLocal(int portNum) throws URISyntaxException {
        clientDB = new ClientStorage();
        clientDB.currentState = State.LoggedOut;
        clientDB.authToken = 0;
        clientDB.username = "";
        clientDB.currentGameID = "";
        clientDB.existingGames = new ArrayList<GameData>();
        clientDB.baseUri = "http://localhost:";
        header = new TreeMap<>();
        body = new TreeMap<>();
        input = new Scanner(System.in);
        port = portNum;
        clientDB.drawer = new GameDrawer(this);
        preLoginUI = new PreLoginUI(clientDB);
        postLoginUI = new PostLoginUI(clientDB);
        inGameUI = new InGameUI(clientDB);

    }

    public void run() throws Exception {
        clientDB.baseUri += String.valueOf(port);
        System.out.println("\u001b[95mWelcome to Chess!");

        command = "";
        parameters = new ArrayList<>();

        runLoop();
        System.out.println("...Disconnected");
        if (connection != null) {
            connection.disconnect();
        }
    }

    private void runLoop() {
        while (true) {
            try {
                System.out.println(command);
                System.out.println(parameters);
                if (command.equals("quit") || command.equals("exit")) {
                    if (clientDB.currentState != State.LoggedOut) {
                        throw new Exception("Logout before quitting");
                    }
                    break;
                }
                if (clientDB.currentState == State.LoggedOut) {
                    preLoginUI.run(command, parameters);
                }
                else if (clientDB.currentState == State.LoggedIn) {
                    postLoginUI.run(command, parameters);
                }
                else if (clientDB.currentState == State.InGame || clientDB.currentState == State.Observing) {
                    inGameUI.run(command, parameters);
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("Error: Not a number");
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
                System.out.println("Error");
            }
            finally {
                cleanUp();
            }
        }
    }

    private void cleanUp() {
        try {
            header.clear();
            body.clear();
            parameters.clear();
            if ((!command.equalsIgnoreCase("quit") && !command.equalsIgnoreCase("exit"))
                    || clientDB.currentState != State.LoggedOut) {
                parameters = getInput();
                command = parameters.get(0).toLowerCase();
                parameters.remove(0);
            }
        }
        catch (IndexOutOfBoundsException ex) {
            System.out.println("Please input properly formatted commands. Print help for more information.");
        }
    }

    private ArrayList<String> getInput(){
        String line = input.nextLine();
        ArrayList<String> output = new ArrayList<>((List.of(line.split(" "))));
        for (int i=0;i < output.size();i++) {
            output.set(i, output.get(i));
        }
        return output;
    }

}
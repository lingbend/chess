package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacadeLocal {

    public State currentState;
    public String username;
    public int authToken;
    public String currentGameID;
    public ArrayList<GameData> existingGames;
    public GameData currentGame;
    public String baseUri;
    public HttpURLConnection connection;
    public TreeMap<String, String> header;
    public TreeMap<String, String> body;
    public Scanner input;
    public int port;
    public String command;
    public ArrayList<String> parameters;

    public enum State {
        LoggedOut,
        LoggedIn,
        InGame,
        Observing
    }

    public ServerFacadeLocal(int portNum) throws URISyntaxException {
        currentState = State.LoggedOut;
        authToken = 0;
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
        baseUri = "http://localhost:";
        header = new TreeMap<>();
        body = new TreeMap<>();
        input = new Scanner(System.in);
        port = portNum;
    }

    public void run() throws Exception {
        baseUri += String.valueOf(port);
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
                if (command.equalsIgnoreCase("help")) {
                    System.out.print(getHelp());
                }
                else if (command.equalsIgnoreCase("login")) {
                    checkState(State.LoggedOut, 2);
                    System.out.println("Logging in...");
                    login(parameters.get(0), parameters.get(1));
                }
                else if (command.equalsIgnoreCase("register")) {
                    checkState(State.LoggedOut, 3);
                    System.out.println("Registering user...");
                    register(parameters.get(0), parameters.get(1), parameters.get(2));
                }
                else if (command.equalsIgnoreCase("logout")) {
                    checkState(State.LoggedIn, 0);
                    System.out.println("Logging out...");
                    logout();
                }
                else if (command.equalsIgnoreCase("create")) {
                    checkState(State.LoggedIn, 1);
                    System.out.println("Creating game...");
                    createGame(parameters.get(0));
                }
                else if (command.equalsIgnoreCase("join")) {
                    checkState(State.LoggedIn, 2);
                    System.out.println("Joining game...");
                    if (existingGames.isEmpty()) {
                        throw new Exception("Game not found. Use 'list' to find games and 'create' if none exist");
                    }
                    playGame(Integer.parseInt(parameters.get(0)),
                            Enum.valueOf(ChessGame.TeamColor.class, parameters.get(1).toUpperCase()));
                }
                else if (command.equalsIgnoreCase("observe")) {
                    checkState(State.LoggedIn, 1);
                    System.out.println("Loading game as observer...");
                    observeGame(Integer.parseInt(parameters.get(0)));
                }
                else if (command.equalsIgnoreCase("list")) {
                    checkState(State.LoggedIn, 0);
                    System.out.println("Retrieving current games...");
                    listGames();
                }
                else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")) {
                    if (currentState != State.LoggedOut) {
                        throw new Exception("Logout before quitting");
                    }
                    break;
                }
                else if (command.equals("")) {}
                else {
                    System.out.println("Error: Not a valid input");
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

    private void checkState(State state, int size) throws Exception {
        if (state == State.LoggedOut && currentState != state) {
            throw new Exception("Must logout first");
        }
        else if (state == State.LoggedIn && currentState == State.LoggedOut) {
            throw new Exception("Not logged in. Login first");
        }
        else if (parameters.size() != size) {
            throw new FacadeException("Error: Wrong number of inputs");
        }
    }

    private void cleanUp() {
        try {
            header.clear();
            body.clear();
            parameters.clear();
            if ((!command.equalsIgnoreCase("quit") && !command.equalsIgnoreCase("exit"))
                    || currentState != State.LoggedOut) {
                parameters = getInput();
                command = parameters.get(0);
                parameters.remove(0);
            }
        }
        catch (IndexOutOfBoundsException ex) {
            System.out.println("Please input properly formatted commands. Print help for more information.");
        }
    }

    private String getHelp() {
        if (currentState == State.LoggedOut) {
            return
                """
                Available Commands:              Format:
                Log in to Chess System           | login username password
                Register a new account for Chess | register username password email
                Close the Chess program          | quit OR exit
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
        this.username = username;
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
        this.username = username;
    }

    public void logout() throws Exception {
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap();
        getConnection("/session", "DELETE");
        getResponse();
        currentState = State.LoggedOut;
        System.out.print("\u001b[95m");
        System.out.println("...Logged out successfully");
        this.username = "";
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
        System.out.println("...Created list of games successfully");
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

    public void playGame(int gameNumber, ChessGame.TeamColor color) throws Exception {
        String id;

        try {
            id = String.valueOf(existingGames.get(gameNumber - 1).getGameID());
        }
        catch (IndexOutOfBoundsException ex) {
            throw new FacadeException("Error: Game doesn't exist. Choose a different number or use list to load games");
        }
        header = new TreeMap(Map.of("authToken", String.valueOf(authToken)));
        body = new TreeMap(Map.of("playerColor", color, "gameID", id));
        getConnection("/game", "PUT");
        getResponse();

        if (color == ChessGame.TeamColor.WHITE) {
            existingGames.get(gameNumber - 1).setWhiteUsername(username);
        }
        else {
            existingGames.get(gameNumber - 1).setBlackUsername(username);
        }
        currentGameID = id;
        currentGame = existingGames.get(gameNumber - 1);
        currentState = State.InGame;

        System.out.printf("...Joined game %s\n", currentGame.getGameName());
        System.out.print(drawBoard("107", "40", "35", "34",
                new ChessGame()));
        System.out.print("\u001b[34;49m");
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
        System.out.print(drawBoard("107", "40", "35", "34",
                new ChessGame()));
        System.out.print("\u001b[34;49m");
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

    private String drawBoard(String backColor1, String backColor2, String frontColor1,
                           String frontColor2, ChessGame chess) throws Exception {
        ChessBoard board = chess.getBoard();
        String currentBackColor = backColor2;
        String currentFrontColor = "";
        StringBuilder result = new StringBuilder();
        ChessGame.TeamColor startingColor;
        String em = "\u2004".repeat(3) + "\u2006" + "\u2009".repeat(3);
        StringBuilder alpha = new StringBuilder("    " + "a" + em + "b" + em + "c" +
                em + "d" + em + "e" + em + "f" + em + "g" + em + "h" + "    ");
        StringBuilder beta = new StringBuilder(alpha).reverse();

        if (username.equals(currentGame.getWhiteUsername()) || currentState == State.Observing) {
            startingColor = ChessGame.TeamColor.WHITE;
            result.append("\u001b[39m").append(alpha).append(EscapeSequences.EMPTY).append("\n");
        }
        else {
            startingColor = ChessGame.TeamColor.BLACK;
            result.append("\u001b[39m").append(beta).append(EscapeSequences.EMPTY).append("\n");
        }

        for (int row=1; row < 9; row++) {
            if (startingColor == ChessGame.TeamColor.WHITE) {
                result.append(" ").append(9-row).append(" ");
            }
            else {
                result.append(" ").append(row).append(" ");
            }
            for (int col=1; col<9; col++) {
                if (startingColor == ChessGame.TeamColor.WHITE) {
                    currentFrontColor = getPieceColor(9-row, col, board, frontColor1, frontColor2);
                    result.append("\u001b[").append(currentBackColor).append(";").append(currentFrontColor)
                            .append("m").append(getPieceCode(9-row, col, board));
                }
                else {
                    currentFrontColor = getPieceColor(row, 9-col, board, frontColor1, frontColor2);
                    result.append("\u001b[").append(currentBackColor).append(";").append(currentFrontColor).
                            append("m").append(getPieceCode(row, 9-col, board));
                }
                if (currentBackColor.equals(backColor1)) {
                    currentBackColor = backColor2;
                }
                else {
                    currentBackColor = backColor1;
                }
            }
            result.append("\u001b[49;39m");
            if (startingColor == ChessGame.TeamColor.WHITE) {
                result.append(" ").append(9-row).append(" ");
            }
            else {
                result.append(" ").append(row).append(" ");
            }
            result.append("\n");
            if (currentBackColor.equals(backColor1)) {
                currentBackColor = backColor2;
            }
            else {
                currentBackColor = backColor1;
            }
        }
        if (username.equals(currentGame.getBlackUsername()) && currentState != State.Observing) {
            result.append(beta).append(EscapeSequences.EMPTY).append("\n");
        }
        else {
            result.append(alpha).append(EscapeSequences.EMPTY).append("\n");
        }
        return result.toString();
    }

    private String getPieceColor(int row, int col, ChessBoard board, String whiteColor, String blackColor) {
        ChessPiece piece = board.getPiece(row, col);
        if (piece == null || piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return whiteColor;
        }
        else {
            return blackColor;
        }
    }

    private String getPieceCode(int row, int col, ChessBoard board) throws Exception{
        ChessPiece piece = board.getPiece(row, col);

        if (piece == null) {
            return EscapeSequences.EMPTY;
        }

        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();

        EscapeSequences escapeSequences = new EscapeSequences();
        Field chessField = escapeSequences.getClass().getDeclaredField((color.toString() + "_" + type.toString()));
        chessField.setAccessible(true);
        Object obj2 = chessField.get(escapeSequences);

        return obj2.toString();
    }
}
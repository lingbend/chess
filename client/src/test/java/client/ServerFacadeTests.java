package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handler.JsonHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import server.Server;
import service.ClearService;
import service.RequestObj;
import ui.FacadeException;
import ui.ServerFacadeLocal;

import java.util.Map;
import java.util.Scanner;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacadeLocal facade;
    private static int port;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacadeLocal(port);
    }

    @BeforeEach
    public void clear() throws Exception{
        DatabaseManager.initializeEntireDatabase();
        facade = new ServerFacadeLocal(port);
        facade.clientDB.baseUri += String.valueOf(port);
    }

    @AfterEach
    public void fixDB() throws DataAccessException {
        ClearService service = new ClearService();
        var request = new RequestObj(Map.of());
        JsonHandler jsonHandler = new JsonHandler(service);
        service.registerHandler(jsonHandler);
        service.run(request);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerNegative(){
        try {
            facade.preLoginUI.register("frog", "cow", "pigeon@pigeon.com");
            facade.preLoginUI.register("frog", "cow", "pigeon@pigeon.com");
            Assertions.fail("Didn't detect duplicate registry");
        }
        catch (FacadeException ex) {
            Assertions.assertTrue(ex.getMessage() != null);
        }
        catch (Exception ex) {
            Assertions.fail("Wrong exception for register negative" + ex.getMessage());
        }
    }

    @Test
    public void registerPositive(){
        try {
            facade.preLoginUI.register("frog", "234ksdfjo", "pigeon@gmail.com");
            Assertions.assertEquals("frog", facade.clientDB.username);
            Assertions.assertTrue(facade.clientDB.authToken > 1);
            Assertions.assertEquals(ServerFacadeLocal.State.LoggedIn, facade.clientDB.currentState);

        }
        catch (Exception ex) {
            Assertions.fail("Registering failed." + ex.getMessage());
        }
    }

    @Test
    public void loginNegative() {
        try {
            facade.preLoginUI.login("piggy", "wiggy");
            Assertions.fail("loginNegative didn't throw an error");
        }
        catch (FacadeException ex){
            Assertions.assertNotNull(ex.getMessage());
        }
        catch (Exception ex) {
            Assertions.fail("loginNegative, wrong error" + ex.getMessage());
        }

    }

    @Test
    public void loginPositive() {
        try {
            facade.preLoginUI.register("dogman", "123123kdfs", "figment@imagination.org");
            facade.postLoginUI.logout();
            facade.preLoginUI.login("dogman", "123123kdfs");
            Assertions.assertEquals("dogman", facade.clientDB.username);
            Assertions.assertTrue(facade.clientDB.authToken > 1);
            Assertions.assertEquals(ServerFacadeLocal.State.LoggedIn, facade.clientDB.currentState);

        }
        catch (Exception ex) {
            Assertions.fail("loginPositive threw error: " + ex.getMessage());
        }
    }

    @Test
    public void logoutNegative() {
        try {
            facade.postLoginUI.logout();
            Assertions.fail("logoutNegative failed to produce Exception");
        }
        catch (Exception ex) {
            Assertions.assertNotNull(ex.getMessage());
        }
    }

    @Test
    public void logoutPositive(){
        try {
            facade.preLoginUI.register("pig", "43*dUU", "doris@banana.co.farming");
            facade.postLoginUI.logout();
            Assertions.assertTrue(facade.clientDB.username.equals(""));
            Assertions.assertEquals(ServerFacadeLocal.State.LoggedOut, facade.clientDB.currentState);

        }
        catch (Exception ex) {
            Assertions.fail("logoutPositive failed" + ex.getMessage());
        }
    }

    @Test
    public void createGameNegative(){
        try {
            facade.postLoginUI.createGame("pig");
            Assertions.fail("creatGameNegative made a game without auth token");
        }
        catch (Exception ex) {
            try {
                facade.postLoginUI.createGame(null);
                Assertions.fail("createGameNegative created a game with null name");
            }
            catch (Exception ex2) {
                Assertions.assertTrue(ex != null && ex2 != null);
            }
        }
    }

    @Test
    public void createGamePositive() {
        try {
            facade.preLoginUI.register("i", "j", "g@g.c");
            String id = facade.postLoginUI.createGame("porkrind");
            Assertions.assertTrue(id.getClass() == String.class && id.length() > 0);
        }
        catch (Exception ex) {
            Assertions.fail("createGamePositive failed to create game" + ex.getMessage());
        }
    }

    @Test
    public void listGamesNegative() {
        try {
            facade.postLoginUI.listGames();
            Assertions.fail("listGamesNegative listed games without auth");
        }
        catch (Exception ex) {
            Assertions.assertNotNull(ex.getMessage());
        }
    }

    @Test
    public void listGamesPositive() {
        try {
            facade.preLoginUI.register("chuck", "wagon", "innards@gizzards.note");
            facade.postLoginUI.createGame("barbeque");
            facade.postLoginUI.listGames();
            Assertions.assertTrue(facade.clientDB.existingGames.size() == 1);
            Assertions.assertTrue(facade.clientDB.existingGames.get(0).getGameName().equals("barbeque"));
        }
        catch (Exception ex) {
            Assertions.fail("listGamesPositive threw an error" + ex.getMessage());
        }
    }

    @Test
    public void playGameNegative() {
        try {
            facade.postLoginUI.playGame(0, ChessGame.TeamColor.BLACK);
            Assertions.fail("playGameNegative joined a non-existent game");
        }
        catch (FacadeException ex) {
            Assertions.assertNotNull(ex.getMessage());
            try {
                facade.preLoginUI.register("b", "c", "d");
                facade.postLoginUI.createGame("Nef's wonderful game of life");
                facade.postLoginUI.listGames();
                facade.postLoginUI.logout();
                facade.postLoginUI.playGame(0, ChessGame.TeamColor.WHITE);
                Assertions.fail("playGameNegative joined a game without auth" + ex.getMessage());
            }
            catch (Exception ex2) {
                Assertions.assertNotNull(ex2.getMessage());
            }
        }
        catch (Exception ex) {
            Assertions.fail("playGameNegative threw wrong error" + ex.getMessage());
        }
    }

    @Test
    public void playGamePositive() {
        try {
            facade.preLoginUI.register("b", "c", "d");
            facade.postLoginUI.createGame("Nef's wonderful game of life");
            facade.postLoginUI.listGames();
            facade.postLoginUI.playGame(1, ChessGame.TeamColor.WHITE);
            Assertions.assertTrue(facade.clientDB.existingGames.get(0).getWhiteUsername().equals("b"));
            Assertions.assertEquals(ServerFacadeLocal.State.InGame, facade.clientDB.currentState);
        }
        catch (Exception ex) {
            Assertions.fail("playGamePositive threw an Exception " + ex.getMessage());
        }
    }

    @Test
    public void observeGameNegative() {
        try {
            facade.postLoginUI.observeGame(0);
            Assertions.fail("observeGameNegative joined a non-existant game");
        }
        catch (FacadeException ex) {
            try {
                facade.preLoginUI.register("gimt", "qlkjfo","3@4.d");
                facade.postLoginUI.createGame("hogie");
                facade.postLoginUI.observeGame(1);
                Assertions.fail("observeGameNegative joined a non-existant game");
            }
            catch (Exception ex2) {
                Assertions.assertNotNull(ex2.getMessage());
                Assertions.assertNotNull(ex.getMessage());
                Assertions.assertNotEquals(ServerFacadeLocal.State.Observing, facade.clientDB.currentState);
            }
        }
        catch (Exception ex) {
            Assertions.fail("observeGameNegative threw wrong exception " + ex.getMessage());
        }
    }

    @Test
    public void observeGamePositive() {
        try {
            facade.preLoginUI.register("sda ", "df3333","3@4.fffd");
            facade.postLoginUI.createGame("hillbilly");
            facade.postLoginUI.listGames();
            facade.postLoginUI.observeGame(1);
            Assertions.assertEquals(ServerFacadeLocal.State.Observing, facade.clientDB.currentState);
            Assertions.assertNotNull(facade.clientDB.currentGameID);
        }
        catch (Exception ex) {
            Assertions.fail("observeGamePositive threw an exception " + ex.getMessage());
        }
    }

    @Test
    public void runPositive() {
        facade.clientDB.baseUri = "localhost:";
        try {
            facade.input = new Scanner(
                    """
                    register tony 23939 email@email.com
                    creaTe billy
                    liSt
                    joIn 1 WhiTe
                    leave
                    create billy
                    OBSERVE 1
                    leave
                    logout
                    login tony 23939
                    help
                    logout
                    help
                    quiT
                    """);
            facade.run();
            Assertions.assertEquals("", facade.clientDB.username);
            Assertions.assertEquals(ServerFacadeLocal.State.LoggedOut, facade.clientDB.currentState);
            System.out.println(facade.clientDB.existingGames.size());
            Assertions.assertTrue(facade.clientDB.existingGames.size() == 1);
        }
        catch (Exception ex) {
            Assertions.fail("runPositive threw an error " + ex.getMessage());
        }

    }

    @Test
    public void runNegative() {
        try {
            facade.input = new Scanner(
                    """
                    register tony 2393h9 email@email.com
                    quiT
                    """);
            facade.clientDB.baseUri = "chicken";
            facade.run();
            Assertions.assertNotEquals("tony", facade.clientDB.username);
            Assertions.assertTrue(facade.clientDB.authToken == 0);
            Assertions.assertEquals(ServerFacadeLocal.State.LoggedOut, facade.clientDB.currentState);
        }
        catch (Exception ex) {
            Assertions.assertNotNull(ex.getMessage());
            try {
                facade.clientDB.baseUri = "http://localhost:";
                facade.input = new Scanner("""
                    register tony
                    creaTe
                    liSt frodo
                    joIn 1 WhiTe etc
                    OBSERVE 10
                    logout
                    login tony 23939
                    logout
                    quiT
                    """);
                Assertions.assertNotEquals("tony", facade.clientDB.username);
                Assertions.assertTrue(facade.clientDB.existingGames == null || facade.clientDB.existingGames.isEmpty());
                Assertions.assertTrue(facade.clientDB.authToken == 0);
                Assertions.assertEquals(ServerFacadeLocal.State.LoggedOut, facade.clientDB.currentState);
            }
            catch (Exception ex2) {
                Assertions.fail("runNegative threw an unexpected Exception " + ex2.getMessage());
            }
        }
    }
}

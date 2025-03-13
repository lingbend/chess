package service;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import dataaccess.DB;


import handler.JsonHandler;
import dataaccess.DataAccessException;

public class ServiceTests {

    @BeforeEach
    public void initialize() throws DataAccessException{
        JsonHandler.initializeDB();
    }


    @Test
    public void registerPositive() throws DataAccessException{
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));
        var requestMap = Map.of("username", "chicken", "code", "200");
        Assertions.assertNotNull(result.get("authToken"));
        result.remove("authToken");
        Assertions.assertEquals(requestMap, result, "Register positive failed");
    }

    @Test
    public void registerNegative() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        try {
            service.run(request);
            Assertions.fail("Failed register negative (bad request)");
        }
        catch (DataAccessException ex){
            Assertions.assertEquals("bad request", ex.getMessage(), "Bad request: assertion failed");
        }

        service = new RegisterService();
        request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        handler = new JsonHandler(service);
        service.registerHandler(handler);
        service.run(request);
        try {
            service.run(request);
            Assertions.fail("Failed register negative (already taken)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("already taken", ex.getMessage(), "Already taken: assertion failed");
        }
    }

    @Test
    public void loginPositive() throws DataAccessException {
        makeUser();
        var service = new LoginService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));
        Assertions.assertNotNull(result.get("authToken"));
        result.remove("authToken");
        var requestMap = Map.of("username", "chicken", "code", "200");
        Assertions.assertEquals(requestMap, result, "Login Positive Failed");
    }

    @Test
    public void loginNegative() throws DataAccessException {
        makeUser();
        var service = new LoginService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"forge"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        try {
            service.run(request);
            Assertions.fail("Failed login negative");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
    }

    @Test
    public void logoutPositive() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new LogoutService();
        request = new RequestObj(Map.of("authToken", result.get("authToken")));
        handler = new JsonHandler(service2);
        service2.registerHandler(handler);
        result = unserialize(service2.run(request));
        Map requestMap = Map.of("code", "200");
        Assertions.assertEquals(requestMap, result);
    }

    @Test
    public void logoutNegative() throws DataAccessException {
        var service2 = new LogoutService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service2);
        service2.registerHandler(handler);
        try {
            service2.run(request);
            Assertions.fail("Failed logout negative");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new MakeGameService();
        request = new RequestObj(Map.of("gameName", "Stinkor", "authToken", result.get("authToken")));
        service2.registerHandler(handler);
        result = unserialize(service2.run(request));

        Assertions.assertNotNull(result.get("gameID"));
        result.remove("gameID");

        Map requestMap = Map.of("code", "200");
        Assertions.assertEquals(requestMap, result, "Create Game positive failed");
    }

    @Test
    public void createGameNegative() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new MakeGameService();
        request = new RequestObj(Map.of("authToken", result.get("authToken")));
        service2.registerHandler(handler);
        try {
            service2.run(request);
            Assertions.fail("create Game Negative failed (bad request)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("bad request", ex.getMessage());
        }

        request = new RequestObj(Map.of("gameName", "Stinkor", "authToken", "als"));
        try {
            service2.run(request);
            Assertions.fail("create Game Negative failed (unauthorized)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
    }

    @Test
    public void joinGamePositive() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new MakeGameService();
        request = new RequestObj(Map.of("gameName", "Stinkor", "authToken", result.get("authToken")));
        service2.registerHandler(handler);
        var authToken = result.get("authToken");
        result = unserialize(service2.run(request));

        request = new RequestObj(Map.of("playerColor", "WHITE", "gameID", result.get("gameID"), "authToken", authToken));
        var service3 = new JoinGameService();
        service3.registerHandler(handler);
        result = unserialize(service3.run(request));

        var requestMap = Map.of("code", "200");
        Assertions.assertEquals(requestMap, result);
    }

    @Test
    public void joinGameNegative() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new MakeGameService();
        request = new RequestObj(Map.of("gameName", "Stinkor", "authToken", result.get("authToken")));
        service2.registerHandler(handler);
        var authToken = result.get("authToken");
        result = unserialize(service2.run(request));

        request = new RequestObj(Map.of("gameID", result.get("gameID"), "authToken", authToken));
        var service3 = new JoinGameService();
        service3.registerHandler(handler);
        try {
            service3.run(request);
            Assertions.fail("join game Negative failed (bad request)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("bad request", ex.getMessage());
        }
        request = new RequestObj(Map.of("playerColor", "WHITE", "gameID", result.get("gameID"), "authToken", "14532"));
        try {
            service3.run(request);
            Assertions.fail("join game Negative failed (unauthorized)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
        request = new RequestObj(Map.of("playerColor","WHITE", "gameID", result.get("gameID"), "authToken", authToken));
        service3.run(request);
        try {
            service3.run(request);
            Assertions.fail("join game Negative failed (already taken)");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("already taken", ex.getMessage());
        }
    }

    @Test
    public void findGamesServicePositive() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        String gameID = (String) makeGame();

        var service2 = new FindGamesService();
        service2.registerHandler(handler);
        request = new RequestObj(Map.of("authToken", result.get("authToken")));
        result = unserialize(service2.run(request));
        Assertions.assertEquals("200", result.get("code"));
        result.remove("code");

        ArrayList<Map> gameList = (ArrayList<Map>) result.get("games");
        Map games = gameList.get(0);

        Map keyMap = Map.of("gameID", gameID, "gameName", "Stinkor");

        for (var i : games.entrySet()) {
            Map.Entry j = (Map.Entry) i;
            Assertions.assertTrue(games.containsKey(j.getKey()));
            Assertions.assertTrue(j.getValue() == games.get(j.getKey()));
        }
    }

    @Test
    public void findGamesNegative() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        String gameID = (String) makeGame();

        var service2 = new FindGamesService();
        service2.registerHandler(handler);
        request = new RequestObj(Map.of());
        try {
            service2.run(request);
            Assertions.fail("findGame negative failed");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
    }

    @Test
    public void clearPositive() throws DataAccessException{
        makeUser();
        makeGame();
        var service = new ClearService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));
        Map requestMap = Map.of("code", "200");
        Assertions.assertEquals(requestMap, result);
        Assertions.assertEquals(new ArrayList<>(), DB.games);
        Assertions.assertEquals(new ArrayList<>(), DB.auth);
        Assertions.assertEquals(new ArrayList<>(), DB.users);
    }

    private Map unserialize(String[] json) {
        TreeMap<String, String> requestMap = new TreeMap();
        if (json != null) {
            requestMap = new Gson().fromJson(json[1], TreeMap.class);
        }
        requestMap.put("code", json[0]);
        return requestMap;
    }

    private void makeUser() throws DataAccessException{
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3", "email", "duck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        service.run(request);
    }

    private Object makeGame() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "bob", "password" ,"asd333gfs%3", "email", "d4uck@duck.com"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        Map result = unserialize(service.run(request));

        var service2 = new MakeGameService();
        request = new RequestObj(Map.of("gameName", "Stinkor", "authToken", result.get("authToken")));
        service2.registerHandler(handler);
        result = unserialize(service2.run(request));
        return result.get("gameID");
    }

    @AfterEach
    public void clearDB() throws DataAccessException {
        var service = new ClearService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        service.run(request);
    }



}

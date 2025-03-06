package service;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.TreeMap;
import com.google.gson.Gson;
import java.lang.reflect.Executable;


import handler.JsonHandler;
import dataAccess.DataAccessException;

public class ServiceTests {

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
    public void registerNegative() throws DataAccessException, Exception {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        try {
            service.run(request);
            throw new Exception("Failed register negative (bad request)");
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
            throw new Exception("Failed register negative (already taken)");
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
    public void loginNegative() throws DataAccessException, Exception {
        makeUser();
        var service = new LoginService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"forge"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        try {
            service.run(request);
            throw new Exception("Failed login negative");
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
    public void logoutNegative() throws DataAccessException, Exception {
        var service2 = new LogoutService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service2);
        service2.registerHandler(handler);
        try {
            service2.run(request);
            throw new Exception("Failed logout negative");
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("unauthorized", ex.getMessage());
        }
    }

    @Test
    public void clearPositive() throws DataAccessException{
        var service = new ClearService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        service.run(request);

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



}

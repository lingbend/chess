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
    public void registerNegative() throws DataAccessException {
        var service = new RegisterService();
        var request = new RequestObj(Map.of("username", "chicken", "password" ,"aslk3sd0%3"));
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        try {
            service.run(request);
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
        }
        catch (DataAccessException ex) {
            Assertions.assertEquals("already taken", ex.getMessage(), "Already taken: assertion failed");
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



}

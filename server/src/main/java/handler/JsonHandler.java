package handler;
import service.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.TreeMap;
import dataaccess.*;

public class JsonHandler implements Handler{

    Service service;

    public JsonHandler(Service serviceType){
        service = serviceType;
    }

    public String[] deserialize(String json, String auth) {
        try {
            initializeDB();

            TreeMap<String, String> requestMap = new TreeMap();
            if (json != null && !json.isEmpty()) {
                requestMap = new Gson().fromJson(json, TreeMap.class);
            }
            if (auth != null) {
                requestMap.put("authToken", auth);
            }
            var request = new RequestObj(requestMap);
            return service.run(request);
        }
        catch (DataAccessException ex) {
            String code = "500";
            if (ex.getMessage().equals("already taken")) {
                code = "403";
            }
            else if (ex.getMessage().equals("bad request")){
                code = "400";
            }
            else if (ex.getMessage().equals("unauthorized")){
                code = "401";
            }
            var result = new ResultObj(Map.of("code", code, "message", "Error: " + ex.getMessage()));
            return serialize(result);
        }
    }

    public String[] serialize(ResultObj obj) {
        String code = obj.getCode();
        obj.setCode(null);
        String json;
        if (code != null) {
            json = new Gson().toJson(obj);
        }
        else {
            json = "";
        }
        String[] response = {code, json};
        return response;
    }

    public static void initializeDB() throws DataAccessException{
//        new DB();
        DatabaseManager.initializeEntireDatabase();
    }
}

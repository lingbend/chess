package handler;
import service.*;
import com.google.gson.Gson;
import java.util.Map;
import dataAccess.*;
import java.util.Arrays;

public class JsonHandler implements Handler{

    Service service;

    public JsonHandler(Service serviceType){
        service = serviceType;
    }

    public String[] Deserialize(String json) {
        try {
            Map<String, String> requestMap = null;
            if (json != null) {
                requestMap = new Gson().fromJson(json, Map.class);
            }
            var request = new RequestObj(requestMap);
            return service.run(request);
        }
        catch (DataAccessException ex) {
            if (ex.getMessage().equals("already taken")) {
                var result = new ResultObj(Map.of("code", "403", "message", ex.getMessage()));
                return Serialize(result);
            }
            else if (ex.getMessage().equals("unable to create user")) {
                var result = new ResultObj(Map.of("code", "500", "message", ex.getMessage()));
                return Serialize(result);
            }
            else if (ex.getMessage().equals("unable to store authToken")) {
                var result = new ResultObj(Map.of("code", "500", "message", ex.getMessage()));
                return Serialize(result);
            }
            else if (ex.getMessage().equals("bad request")){
                var result = new ResultObj(Map.of("code", "400", "message", ex.getMessage()));
                return Serialize(result);
            }
            else {
                var result = new ResultObj(Map.of("code", "500", "message", ex.getMessage()));
                return Serialize(result);
            }
        }
    }

    public String[] Serialize(ResultObj obj) {
        String code = obj.GetCode();
        obj.SetCode(null);
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
}

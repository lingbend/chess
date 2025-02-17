package handler;
import service.*;
import com.google.gson.Gson;
import java.util.Map;
import dataAccess.*;

public class JsonHandler implements Handler{

    Service service;

    public JsonHandler(Service serviceType){
        service = serviceType;
    }

    public void Deserialize(String json) {
        try {
            Map requestMap = new Gson().fromJson(json, Map.class);
            var request = new RequestObj(requestMap);
            service.run(request);
        }
        catch (DataAccessException ex) {
            if (ex.getMessage().equals("already taken")) {
                var result = new ResultObj(Map.of("code", "403", "message", ex.getMessage()));
                Serialize(result);
            }
            else if (ex.getMessage().equals("unable to create user")) {
                var result = new ResultObj(Map.of("code", "500", "message", ex.getMessage()));
                Serialize(result);
            }
            else if (ex.getMessage().equals("unable to store authToken")) {
                var result = new ResultObj(Map.of("code", "500", "message", ex.getMessage()));
                Serialize(result);
            }
            else {
                var result = new ResultObj(Map.of("code", "400", "message", ex.getMessage()));
                Serialize(result);
            }
        }
    }

    public String Serialize(ResultObj obj) {
        String json = new Gson().toJson(obj);
        return json;
    }
}

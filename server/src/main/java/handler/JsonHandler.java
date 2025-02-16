package handler;
import service.*;
import com.google.gson.Gson;
import java.util.Map;

public class JsonHandler implements Handler{

    Service service;

    public JsonHandler(Service serviceType){
        service = serviceType;
    }

    public void Deserialize(String json) {
        Map requestMap = new Gson().fromJson(json, Map.class);
        var request = new RequestObj(requestMap);
        service.run(request);
    }

    public String Serialize(ResultObj obj) {
        String json = new Gson().toJson(obj);
        return json;
    }
}

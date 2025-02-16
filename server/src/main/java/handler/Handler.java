package handler;

import service.Service;
import com.google.gson.Gson;
import service.*;

public interface Handler {
    void Deserialize(String json);
    String Serialize(ResultObj obj);
}



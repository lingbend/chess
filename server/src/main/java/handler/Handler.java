package handler;

import service.Service;
import com.google.gson.Gson;
import service.*;

public interface Handler {
    String[] Deserialize(String json);
    String[] Serialize(ResultObj obj);
}



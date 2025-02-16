package handler;

import service.Service;
import com.google.gson.Gson;

public interface Handler {
    void Deserialize(String json);
    Object Serialize(String obj);
}



package handler;

import service.Service;
import com.google.gson.Gson;

public interface Handler {
    void Deserialize(Object json);
    Object Serialize(Object obj);
}



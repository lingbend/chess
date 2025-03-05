package handler;

import service.*;

public interface Handler {
    String[] Deserialize(String json, String auth);
    String[] Serialize(ResultObj obj);
}



package handler;

import service.*;

public interface Handler {
    String[] deserialize(String json, String auth);
    String[] serialize(ResultObj obj);
}



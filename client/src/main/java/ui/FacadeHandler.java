package ui;

import com.google.gson.Gson;

import java.util.TreeMap;

public class FacadeHandler {


    public FacadeHandler() {}

    public TreeMap<String, String> deserialize(String json) {

        TreeMap<String, String> requestMap = new TreeMap();
        if (json != null && !json.isEmpty()) {
            requestMap = new Gson().fromJson(json, TreeMap.class);
        }

        return requestMap;
    }

    public String serialize(TreeMap<String, ?> request) {
        String json;
        json = new Gson().toJson(request);
        return json;
    }

}

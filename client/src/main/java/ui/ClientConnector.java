package ui;

import com.google.gson.Gson;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.TreeMap;

public class ClientConnector {

    public HttpURLConnection connection;
    public TreeMap<String, String> header;
    public TreeMap<String, String> body;
    private ClientStorage clientDB;

    public ClientConnector(TreeMap<String, String> header,TreeMap<String, String> body, ClientStorage clientDB){
        this.header = header;
        this.body = body;
        this.clientDB = clientDB;
    }


    public void getConnection(String path, String method) throws Exception {
        HttpURLConnection newConnection = (HttpURLConnection) (new URI("http://" + clientDB.baseUri + path)).toURL().openConnection();
        newConnection.setRequestMethod(method);
        newConnection.setDoOutput(true);
        newConnection.setDoInput(true);
        for (var entry : header.entrySet()) {
            newConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }

        if (!body.isEmpty()) {
            var outStream = newConnection.getOutputStream();
            String json = new Gson().toJson(body);
            outStream.write(json.getBytes());
            outStream.close();
        }

        newConnection.connect();
        connection = newConnection;
    }

    public TreeMap getResponse() throws Exception {
        int responseCode = connection.getResponseCode();
        TreeMap responseMap = getResponseHelper();

        responseMap.put("code", Integer.toString(responseCode));

        return responseMap;
    }

    private TreeMap getResponseHelper() throws IOException, FacadeException {
        int responseCode = connection.getResponseCode();
        InputStream responseStream;
        if (responseCode != 200) {
            responseStream = connection.getErrorStream();
        }
        else {
            responseStream = connection.getInputStream();
        }
        InputStreamReader reader = new InputStreamReader(responseStream);

        TreeMap responseMap = new Gson().fromJson(reader, TreeMap.class);

        reader.close();
        responseStream.close();
        if (responseCode != 200) {
            throw new FacadeException((String) responseMap.get("message"));
        }
        return responseMap;
    }

    public ArrayList<GameData> getArrayResponse() throws Exception {
        TreeMap responseMap = getResponseHelper();

        ArrayList rawArray = (ArrayList) responseMap.get("games");
        ArrayList<GameData> arrayResponse = new ArrayList<>();

        for (var i : rawArray) {
            arrayResponse.add(new Gson().fromJson((new Gson().toJson(i)), GameData.class));
        }

        return arrayResponse;
    }

}

package ui;

import model.GameData;
import java.util.ArrayList;


public class ClientStorage {
    public ServerFacadeLocal.State currentState;
    public String username;
    public int authToken;
    public String currentGameID;
    public String baseUri;
    public ArrayList<GameData> existingGames;
    public GameData currentGame;
    public GameDrawer drawer;

    public ClientStorage() {
        currentState = ServerFacadeLocal.State.LoggedOut;
        authToken = 0;
        username = "";
        currentGameID = "";
        existingGames = new ArrayList<GameData>();
    }

}

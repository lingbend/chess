package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class GameAccess implements dataAccess {

    public GameAccess(){}

    public boolean Create(Object obj){
        GameData data = (GameData) obj;
        return DB.games.add(data);
    };
    public boolean Find(Object index){
        String gameID = (String) index;
        for (var i : DB.games) {
            if (String.valueOf(i.GetGameID()).equals(gameID)) {
                return true;
            }
        }
        return false;
    };
    public ArrayList<GameData> FindAll(){
        return DB.games;
    }
    public boolean Update(Object index){
        var game = (GameData) index;
        var gameID = game.GetGameID();
        var oldGame = Read(String.valueOf(gameID));
        if (DB.games.remove(oldGame) && DB.games.add(game)) {
            return true;
        }
        return false;
    };
    public boolean Delete(Object index){
        return false;
    };
    public boolean Match(Object index){
        return false;
    };
    public Object Read(String gameID){
        for (var i : DB.games) {
            if (String.valueOf(i.GetGameID()).equals(gameID)) {
                return i;
            }
        }
        return null;
    };
    public boolean DeleteAll(){
        DB.games = new ArrayList<>();
        return true;
    }

}

package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GameAccess implements DataAccess {

    public GameAccess(){}

    public boolean create(Object obj){
        GameData data = (GameData) obj;
        return DB.games.add(data);
    };
    public boolean find(Object index){
        String gameID = (String) index;
        for (var i : DB.games) {
            if (String.valueOf(i.getGameID()).equals(gameID)) {
                return true;
            }
        }
        return false;
    };
    public ArrayList<GameData> findAll(){
        return DB.games;
    }
    public boolean update(Object index){
        var game = (GameData) index;
        var gameID = game.getGameID();
        var oldGame = read(String.valueOf(gameID));
        if (DB.games.remove(oldGame) && DB.games.add(game)) {
            return true;
        }
        return false;
    };
    public boolean delete(Object index){
        return false;
    };
    public Object read(String gameID){
        for (var i : DB.games) {
            if (String.valueOf(i.getGameID()).equals(gameID)) {
                return i;
            }
        }
        return null;
    };
    public boolean deleteAll(){
        DB.games = new ArrayList<>();
        return true;
    }

}

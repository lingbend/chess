package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GameAccess implements DataAccess, GameAccessInter {

    public GameAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        GameData data = (GameData) obj;
        return DB.games.add(data);
    };
    public boolean find(Object index) throws DataAccessException {
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
    public boolean update(Object index) throws DataAccessException {
        var game = (GameData) index;
        var gameID = game.getGameID();
        var oldGame = read(String.valueOf(gameID));
        if (DB.games.remove(oldGame) && DB.games.add(game)) {
            return true;
        }
        return false;
    };
    public boolean delete(Object index) throws DataAccessException {
        return false;
    };
    public Object read(String gameID) throws DataAccessException {
        for (var i : DB.games) {
            if (String.valueOf(i.getGameID()).equals(gameID)) {
                return i;
            }
        }
        return null;
    };
    public boolean deleteAll() throws DataAccessException {
        DB.games = new ArrayList<>();
        return true;
    }

}

package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class GameAccess implements dataAccess {

    public GameAccess(){}

    public boolean Create(Object obj){
        return false;
    };
    public boolean Find(Object index){
        return false;
    };
    public ArrayList<GameData> FindAll(){
        return DB.games;
    }
    public boolean Update(String index){
        return false;
    };
    public boolean Delete(Object index){
        return false;
    };
    public boolean Match(Object index){
        return false;
    };
    public Object Read(String index){
        return false;
    };
    public boolean DeleteAll(){
        DB.games = new ArrayList<>();
        return true;
    }

}

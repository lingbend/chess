package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class GameAccess implements dataAccess {

    public GameAccess(){}

    public boolean Create(Object obj){};
    public boolean Find(Object index){};
    public boolean Update(String index){};
    public boolean Delete(Object index){};
    public boolean Match(Object index){};
    public Object Read(String index){};
    public boolean DeleteAll(){
        DB.games = new ArrayList<>();
        return true;
    }

}

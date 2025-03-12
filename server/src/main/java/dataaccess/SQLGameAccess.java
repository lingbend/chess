package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameAccess implements DataAccess{
    public boolean create(Object obj){};
    public boolean find(Object index){};
    public ArrayList<GameData> findAll(){}
    public boolean update(Object index){};
    public boolean delete(Object index){};
    public Object read(String index){};
    public boolean deleteAll(){};
}

package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameAccessInter {
    boolean update(Object index) throws DataAccessException;
    boolean delete(Object index) throws DataAccessException;
    ArrayList<GameData> findAll() throws DataAccessException;
}

package dataaccess;

public interface GameAccessInter {
    boolean update(Object index) throws DataAccessException;
    boolean delete(Object index) throws DataAccessException;
}

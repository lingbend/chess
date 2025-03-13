package dataaccess;

import java.lang.String;

public interface DataAccess {
    boolean create(Object obj) throws DataAccessException;
    boolean find(Object index) throws DataAccessException;
    Object read(String index) throws DataAccessException;
    boolean deleteAll() throws DataAccessException;
}

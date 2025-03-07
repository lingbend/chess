package dataAccess;

import java.lang.String;

public interface dataAccess {
    boolean create(Object obj);
    boolean find(Object index);
    boolean update(Object index);
    boolean delete(Object index);
    Object read(String index);
    boolean deleteAll();
}

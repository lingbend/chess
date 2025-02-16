package dataAccess;

import java.lang.String;

public interface dataAccess {
    boolean Create(Object obj);
    boolean Find(Object index);
    boolean Update(String index);
    boolean Delete(Object index);
    boolean Match(Object index);
    Object Read(String index);
    boolean DeleteAll(Class type);
}

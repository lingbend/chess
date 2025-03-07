package dataAccess;

import java.lang.String;

public interface dataAccess {
    boolean Create(Object obj);
    boolean Find(Object index);
    boolean Update(Object index);
    boolean Delete(Object index);
    Object Read(String index);
    boolean DeleteAll();
}

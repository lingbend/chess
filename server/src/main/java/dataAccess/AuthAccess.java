package dataAccess;

public class AuthAccess implements dataAccess {

    public AuthAccess(){}

    public boolean Create(Object obj){};
    public boolean Find(Object index){};
    public boolean Update(String index){};
    public boolean Delete(Object index){};
    public boolean Match(Object index){};
    public Object Read(String index){};
    public boolean DeleteAll(Class type){};

}

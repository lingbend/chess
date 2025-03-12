package dataaccess;

public class SQLUserAccess implements DataAccess{
    public boolean create(Object obj){};
    public boolean find(Object index){};
    public boolean update(Object index){};
    public boolean delete(Object index){};
    public Object read(String index){};
    public boolean deleteAll(){};
}

package service;

import handler.*;
import DataAccess.*;

public interface Service {
    String[] run(ServiceObj obj) throws DataAccessException;
    void registerHandler(Handler handler);
}

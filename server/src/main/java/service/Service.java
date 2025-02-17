package service;

import handler.*;
import dataAccess.*;

public interface Service {
    String[] run(ServiceObj obj) throws DataAccessException;
    void registerHandler(Handler handler);
}

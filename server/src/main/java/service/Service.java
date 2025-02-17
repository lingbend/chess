package service;

import handler.*;
import dataAccess.*;

public interface Service {
    void run(ServiceObj obj) throws DataAccessException;
    void registerHandler(Handler handler);
}

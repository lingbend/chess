package service;

import handler.*;
import dataaccess.*;

public interface Service {
    String[] run(ServiceObj obj) throws DataAccessException;
    void registerHandler(Handler handler);
}

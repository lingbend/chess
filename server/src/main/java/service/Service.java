package service;

import dataAccess.*;

public interface Service {
    void run(ServiceObj obj) throws DataAccessException;
}

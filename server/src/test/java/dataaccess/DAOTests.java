package dataaccess;

import handler.JsonHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.ClearService;
import service.RequestObj;

import java.util.Map;

public class DAOTests {

    @BeforeEach
    public void initialize() throws DataAccessException{
        JsonHandler.initializeDB();
    }

    @AfterEach
    public void clearDB() throws DataAccessException {
        var service = new ClearService();
        var request = new RequestObj(Map.of());
        var handler = new JsonHandler(service);
        service.registerHandler(handler);
        service.run(request);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void createPositive(Class<?> accessClass){

    }

    @Test
    public void createNegative(){

    }

    @Test
    public void findPositive(){

    }

    @Test
    public void findNegative(){

    }

    @Test
    public void deletePositive(){

    }

    @Test
    public void deleteAllPositive(){

    }

    @Test
    public void readPositive(){

    }

    @Test
    public void readNegative(){

    }

    @Test
    public void findAllPositive(){

    }

    @Test
    public void findAllNegative(){

    }

    @Test
    public void updatePositive(){

    }

    @Test
    public void updateNegative(){

    }

// auth: create(authData), find(authToken), delete(authToken), read(authToken), deleteAll
    // game:create(gameData), find(gameID), findAll, update(gameData), delete(gameID), read(gameID), deleteAll
    //user:create(userData), find(username), read(username), deleteAll






}

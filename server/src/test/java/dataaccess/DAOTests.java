package dataaccess;

import handler.JsonHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.ClearService;
import service.RequestObj;

import java.util.ArrayList;
import model.*;

import java.util.Map;

public class DAOTests {

    private final AuthData goodAuthData = new AuthData("bobby", "123456");
    String goodAuthToken = "123456";
    private final GameData goodGameData = new GameData(789, "bobby's game");
    String goodGameID = "789";
    private final UserData goodUserData = new UserData("bobby", "froggy9", "bob@cratchet.com");
    String goodUsername = "bobby";


    private boolean isInterfaceOf(Class<?> inClass, Class<?> inInterface) {
        for (var i : inClass.getInterfaces()) {
            if (i == inInterface) {
                return true;
            }
        }
        return false;
    }

    private boolean goodCreations(Class<?> accessClass, DataAccess dataAccess) throws DataAccessException {
        boolean result = false;

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            result = dataAccess.create(goodAuthData);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            result = dataAccess.create(goodGameData);
        }
        else if (isInterfaceOf(accessClass, UserAccessInter.class)) {
            result = dataAccess.create(goodUserData);
        }
        return result;
    }

    private void clearStaticDB() {
        DB.games = new ArrayList<>();
        DB.auth = new ArrayList<>();
        DB.users = new ArrayList<>();
    }

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
        clearStaticDB();
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void createPositive(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());

        Assertions.assertTrue(goodCreations(accessClass, dataAccess));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void createNegative(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());

        boolean result = dataAccess.create(null);

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void findPositive(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());
        goodCreations(accessClass, dataAccess);
        boolean result = false;

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            result = dataAccess.find(goodAuthToken);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            result = dataAccess.find(goodGameID);
        }
        else if (isInterfaceOf(accessClass, UserAccessInter.class)) {
            result = dataAccess.find(goodUsername);
        }

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void findNegative(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());
        boolean result = false;

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            result = dataAccess.find(goodAuthToken);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            result = dataAccess.find(goodGameID);
        }
        else if (isInterfaceOf(accessClass, UserAccessInter.class)) {
            result = dataAccess.find(goodUsername);
        }

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class})
    public void deletePositive(Class<?> accessClass) throws Exception{
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());
        goodCreations(accessClass, dataAccess);
        boolean result = false;

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            result = dataAccess.find(goodAuthToken);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            result = dataAccess.find(goodGameID);
        }

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void deleteAllPositive(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());
        goodCreations(accessClass, dataAccess);

        boolean result = dataAccess.deleteAll();

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void readPositive(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());
        goodCreations(accessClass, dataAccess);

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            var result = (AuthData) dataAccess.read(goodAuthToken);
            Assertions.assertEquals(goodAuthData, result);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            var result = (GameData) dataAccess.read(goodGameID);
            Assertions.assertEquals(goodGameData, result);
        }
        else if (isInterfaceOf(accessClass, UserAccessInter.class)) {
            var result = (UserData) dataAccess.read(goodUsername);
            Assertions.assertEquals(goodUserData, result);
        }
        else {
            Assertions.fail();
        }
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthAccess.class, SQLAuthAccess.class, GameAccess.class,
            SQLGameAccess.class, UserAccess.class, SQLUserAccess.class})
    public void readNegative(Class<?> accessClass) throws Exception {
        var dataAccess = (DataAccess) (accessClass.getDeclaredConstructor().newInstance());

        if (isInterfaceOf(accessClass, AuthAccessInter.class)) {
            var result = (AuthData) dataAccess.read(goodAuthToken);
            Assertions.assertNull(result);
        }
        else if (isInterfaceOf(accessClass, GameAccessInter.class)) {
            var result = (GameData) dataAccess.read(goodGameID);
            Assertions.assertNull(result);
        }
        else if (isInterfaceOf(accessClass, UserAccessInter.class)) {
            var result = (UserData) dataAccess.read(goodUsername);
            Assertions.assertNull(result);
        }
        else {
            Assertions.fail();
        }
    }

    @Test
    public void findAllPositive(){

    }

    @Test
    public void findAllNegative(){

    }

    @ParameterizedTest
    @ValueSource(classes = {GameAccess.class, SQLGameAccess.class})
    public void updatePositive(Class<?> accessClass) throws Exception {
        var dataAccess = (GameAccessInter) (accessClass.getDeclaredConstructor().newInstance());
        goodCreations(accessClass, (DataAccess) dataAccess);

        GameData updatedGameData = new GameData(789, "frodo's game");
        updatedGameData.setWhiteUsername("homo habilus");
        boolean result = dataAccess.update(updatedGameData);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {GameAccess.class, SQLGameAccess.class})
    public void updateNegative(Class<?> accessClass) throws Exception{
        var dataAccess = (GameAccessInter) (accessClass.getDeclaredConstructor().newInstance());

        GameData updatedGameData = new GameData(789, "frodo's game");
        updatedGameData.setWhiteUsername("homo habilus");
        boolean result = dataAccess.update(updatedGameData);

        Assertions.assertFalse(result);
    }

// auth: create(authData), find(authToken), delete(authToken), read(authToken), deleteAll
    // game:create(gameData), find(gameID), findAll, update(gameData), delete(gameID), read(gameID), deleteAll
    //user:create(userData), find(username), read(username), deleteAll
}

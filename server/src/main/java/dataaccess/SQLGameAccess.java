package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class SQLGameAccess implements DataAccess, GameAccessInter {

    public SQLGameAccess(){}

    public boolean create(Object obj) throws DataAccessException {
        if (obj == null) {
            return false;
        }
        GameData gameData = (GameData) obj;
        int gameID = gameData.getGameID();
        String gameName = gameData.getGameName();
        ChessGame game = gameData.getGame();
        String whiteUsername = gameData.getWhiteUsername();
        String blackUsername = gameData.getBlackUsername();
        String state = gameData.getState();

        //needs to be refined to actually work here
        String jsonGame = new Gson().toJson(game);

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT games (gameID, gameName, game, whiteUsername, blackUsername, state)" +
                    " VALUES(?, ?, ?, ?, ?, ?)";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, gameName);
            preparedStatement.setString(3, jsonGame);
            preparedStatement.setString(4, whiteUsername);
            preparedStatement.setString(5, blackUsername);
            preparedStatement.setString(6, state);


            preparedStatement.executeUpdate();

            return true;

        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public boolean find(Object index) throws DataAccessException {
        int gameID;
        if (index.getClass() == String.class) {
            gameID = Integer.decode((String) index);
        }
        else {
            gameID = (int) index;

        }

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID FROM games WHERE gameID=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);

            var findResponse = preparedStatement.executeQuery();

            if (findResponse.next()) {
                return true;
            }
            else {
                return false;
            }
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public ArrayList<GameData> findAll() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            var preparedStatement = conn.prepareStatement(statement);

            var findResponse = preparedStatement.executeQuery();

            ArrayList<GameData> gameList = new ArrayList<>();

            while (findResponse.next()) {
                GameData gameDataEntry = getGameData(findResponse);

                gameList.add(gameDataEntry);
            }

            return gameList;
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static GameData getGameData(ResultSet findResponse) throws SQLException {
        int gameID = findResponse.getInt("gameID");
        String gameName = findResponse.getString("gameName");
        String jsonGame = findResponse.getString("game");
        ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class); //Need to make this part work

        GameData gameDataEntry = new GameData(gameID, gameName, game);

        gameDataEntry.setWhiteUsername(findResponse.getString("whiteUsername"));
        gameDataEntry.setBlackUsername(findResponse.getString("blackUsername"));
        return gameDataEntry;
    }

    public boolean update(Object index) throws DataAccessException {
        GameData gameData = (GameData) index;
        int gameID = gameData.getGameID();

        if (find(gameID) && delete(gameID)) {
            if (create(gameData)) {
                return true;
            }
        }
        return false;
    }

    public boolean delete(Object index) throws DataAccessException {
        int gameID = (int) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM games WHERE gameID=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);

            preparedStatement.executeUpdate();

            return true;
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public Object read(String index) throws DataAccessException {
        int gameID = Integer.decode(index);

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, state" +
                    " FROM games WHERE gameID=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);

            var findResponse = preparedStatement.executeQuery();

            if (findResponse.next()) {
                return getGameData(findResponse);
            }
            else {
                return null;
            }
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    };

    public boolean deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE games";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;
        }

        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

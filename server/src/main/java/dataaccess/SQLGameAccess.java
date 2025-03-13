package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class SQLGameAccess implements DataAccess{

    public SQLGameAccess(){}

    public boolean create(Object obj){
        GameData gameData = (GameData) obj;
        int gameID = gameData.getGameID();
        String gameName = gameData.getGameName();
        ChessGame game = gameData.getGame();

        //needs to be refined to actually work here
        String jsonGame = new Gson().toJson(game);

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT games (gameID, gameName, game) VALUES(?, ?, ?)";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, gameName);
            preparedStatement.setString(3, jsonGame);

            preparedStatement.executeUpdate();

            return true;

        }

        catch (Exception ex) {
            return false;
        }
    }

    public boolean find(Object index){
        int gameID = (int) index;

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

        catch (Exception ex) {
            return false;
        }
    }

    public ArrayList<GameData> findAll(){
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

        catch (Exception ex) {
            return new ArrayList<GameData>();
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

    public boolean update(Object index){
        GameData gameData = (GameData) index;
        int gameID = gameData.getGameID();

        if (delete(gameID)) {
            if (create(gameData)) {
                return true;
            }
        }
        return false;
    }

    public boolean delete(Object index){
        int gameID = (int) index;

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM games WHERE gameID=?";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, gameID);

            preparedStatement.executeUpdate();

            return true;
        }

        catch (Exception ex) {
            return false;
        }
    }

    public Object read(String index){
        int gameID = Integer.decode(index);

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID FROM games WHERE gameID=?";
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

        catch (Exception ex) {
            return null;
        }
    };

    public boolean deleteAll(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE games";
            var preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            return true;
        }

        catch (Exception ex) {
            return false;
        }
    }
}

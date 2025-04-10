package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

import java.lang.reflect.Field;

public class GameDrawer {


    private ServerFacadeLocal.State currentState;
    private String username;
    private GameData currentGame;
    private final ServerFacadeLocal client;
    private static final String backColor1 = "107";
    private static final String backColor2 = "40";
    private static final String frontColor1 = "35";
    private static final String frontColor2 = "34";
    private static final String highlightColorPiece = "103";
    private static final String highlightColorMove = "42";
    private static final String em = "\u2004".repeat(3) + "\u2006" + "\u2009".repeat(3);
    private static final StringBuilder whiteHorizLabel = new StringBuilder("    " + "a" + em + "b" + em + "c" +
            em + "d" + em + "e" + em + "f" + em + "g" + em + "h" + "    ");
    private static final StringBuilder blackHorizLabel = whiteHorizLabel.reverse();


    public GameDrawer(ServerFacadeLocal clientExternal) {
        this.client = clientExternal;
        currentState = clientExternal.currentState;
        username = clientExternal.username;
        currentGame = clientExternal.currentGame;
    }

    void updateStates() {
        currentState = client.currentState;
        username = client.username;
        currentGame = client.currentGame;
    }


    public String drawBoard(ChessGame chess) throws Exception {
        updateStates();


        ChessBoard board = chess.getBoard();
        String currentBackColor = backColor2;
        String currentFrontColor = "";
        StringBuilder result = new StringBuilder();
        ChessGame.TeamColor startingColor;

        StringBuilder horizLabel;

        int row;
        int col;
        int inc;

        if (username.equals(currentGame.getWhiteUsername()) || currentState == ServerFacadeLocal.State.Observing) {
            startingColor = ChessGame.TeamColor.WHITE;
            horizLabel = whiteHorizLabel;
            row = 8;
            col = 1;
            inc = -1;
        }
        else {
            startingColor = ChessGame.TeamColor.BLACK;
            horizLabel = blackHorizLabel;
            row = 1;
            col = 8;
            inc = 1;
        }

        result.append("\u001b[39m").append(horizLabel).append(EscapeSequences.EMPTY).append("\n");


        while (row < 9 && row > 0) {
            result.append(" ").append(row).append(" ");

            while (col < 9 && col > 0) {
                currentFrontColor = getPieceColor(row, col, board, frontColor1, frontColor2);
                result.append("\u001b[").append(currentBackColor).append(";").append(currentFrontColor)
                        .append("m").append(getPieceCode(row, col, board));
                if (currentBackColor.equals(backColor1)) {
                    currentBackColor = backColor2;
                }
                else {
                    currentBackColor = backColor1;
                }
                col -= inc;
            }

            result.append("\u001b[49;39m").append(" ").append(row).append(" ").append("\n");

            if (currentBackColor.equals(backColor1)) {
                currentBackColor = backColor2;
            }
            else {
                currentBackColor = backColor1;
            }

            row += inc;
            if (startingColor == ChessGame.TeamColor.WHITE) {
                col = 1;
            }
            else {
                col = 8;
            }
        }

        result.append(horizLabel).append(EscapeSequences.EMPTY).append("\n");

        return result.toString();
    }

    private String getPieceColor(int row, int col, ChessBoard board, String whiteColor, String blackColor) {
        ChessPiece piece = board.getPiece(row, col);
        if (piece == null || piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return whiteColor;
        }
        else {
            return blackColor;
        }
    }

    private String getPieceCode(int row, int col, ChessBoard board) throws Exception{
        ChessPiece piece = board.getPiece(row, col);

        if (piece == null) {
            return EscapeSequences.EMPTY;
        }

        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();

        EscapeSequences escapeSequences = new EscapeSequences();
        Field chessField = escapeSequences.getClass().getDeclaredField((color.toString() + "_" + type.toString()));
        chessField.setAccessible(true);
        Object obj2 = chessField.get(escapeSequences);

        return obj2.toString();
    }





}

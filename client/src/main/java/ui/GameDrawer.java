package ui;

import chess.*;
import model.GameData;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
        currentState = clientExternal.clientDB.currentState;
        username = clientExternal.clientDB.username;
        currentGame = clientExternal.clientDB.currentGame;
    }

    void updateStates() {
        currentState = client.clientDB.currentState;
        username = client.clientDB.username;
        currentGame = client.clientDB.currentGame;
    }


    public String drawBoard(ChessGame chess, ChessPosition start) throws Exception {
        updateStates();

        ChessBoard board = chess.getBoard();
        String currentBackColor = backColor2;
        String currentFrontColor = "";
        String tileColor = backColor2;
        StringBuilder result = new StringBuilder();
        ChessGame.TeamColor startingColor;
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (start != null) {
            validMoves = (ArrayList<ChessMove>) chess.validMoves(start);
        }

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
                tileColor = getTileColor(tileColor);

                col -= inc;

                currentBackColor = getCurrentBackColor(start, validMoves, row, col, tileColor);


            }

            result.append("\u001b[49;39m").append(" ").append(row).append(" ").append("\n");

            row += inc;

            tileColor = getTileColor(tileColor);
            currentBackColor = getCurrentBackColor(start, validMoves, row, col, tileColor);


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

    private String getCurrentBackColor(ChessPosition start, ArrayList<ChessMove> validMoves,
                                       int row, int col, String tileColor) {
        String currentBackColor;
        if (start == null) {
            currentBackColor = tileColor;
        }
        else if (start.equals(new ChessPosition(row, col))) {
            currentBackColor = highlightColorPiece;
        }
        else if (checkIsMove(validMoves, start, row, col)) {
            currentBackColor = highlightColorMove;
        }
        else {
            currentBackColor = tileColor;
        }
        return currentBackColor;
    }

    private String getTileColor(String tileColor) {
        if (tileColor.equals(backColor1)) {
            tileColor = backColor2;
        }
        else {
            tileColor = backColor1;
        }
        return tileColor;
    }


    private boolean checkIsMove(ArrayList<ChessMove> validMoves, ChessPosition start, int row, int col) {
        ChessPosition endPos = new ChessPosition(row, col);
        if (validMoves.contains(new ChessMove(start, endPos, null))) {
            return true;
        }
        else if (validMoves.contains(new ChessMove(start, endPos, ChessPiece.PieceType.KNIGHT))) {
            return true;
        }
        else {
            return false;
        }
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

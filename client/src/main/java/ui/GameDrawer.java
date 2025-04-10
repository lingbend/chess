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
    private ServerFacadeLocal client;
    private static final String backColor1 = "107";
    private static final String backColor2 = "40";
    private static final String frontColor1 = "35";
    private static final String frontColor2 = "34";
    private static String highlightColorPiece;
    private static String highlightColorMove;

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
        String em = "\u2004".repeat(3) + "\u2006" + "\u2009".repeat(3);
        StringBuilder alpha = new StringBuilder("    " + "a" + em + "b" + em + "c" +
                em + "d" + em + "e" + em + "f" + em + "g" + em + "h" + "    ");
        StringBuilder beta = new StringBuilder(alpha).reverse();

        if (username.equals(currentGame.getWhiteUsername()) || currentState == ServerFacadeLocal.State.Observing) {
            startingColor = ChessGame.TeamColor.WHITE;
            result.append("\u001b[39m").append(alpha).append(EscapeSequences.EMPTY).append("\n");
        }
        else {
            startingColor = ChessGame.TeamColor.BLACK;
            result.append("\u001b[39m").append(beta).append(EscapeSequences.EMPTY).append("\n");
        }

        for (int row=1; row < 9; row++) {
            if (startingColor == ChessGame.TeamColor.WHITE) {
                result.append(" ").append(9-row).append(" ");
            }
            else {
                result.append(" ").append(row).append(" ");
            }
            for (int col=1; col<9; col++) {
                if (startingColor == ChessGame.TeamColor.WHITE) {
                    currentFrontColor = getPieceColor(9-row, col, board, frontColor1, frontColor2);
                    result.append("\u001b[").append(currentBackColor).append(";").append(currentFrontColor)
                            .append("m").append(getPieceCode(9-row, col, board));
                }
                else {
                    currentFrontColor = getPieceColor(row, 9-col, board, frontColor1, frontColor2);
                    result.append("\u001b[").append(currentBackColor).append(";").append(currentFrontColor).
                            append("m").append(getPieceCode(row, 9-col, board));
                }
                if (currentBackColor.equals(backColor1)) {
                    currentBackColor = backColor2;
                }
                else {
                    currentBackColor = backColor1;
                }
            }
            result.append("\u001b[49;39m");
            if (startingColor == ChessGame.TeamColor.WHITE) {
                result.append(" ").append(9-row).append(" ");
            }
            else {
                result.append(" ").append(row).append(" ");
            }
            result.append("\n");
            if (currentBackColor.equals(backColor1)) {
                currentBackColor = backColor2;
            }
            else {
                currentBackColor = backColor1;
            }
        }
        if (username.equals(currentGame.getBlackUsername()) && currentState != ServerFacadeLocal.State.Observing) {
            result.append(beta).append(EscapeSequences.EMPTY).append("\n");
        }
        else {
            result.append(alpha).append(EscapeSequences.EMPTY).append("\n");
        }
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

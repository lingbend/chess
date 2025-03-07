package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8]; //[row][col]
    public ChessPosition wKingPos;
    public ChessPosition bKingPos;

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard old) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                board[i-1][j-1] = old.getPiece(i, j);
            }
        }
        wKingPos = old.wKingPos;
        bKingPos = old.bKingPos;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        this.board[row][col] = piece;
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                wKingPos = position;
            }
            else {
                bKingPos = position;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                output.append("|" + board[i][j] + "|");
                if (j == 7) {
                    output.append("\n");
                }
            }
        }
        return output.toString();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        return this.board[row][col];
    }

    public ChessPiece getPiece(int currRow, int currCol) {
        int row = currRow - 1;
        int col = currCol - 1;
        return this.board[row][col];
    }

    public ChessPosition getWhiteKingPos() {
        return wKingPos;
    }

    public ChessPosition getBlackKingPos() {
        return bKingPos;
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];
        backStripe(ChessGame.TeamColor.BLACK, 7);
        pawnStripe(ChessGame.TeamColor.BLACK, 6);
        pawnStripe(ChessGame.TeamColor.WHITE, 1);
        backStripe(ChessGame.TeamColor.WHITE, 0);
        bKingPos = new ChessPosition(8, 5);
        wKingPos = new ChessPosition(1, 5);
    }

    private void pawnStripe(ChessGame.TeamColor color, int row) {
        for (int i = 0; i < 8; i++) {
            this.board[row][i] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }
    }

    private void backStripe(ChessGame.TeamColor color, int row) {
        this.board[row][0] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        this.board[row][1] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        this.board[row][2] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        this.board[row][3] = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        this.board[row][4] = new ChessPiece(color, ChessPiece.PieceType.KING);
        this.board[row][5] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        this.board[row][6] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        this.board[row][7] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
    }
}

package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8]; //[row][col]

    public ChessBoard() {
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
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

package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard game_board = new ChessBoard();
    private TeamColor game_turn;

    public ChessGame() {
        game_board.resetBoard();
        game_turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return game_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        game_turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = game_board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        ArrayList<ChessMove> raw_moves = (ArrayList<ChessMove>)
                piece.pieceMoves(game_board, startPosition);
        ChessBoard temp;
        ArrayList<ChessMove> cleaned_moves = new ArrayList<>(8);
        for (ChessMove i : raw_moves) {
            temp = new ChessBoard(game_board);
            makeMoveHelper(i, temp);
            if (!isInCheckHelper(piece.getTeamColor(), temp)) {
                cleaned_moves.add(i);
            }
        }
        return cleaned_moves;
    }

    private static boolean isInCheckHelper(TeamColor color, ChessBoard board) {
        return false;
    }

    private static void makeMoveHelper(ChessMove move, ChessBoard board) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        if (promo != null) {
            piece.setPieceType(promo);
        }
        board.addPiece(end, piece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        makeMoveHelper(move, game_board);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        game_board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
    }
}

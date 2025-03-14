package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor gameTurn;

    public ChessGame() {
        gameBoard.resetBoard();
        gameTurn = TeamColor.WHITE;
    }

    public ChessGame(ChessBoard board, TeamColor color) {
        gameBoard = board;
        gameTurn = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && gameTurn == chessGame.gameTurn;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return gameTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        gameTurn = team;
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
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        ArrayList<ChessMove> rawMoves = (ArrayList<ChessMove>)
                piece.pieceMoves(gameBoard, startPosition);
        ChessBoard temp;
        ArrayList<ChessMove> cleanedMoves = new ArrayList<>(8);
        for (ChessMove i : rawMoves) {
            temp = new ChessBoard(gameBoard);
            makeMoveHelper(i, temp);
            if (!isInCheckHelper(piece.getTeamColor(), temp)) {
                cleanedMoves.add(i);
            }
        }
        return cleanedMoves;
    }

    private static boolean isInCheckHelper(TeamColor color, ChessBoard board) {
        if (color == TeamColor.WHITE) {
            return ChessPiece.isKingChecked(board, board.getWhiteKingPos(), color);
        }
        else {
            return ChessPiece.isKingChecked(board, board.getBlackKingPos(), color);
        }
    }

    private void makeMoveHelper(ChessMove move, ChessBoard board) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        board.addPiece(start, null);
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
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece piece = gameBoard.getPiece(start);
        if (piece != null) {
            TeamColor color = piece.getTeamColor();
            if (color != gameTurn) {
                throw new InvalidMoveException("Not " + color + "'s turn");
            }
            for (ChessMove i : validMoves(start)) {
                if (move.equals(i)) {
                    makeMoveHelper(move, gameBoard);
                    if (gameTurn == TeamColor.WHITE) {
                        gameTurn = TeamColor.BLACK;
                    }
                    else {
                        gameTurn = TeamColor.WHITE;
                    }
                    return;
                }
            }
        }
        throw new InvalidMoveException(move + " for " + piece + " is not a valid move");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckHelper(teamColor, gameBoard);
    }


    private boolean anyValidMoves(TeamColor color) {
        int count = 0;
        ChessPiece piece;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                piece = gameBoard.getPiece(i, j);
                if (piece != null && piece.getTeamColor() == color) {
                    count++;
                    if (validMoves(new ChessPosition(i, j)).size() > 0) {
                        return true;
                    }
                    else if (count >= 16) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && !anyValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && !anyValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

}

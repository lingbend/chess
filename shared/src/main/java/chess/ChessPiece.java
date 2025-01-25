package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamcolor;
    private ChessPiece.PieceType piecetype;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamcolor = pieceColor;
        this.piecetype = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return this.teamcolor == that.teamcolor && this.piecetype == that.piecetype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.teamcolor, this.piecetype);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.teamcolor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.piecetype;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessPosition> validmoves = new ArrayList<ChessPosition>();
        int curr_x = myPosition.getColumn();
        int curr_y = myPosition.getRow();
        if (this.piecetype == PieceType.PAWN) {
            if (this.teamcolor == ChessGame.TeamColor.WHITE) {
                if (curr_y + 1 < 7 && board.getPiece(new ChessPosition(curr_y + 1, curr_x)) == null) { //row, col
                    validmoves.add(new ChessPosition(curr_y + 1, curr_x));
                }
            }
            else {

            }
        }
        else if (this.piecetype == PieceType.ROOK) {

        }
        else if (this.piecetype == PieceType.BISHOP) {

        }
        else if (this.piecetype == PieceType.KNIGHT) {

        }
        else if (this.piecetype == PieceType.QUEEN) {

        }
        else if (this.piecetype == PieceType.KING) {

        }
        return null;
    }

//    private ArrayList<ArrayList<ChessPosition>> moveLocations(ChessPosition myPosition) {
//        int curr_col = myPosition.getColumn();
//        int curr_row = myPosition.getRow();
//        ArrayList<ArrayList<ChessPosition>> index = new ArrayList<ArrayList<ChessPosition>>();
//        ArrayList<ChessPosition> temp_list = new ArrayList<ChessPosition>();
//
//        if (this.piecetype == PieceType.PAWN) {
//
//            int placeholder_row = 1;
//            for (int i = 1; (onBoard(placeholder_row) && i < 3); i++) {
//                if (this.teamcolor == ChessGame.TeamColor.WHITE) {
//                    placeholder_row = curr_row + i;
//                }
//                else {
//                    placeholder_row = curr_row - i;
//                }
//                temp_list.add(new ChessPosition(placeholder_row, curr_col));
//            }
//
//            index.add((ArrayList<ChessPosition>)temp_list.clone());
//            temp_list.clear();
//
//            if (this.teamcolor == ChessGame.TeamColor.WHITE) {
//                placeholder_row = curr_row + 1;
//            }
//            else {
//                placeholder_row = curr_row - 1;
//            }
//            int placeholder_col = curr_col - 1;
//            if (onBoard(placeholder_col) && onBoard(placeholder_row)) {
//                temp_list.add(new ChessPosition(placeholder_row, placeholder_col));
//                index.add((ArrayList<ChessPosition>)temp_list.clone());
//                temp_list.clear();
//            }
//            placeholder_col = curr_col + 1;
//            if (onBoard(placeholder_col) && onBoard(placeholder_row)) {
//                temp_list.add(new ChessPosition(placeholder_row, placeholder_col));
//                index.add((ArrayList<ChessPosition>)temp_list.clone());
//                temp_list.clear();
//            }
//
//
//        }
//
//    }

    private boolean isOccupied(ChessBoard board, ChessPosition pos) {
        if (board.getPiece(pos) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean onBoard(int num) {
        if (num > 0 && num < 9) {
            return true;
        }
        else {
            return false;
        }
    }

}

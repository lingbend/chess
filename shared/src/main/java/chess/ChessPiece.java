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
    public String toString() {
        return teamcolor + " " + piecetype;
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

    public void setPieceType(PieceType type) {
        piecetype = type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessPosition> validends = new ArrayList<>();
        int curr_x = myPosition.getColumn();
        int curr_y = myPosition.getRow();
        if (this.piecetype == PieceType.PAWN) {
            if (this.teamcolor == ChessGame.TeamColor.WHITE) {
                if (curr_y == 2) {
                    validends.addAll(checkDirUntil(board, myPosition, 0, 1, 2));
                }
                else {
                    validends.addAll(checkDirUntil(board, myPosition, 0, 1, 1));
                }
                validends.addAll(checkPawnDiagonals(board, myPosition));
            }
            else {
                if (curr_y == 7) {
                    validends.addAll(checkDirUntil(board, myPosition, 0, -1, 2));
                }
                else {
                    validends.addAll(checkDirUntil(board, myPosition, 0, -1, 1));
                }
                validends.addAll(checkPawnDiagonals(board, myPosition));
            }
        }
        else if (this.piecetype == PieceType.ROOK) {
            validends.addAll(checkDirUntil(board, myPosition, 1, 0));
            validends.addAll(checkDirUntil(board, myPosition, -1, 0));
            validends.addAll(checkDirUntil(board, myPosition, 0, 1));
            validends.addAll(checkDirUntil(board, myPosition, 0, -1));
        }
        else if (this.piecetype == PieceType.BISHOP) {
            validends.addAll(checkDirUntil(board, myPosition, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 1, -1));
            validends.addAll(checkDirUntil(board, myPosition, -1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, -1));
        }
        else if (this.piecetype == PieceType.KNIGHT) {
            validends.addAll(checkDirUntil(board, myPosition, 1, 2, 1));
            validends.addAll(checkDirUntil(board, myPosition, 1, -2, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, 2, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, -2, 1));
            validends.addAll(checkDirUntil(board, myPosition, 2, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 2, -1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -2, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -2, -1, 1));
        }
        else if (this.piecetype == PieceType.QUEEN) {
            validends.addAll(checkDirUntil(board, myPosition, 1, 0));
            validends.addAll(checkDirUntil(board, myPosition, -1, 0));
            validends.addAll(checkDirUntil(board, myPosition, 0, 1));
            validends.addAll(checkDirUntil(board, myPosition, 0, -1));
            validends.addAll(checkDirUntil(board, myPosition, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 1, -1));
            validends.addAll(checkDirUntil(board, myPosition, -1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, -1));
        }
        else if (this.piecetype == PieceType.KING) {
            validends.addAll(checkDirUntil(board, myPosition, 1, 0, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, 0, 1));
            validends.addAll(checkDirUntil(board, myPosition, 0, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 0, -1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 1, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, 1, -1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, 1, 1));
            validends.addAll(checkDirUntil(board, myPosition, -1, -1, 1));
        }

        ArrayList<ChessMove> validmoves = generateMoves(validends, myPosition);

        return validmoves;
    }

    private ArrayList<ChessMove> generateMoves (ArrayList<ChessPosition> endlist, ChessPosition start) {
        ArrayList<ChessMove> movelist = new ArrayList<>();
        for (int i = endlist.size(); i > 0; i--) {
            if (this.piecetype == PieceType.PAWN && ((this.teamcolor ==
                    ChessGame.TeamColor.WHITE && endlist.get(i - 1).getRow() == 8) ||
                    (this.teamcolor == ChessGame.TeamColor.BLACK && endlist.get(i - 1).getRow() == 1))) {
                for (PieceType j : PieceType.values() ) {
                    if (j == PieceType.KING || j == PieceType.PAWN) {
                        continue;
                    }
                    movelist.add(new ChessMove(start, endlist.get(i - 1), j));
                }
            }
            else {
                movelist.add(new ChessMove(start, endlist.get(i - 1), null));
            }
        }
        return movelist;
    }

    public static boolean isKingChecked(ChessBoard board, ChessPosition king_loc, ChessGame.TeamColor color) {
        boolean pawn_check = false;
        if (color == ChessGame.TeamColor.WHITE && (checkOnceForEnemy(board, king_loc, -1, 1,
                color, PieceType.PAWN) || checkOnceForEnemy(board, king_loc, 1, 1, color,
                PieceType.PAWN))) {
            pawn_check = true;
        }
        else if (color == ChessGame.TeamColor.BLACK && (checkOnceForEnemy(board, king_loc, -1, -1,
                color, PieceType.PAWN) || checkOnceForEnemy(board, king_loc, 1, -1, color,
                PieceType.PAWN))) {
            pawn_check = true;
        }
        if (pawn_check) {
            return true;
        }
        else if (checkDirForEnemy(board, king_loc, 1, 0, color, PieceType.ROOK)
            || checkDirForEnemy(board, king_loc, -1, 0, color, PieceType.ROOK)
            || checkDirForEnemy(board, king_loc, 0, 1, color, PieceType.ROOK)
            || checkDirForEnemy(board, king_loc, 0, -1, color, PieceType.ROOK)) {
            return true;
        }
        else if (checkDirForEnemy(board, king_loc, 1, -1, color, PieceType.BISHOP)
                || checkDirForEnemy(board, king_loc, -1, 1, color, PieceType.BISHOP)
                || checkDirForEnemy(board, king_loc, -1, -1, color, PieceType.BISHOP)
                || checkDirForEnemy(board, king_loc, 1, 1, color, PieceType.BISHOP)) {
            return true;
        }
        else if (checkOnceForEnemy(board, king_loc, 1, 2, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, 2, 1, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, -1, 2, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, 1, -2, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, -2, 1, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, 2, -1, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, -1, -2, color, PieceType.KNIGHT)
                || checkOnceForEnemy(board, king_loc, -2, -1, color, PieceType.KNIGHT)) {
            return true;
        }
        else if (checkDirForEnemy(board, king_loc, 1, -1, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, -1, 1, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, -1, -1, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, 1, 1, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, 1, 0, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, -1, 0, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, 0, 1, color, PieceType.QUEEN)
                || checkDirForEnemy(board, king_loc, 0, -1, color, PieceType.QUEEN)) {
            return true;
        }
        else if (checkOnceForEnemy(board, king_loc, 1, -1, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, -1, 1, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, -1, -1, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, 1, 1, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, 1, 0, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, -1, 0, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, 0, 1, color, PieceType.KING)
                || checkOnceForEnemy(board, king_loc, 0, -1, color, PieceType.KING)) {
            return true;
        }
        else {
            return false;
        }
    }


    private ArrayList<ChessPosition> checkDirUntil (ChessBoard board, ChessPosition start,
                                                    int col_mod, int row_mod) {
        ArrayList<ChessPosition> valid_move = new ArrayList<>();
        int curr_row = start.getRow() + row_mod;
        int curr_col = start.getColumn() + col_mod;

        while (onBoard(curr_row) && onBoard(curr_col) && !isOccupied(board, curr_row, curr_col)){
            valid_move.add(new ChessPosition(curr_row, curr_col));
            curr_row += row_mod;
            curr_col += col_mod;
        }

        if (onBoard(curr_row) && onBoard(curr_col) && isTarget(board, curr_row, curr_col)) {
            valid_move.add(new ChessPosition(curr_row, curr_col));
        }

        return valid_move;
    }

    private ArrayList<ChessPosition> checkDirUntil (ChessBoard board, ChessPosition start,
                                                    int col_mod, int row_mod, int max_dist) {

        ArrayList<ChessPosition> valid_move = new ArrayList<>();
        int curr_row = start.getRow() + row_mod;
        int curr_col = start.getColumn() + col_mod;
        int moved = 1;

        while (onBoard(curr_row) && onBoard(curr_col) && !isOccupied(board, curr_row, curr_col)
                && moved <= max_dist) {
            valid_move.add(new ChessPosition(curr_row, curr_col));
            curr_row += row_mod;
            curr_col += col_mod;
            moved += 1;
        }

        if (onBoard(curr_row) && onBoard(curr_col) && isTarget(board, curr_row, curr_col)
                && this.piecetype != PieceType.PAWN && moved <= max_dist) {
            valid_move.add(new ChessPosition(curr_row, curr_col));
        }

        return valid_move;

    }

    private static boolean checkDirForEnemy (ChessBoard board, ChessPosition start, int col_mod,
                                             int row_mod, ChessGame.TeamColor color, PieceType type) {
        int curr_row = start.getRow() + row_mod;
        int curr_col = start.getColumn() + col_mod;

        while (onBoard(curr_row) && onBoard(curr_col) && !isOccupied(board, curr_row, curr_col)){
            curr_row += row_mod;
            curr_col += col_mod;
        }

        if (onBoard(curr_row) && onBoard(curr_col) && isTarget(board, curr_row, curr_col, color, type)) {
            return true;
        }
        return false;
    }

    private static boolean checkOnceForEnemy (ChessBoard board, ChessPosition start,
                                      int col_mod, int row_mod, ChessGame.TeamColor color, PieceType type) {
        int curr_row = start.getRow() + row_mod;
        int curr_col = start.getColumn() + col_mod;

        if (onBoard(curr_row) && onBoard(curr_col) && isTarget(board, curr_row, curr_col, color, type)) {
            return true;
        }
        return false;
    }

    private ArrayList<ChessPosition> checkPawnDiagonals (ChessBoard board, ChessPosition start) {
        ArrayList<ChessPosition> moves = new ArrayList<>();
        int curr_col = start.getColumn();
        int curr_row = start.getRow();
        if (this.teamcolor == ChessGame.TeamColor.WHITE) {
            curr_row += 1;
        }
        else {
            curr_row -= 1;
        }
        if (onBoard(curr_col - 1) && onBoard(curr_row) && isTarget(board, curr_row, curr_col - 1)) {
            moves.add(new ChessPosition(curr_row, curr_col - 1));
        }
        if (onBoard(curr_col + 1) && onBoard(curr_row) && isTarget(board, curr_row, curr_col + 1)) {
            moves.add(new ChessPosition(curr_row, curr_col + 1));
        }
        return moves;
    }

    private boolean isTarget(ChessBoard chessboard, int curr_row, int curr_col) {
        if (chessboard.getPiece(curr_row, curr_col) != null &&
                chessboard.getPiece(curr_row, curr_col).teamcolor != this.teamcolor ) {
            return true;
        }
        else {
            return false;
        }
    }

    private static boolean isTarget(ChessBoard chessboard, int curr_row, int curr_col, ChessGame.TeamColor color
            , PieceType type) {
        if (chessboard.getPiece(curr_row, curr_col) != null &&
                chessboard.getPiece(curr_row, curr_col).teamcolor != color &&
                chessboard.getPiece(curr_row, curr_col).piecetype == type) {
            return true;
        }
        else {
            return false;
        }
    }

    private static boolean isOccupied(ChessBoard chessboard, int curr_row, int curr_col) {
        if (chessboard.getPiece(curr_row, curr_col) != null) {
            return true;
        }
        else {
            return false;
        }
    }


    private static boolean onBoard(int num) {
        if (num > 0 && num < 9) {
            return true;
        }
        else {
            return false;
        }
    }

}

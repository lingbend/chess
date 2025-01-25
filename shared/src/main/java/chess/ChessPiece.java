package chess;

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
        if (this.piecetype == PieceType.PAWN) {

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
}

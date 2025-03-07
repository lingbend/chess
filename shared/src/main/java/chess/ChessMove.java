package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition start_position;
    ChessPosition end_position;
    ChessPiece.PieceType promotion_type;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start_position = startPosition;
        this.end_position = endPosition;
        this.promotion_type = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start_position;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end_position;
    }
    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotion_type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(this.start_position, chessMove.start_position)
                && Objects.equals(this.end_position, chessMove.end_position) && this.promotion_type == chessMove.promotion_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start_position, this.end_position, this.promotion_type);
    }

    @Override
    public String toString() {
        String output = "Start: " + this.start_position + "End: " + this.end_position + "Promo: " + this.promotion_type;
        return output;
    }
}

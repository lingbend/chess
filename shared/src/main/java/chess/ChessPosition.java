package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return this.pos_row == that.pos_row && this.pos_col == that.pos_col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos_row, pos_col);
    }

    @Override
    public String toString() {
        String output = "Row: " + this.pos_row + " Col: " + this.pos_col;
        return output;
    }

    private final int pos_row;
    private final int pos_col;

    public ChessPosition(int row, int col) {
        this.pos_row = row;
        this.pos_col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.pos_row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.pos_col;
    }
}

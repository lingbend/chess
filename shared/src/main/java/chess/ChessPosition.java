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
        return this.posRow == that.posRow && this.posCol == that.posCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posRow, posCol);
    }

    @Override
    public String toString() {
        String output = "(" + this.posRow + ", " + this.posCol + ")";
        return output;
    }

    private final int posRow;
    private final int posCol;

    public ChessPosition(int row, int col) {
        this.posRow = row;
        this.posCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.posRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.posCol;
    }
}

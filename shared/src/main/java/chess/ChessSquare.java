package chess;

public class ChessSquare {
    ChessPiece piece;
    ChessPosition pos;

    public ChessSquare(ChessPiece piece, ChessPosition pos) {
        this.piece = piece;
        this.pos = pos;
    }

    public void removePiece() {
        this.piece = null;
    }

    public void addPiece(ChessPiece new_piece) { //assumes there is no piece here
        this.piece = new_piece;
    }

    public ChessPiece showPiece() {
        return this.piece;
    }

    public void setPos(ChessPosition new_pos) {
        this.pos = new_pos;
    }

    public ChessPosition getPos() {
        return this.pos;
    }

}

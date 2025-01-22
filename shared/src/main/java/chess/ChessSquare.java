package chess;

public class ChessSquare {

    ChessPosition pos;
    ChessPiece piece;

    public ChessSquare(ChessPosition pos, ChessPiece piece) {
        this.pos = pos;
        this.piece = piece;
    }
}


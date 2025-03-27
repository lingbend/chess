import chess.*;
//import server.Server;
import ui.ServerFacadeLocal;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
//        try {
//            var server = new Server();
//            var port = server.run(0);
//            new ServerFacadeLocal(port).run();
//            server.stop();
//        }
//        catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
    }
}
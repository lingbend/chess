package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import websocket.commands.UserGameCommand;

import java.lang.Math;
import java.lang.Character;
import java.util.ArrayList;

public class InGameUI {

    private ClientStorage clientDB;
    private ArrayList<String> parameters;

    public InGameUI (ClientStorage clientDB){
        this.clientDB = clientDB;
    }

    public void run(String command, ArrayList<String> parameters) throws Exception {
        this.parameters = parameters;
        if (command.equals("help")) {
            System.out.print(getHelp());
        }
        else if (command.equals("redraw")) {
            System.out.println("Redrawing...");
            System.out.println(clientDB.drawer.drawBoard(clientDB.currentGame.getGame(), null));
        }
        else if (command.equals("leave")) {
            System.out.println("Leaving game...");
            UserGameCommand request = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                    clientDB.authToken, clientDB.currentGameID);
            clientDB.webSocket.transmit(request);
            clientDB.webSocket.close();
            clientDB.webSocket = null;
            clientDB.currentState = ServerFacadeLocal.State.LoggedIn;
            System.out.print("\u001b[34m");
            System.out.println("... Game left");
        }
        else if (command.equals("move")) {
            if (clientDB.currentState == ServerFacadeLocal.State.Observing) {
                throw new Exception("Access denied");
            }
            if (parameters.size() > 2) {
                checkParameters(3);
            }
            else {
                checkParameters(2);
            }
            ChessPosition start = parsePosition(parameters.get(0));
            ChessPosition end = parsePosition(parameters.get(1));
            ChessMove move;
            if (parameters.size() == 3) {
                move = new ChessMove(start, end,
                        Enum.valueOf(ChessPiece.PieceType.class, parameters.get(2).toUpperCase()));
            }
            else {
                move = new ChessMove(start, end, null);
            }
            UserGameCommand request = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    clientDB.authToken, clientDB.currentGameID);
            request.setMove(move);
            clientDB.webSocket.transmit(request);
        }
        else if (command.equals("preview")) {
            checkParameters(1);
            System.out.println("Previewing moves...");
            ChessPosition start = parsePosition(parameters.get(0));
            System.out.println(clientDB.drawer.drawBoard(clientDB.currentGame.getGame(), start));
        }
        else if (command.equals("resign")) {
            System.out.println("Are you sure you want to resign? Enter yes to confirm.");
            clientDB.facade.cleanUp();
            if (clientDB.facade.command.toLowerCase().equals("yes") ||
                    clientDB.facade.command.toLowerCase().equals("y")) {
                if (clientDB.currentState == ServerFacadeLocal.State.Observing) {
                    throw new Exception("Access denied");
                }
                System.out.println("Resigning...");
                UserGameCommand request = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                        clientDB.authToken, clientDB.currentGameID);
                clientDB.webSocket.transmit(request);

                System.out.println("...Successfully Resigned");
            }
            else {
                System.out.println("...Aborted Resignation");
            }
        }
        else if (command.equals("")) {}
        else {
            System.out.println("Error: Not a valid input");
        }
    }

    private void checkParameters(int size) throws Exception {
        if (parameters.size() != size) {
            throw new FacadeException("Error: Wrong number of inputs");
        }
    }

    private String getHelp() {
        return
                """
                Available Commands:                                 Format:
                Redraw the chess game                               | redraw
                Leave the game without resigning                    | leave
                Move a piece from one square to another             | move start(a1) end(c8) promo_piece(if applicable)
                Resign the game (forfeit)                           | resign
                View legal moves for the piece on a square          | preview space(eg. e4)
                View currently available options                    | help
                """;
    }

    private ChessPosition parsePosition(String pos) throws IllegalArgumentException{
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Position parameter must be 2 letters long");
        }

        char a = pos.charAt(0);
        char b = pos.charAt(1);
        int row;
        int col;

        if (Character.isAlphabetic(a) && Character.isDigit(b)) {
            col = Character.getNumericValue(a) - 9;
            row = Character.getNumericValue(b);
        }
        else if (Character.isAlphabetic(b) && Character.isDigit(a)) {
            row = Character.getNumericValue(b) - 9;
            col = Character.getNumericValue(a);
        }
        else {
            return null;
        }

        if (Math.abs(row) > 8 || Math.abs(col) > 8) {
            throw new IllegalArgumentException("Position must use letters a-h and 1-8");
        }

        return new ChessPosition(row, col);
    }
}

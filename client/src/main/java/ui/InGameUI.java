package ui;

import chess.ChessGame;
import chess.ChessPosition;

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
            System.out.println("");
        }
        else if (command.equals("move")) {
            checkParameters(2);
            System.out.println("Previewing moves...");
            ChessPosition start = parsePosition(parameters.get(0));
            System.out.println(clientDB.drawer.drawBoard(clientDB.currentGame.getGame(), start));
        }
        else if (command.equals("preview")) {
            checkParameters(1);
            System.out.println("");
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
                Move a piece from one square to another             | move start(eg. a1) end(eg. c8)
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

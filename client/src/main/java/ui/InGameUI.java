package ui;

import chess.ChessGame;

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
            System.out.println("");
        }
        else if (command.equals("leave")) {
            System.out.println("");
        }
        else if (command.equals("move")) {
            checkParameters(2);
            System.out.println("");
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




}

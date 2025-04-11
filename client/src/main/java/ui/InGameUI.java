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
            System.out.print("");
        }
        else if (command.equals("redraw")) {
//            checkState(ServerFacadeLocal.State.LoggedIn, 0);
            System.out.println("");
        }
        else if (command.equals("leave")) {
            System.out.println("");
        }
        else if (command.equals("move")) {
            System.out.println("");
        }
        else if (command.equals("preview")) {
            System.out.println("");
        }
        else if (command.equals("")) {}
        else {
            System.out.println("Error: Not a valid input");
        }
    }

    private void checkState(ServerFacadeLocal.State state, int size) throws Exception {
        if (state == ServerFacadeLocal.State.LoggedOut && clientDB.currentState != state) {
            throw new Exception("Must logout first");
        }
        else if (state == ServerFacadeLocal.State.LoggedIn && clientDB.currentState == ServerFacadeLocal.State.LoggedOut) {
            throw new Exception("Not logged in. Login first");
        }
        else if (parameters.size() != size) {
            throw new FacadeException("Error: Wrong number of inputs");
        }
    }




}

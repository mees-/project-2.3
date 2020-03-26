package application;

import reversi.Reversi;

public class Main {
    public static void main(String[] argv) {
        System.out.println("Hello, world!");
        Reversi reversi = new Reversi();
        reversi.newGame();
        reversi.printBoard(reversi.getBoard());
        reversi.validMovesOverview(Reversi.WHITE_DISC);
        reversi.getSuggestionsBoard();
        reversi.printBoard(reversi.getSuggestionsBoard());
    }
}

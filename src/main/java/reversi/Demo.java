package reversi;

import framework.*;

import java.util.Random;
import java.util.Scanner;


public class Demo {
//    public static final String ANSI_RED = "\u001B[31m";
//    public static final String ANSI_WHITE = "\u001B[30m";
//    public static final String ANSI_BLACK = "\u001B[37m";


    public static void main(String[] args) throws InvalidTurnException, InvalidMoveException {
        Scanner input = new Scanner(System.in);
        Game reversi = new Game();

        boolean isGameOver = false;
        boolean draw = false;
        GameState currentTurn = GameState.LocalTurn;

//        reversi.setPrintToCommandLine(true);
//        reversi.setup();
//        reversi.printBoard();

        boolean turn = true;
        BoardInterface board = reversi.getBoard();
//        reversi.printBoard();

        boolean inputCommand = false;

        do {
            reversi.printBoard();
            System.out.println("Player '" + currentTurn.toString() + "', enter an empty row and column to place your mark:");
            System.out.print("> X: ");
            int row = input.nextInt();
            System.out.print("> Y: ");
            int col = input.nextInt();
            System.out.println();
            Move move = new Move(currentTurn, row, col);
            GameState gameState = reversi.doMove(move);
            switch (gameState) {
                case OneWin:
                    isGameOver = true;
                    break;
                case Draw:
                    draw = true;
                case TwoWin:
                    isGameOver = true;
                    break;
                case LocalTurn:
                    currentTurn = GameState.LocalTurn;
                    break;
                case RemoteTurn:
                    currentTurn = GameState.RemoteTurn;
                    break;
            }
        }
        while (!isGameOver);
        System.out.println("Game has ended...");
        System.out.println();

        if (draw) {
            System.out.println("The game is a tie!");
        } else {
            System.out.println("Player '" + currentTurn.toString() + "' has won the game!");
        }
    }
}



//        while (true) {
//            Move a = new Move();
//            if (turn) {
//                a.player = GameState.LocalTurn;
//            } else {
//                a.player = GameState.RemoteTurn;
//            }
//
//            System.out.println(a.player+"'s turn: ");
////            reversi.printBoard(reversi.validMovesOverview(reversi.playerTypeToCellContent(a.player)));
//
//            if (inputCommand) {
//                System.out.println("X:");
//                a.x = input.nextInt();
//                System.out.println("Y:");
//                a.y = input.nextInt();
//            } else {
//                Random random = new Random();
//                a.x = random.nextInt(8);
//                a.y = random.nextInt(8);
//            }
//
//
//            try {
//                reversi.doMove(a);
//            } catch (InvalidMoveException e) {
//                e.printStackTrace();
//
//                // We don't want to switch turn if move is invalid
//                turn = !turn;
//            }
//
////            board = reversi.getBoard();
//            reversi.printBoard();
//
//            turn = !turn;
//        }

//    public static void printBoard(BoardInterface board) {
//
//        for (int i = 0; i < board.getSize(); i++) {
//            for (int j = 0; j < board.getSize(); j++) {
//                System.out.print(ANSI_RED+ "("+i+", "+j+") ");
//                CellContent a = board.getCell(i, j);
//                if (a == CellContent.Local) {
//                    System.out.print(ANSI_BLACK + a + ANSI_RED + " | ");
//                } else if (a == CellContent.Remote) {
//                    System.out.print(ANSI_WHITE + a + ANSI_RED + " | ");
//                } else {
//                    System.out.print(ANSI_RED + a + ANSI_RED + " | ");
//                }
//
//            }
//            System.out.println("");
//            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
//        }
//        System.out.println("");
////        System.out.println(board.getCell(3,3));
//    }

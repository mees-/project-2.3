package reversi;

import framework.*;

import java.util.Random;
import java.util.Scanner;


public class ReversiDemo {


    public static void main(String[] args) throws InvalidTurnException, InvalidMoveException {
        Scanner input = new Scanner(System.in);
        ReversiGame reversi = new ReversiGame();

        boolean isGameOver = false;
        boolean draw = false;
        GameState currentTurn = GameState.TurnOne;

//        reversi.setPrintToCommandLine(true);
//        reversi.setup();
//        reversi.printBoard();

//        boolean turn = true;
//        BoardInterface board = reversi.getBoard();
//        reversi.printBoard();

//        boolean inputCommand = false;
        reversi.setup(currentTurn);
        do {
            reversi.printBoard();
            System.out.println("Player '" + currentTurn.toString() + "', enter an empty row and column to place your mark:");
            System.out.println(reversi.getBoard().getValidMoves(currentTurn).toString());
            System.out.print("> Row: ");
            int x = input.nextInt();
            System.out.print("> Column: ");
            int y = input.nextInt();
            System.out.println();
            Move move = new Move(currentTurn, x, y);
            try {
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
                    case TurnOne:
                        currentTurn = GameState.TurnOne;
                        break;
                    case TurnTwo:
                        currentTurn = GameState.TurnTwo;
                        break;
                }
            } catch (InvalidMoveException e) {
                e.printStackTrace();
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
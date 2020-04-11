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
        reversi.printBoard();

        Move move = new Move(currentTurn, 3, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 2, 4);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 1, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 1, 4);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 4, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 5, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 0, 4);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 2, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 2, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 2, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 1, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 0, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 0, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 1, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 2, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 3, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 5, 5);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 4, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 3, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 5, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 1, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 0, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 2, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 0, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 0, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 3, 6);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 0, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 1, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 5, 4);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 4, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 6, 7);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 3, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 3, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 4, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 4, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 5, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 6, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 5, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 6, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 6, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 5, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 7, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 7, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 4, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 6, 2);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 7, 1);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 5, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 7, 3);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        move = new Move(currentTurn, 0, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnTwo;

        move = new Move(currentTurn, 2, 0);
        reversi.doMove(move);
        currentTurn = GameState.TurnOne;

        System.out.println("player "+currentTurn+" can make following turns:"+ reversi.getBoard().getValidMoves(currentTurn).toString());

        if (!((ReversiBoard)reversi.getBoard()).canMakeTurn(currentTurn)) {
            System.out.println("boop");
            currentTurn = GameState.TurnTwo;
        }
//        currentTurn = GameState.TurnOne;

        do {
            System.out.println("Player '" + currentTurn.toString() + "', enter an empty row and column to place your mark:");
            System.out.println(reversi.getBoard().getValidMoves(currentTurn).toString());
            System.out.print("> Row: ");
            int x = input.nextInt();
            System.out.print("> Column: ");
            int y = input.nextInt();
            System.out.println();
            move = new Move(currentTurn, x, y);
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
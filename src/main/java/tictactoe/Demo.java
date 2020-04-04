package tictactoe;

import framework.InvalidMoveException;
import framework.InvalidTurnException;
import framework.Move;
import framework.GameState;

import java.util.Scanner;

public class Demo
{
    public static void main(String[] args) throws InvalidMoveException, InvalidTurnException {
        Scanner input = new Scanner(System.in);
        Game ttt = new Game();
        boolean isGameOver = false;
        boolean draw = false;
        GameState currentTurn = GameState.TurnOne;
        ttt.setPrintToCommandLine(true);
        ttt.setup();
        ttt.printBoard();

        do {
            System.out.println("Player '" + currentTurn.toString() + "', enter an empty row and column to place your mark:");
            System.out.print("> ");
            int row = input.nextInt()-1;
            int col = input.nextInt()-1;
            System.out.println();
            Move move = new Move(currentTurn, row, col);
            GameState gameState = ttt.doMove(move);
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
        }
        while(!isGameOver);
        System.out.println("Game has ended...");
        System.out.println();

        if(draw) {
            System.out.println("The game is a tie!");
        }
        else {
            System.out.println("Player '" + currentTurn.toString() + "' has won the game!");
        }
    }
}

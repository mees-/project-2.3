package tictactoe;

import framework.InvalidMoveException;
import framework.Move;
import framework.GameState;

import java.util.Scanner;

public class Demo
{
    public static void main(String[] args) throws InvalidMoveException {
        Scanner input = new Scanner(System.in);
        Game ttt = new Game();
        boolean isGameOver = false;
        boolean draw = false;
        GameState currentTurn = GameState.LocalTurn;
        ttt.setPrintToCommandLine(true);
        ttt.start();
        ttt.printBoard();

        do {
            System.out.println("Player \'" + currentTurn + "\', enter an empty row and column to place your mark:");
            System.out.print("> ");
            int row = input.nextInt()-1;
            int col = input.nextInt()-1;
            Move move = new Move(currentTurn, row, col);
            GameState gameState = ttt.doMove(move);
            switch (gameState) {
                case Win:
                case Draw:
                    draw = true;
                case Loss:
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
        while(!isGameOver);
        System.out.println("Game has ended.");
        System.out.println();

        if(draw) {
            System.out.println("The game is a tie!");
        }
        else {
            System.out.println("Player \'" + currentTurn + "\' has won the game!");
        }
    }
}

package tictactoe;

import framework.InvalidMoveException;
import framework.Move;
import framework.PlayerType;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InvalidMoveException {
        Scanner input = new Scanner(System.in);
        Game ttt = new Game();
        boolean isGameOver = false;
        ttt.start();
        do {
            System.out.println("Player ... enter an empty row and column to place your mark.");
            int row = input.nextInt()-1;
            int col = input.nextInt()-1;
            Move move = new Move(PlayerType.Local, row, col);
            ttt.doMove(move);
        }
        while(!isGameOver);
        System.out.println("Game has ended.");
        System.out.println();

        // Todo fix onderstaande
        if(!isGameOver) {
            System.out.println("The game is a tie!");
        }
        else {
            System.out.println("... has won the game!");
        }
    }
}

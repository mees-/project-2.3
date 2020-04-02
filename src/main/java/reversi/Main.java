//package reversi;
//
//import framework.*;
//
//import java.util.Random;
//import java.util.Scanner;
//
//
//public class Main {
//    public static final String ANSI_RED = "\u001B[31m";
//    public static final String ANSI_WHITE = "\u001B[30m";
//    public static final String ANSI_BLACK = "\u001B[37m";
//
//
//    public static void main(String[] args) {
//        Reversi reversi = new Reversi();
//        boolean turn = true;
//        BoardInterface board = reversi.getBoard();
//        printBoard(board);
//
//        boolean inputCommand = false;
//
//        while (true) {
//            Move a = new Move();
//            if (turn) {
//                a.player = PlayerType.Local;
//            } else {
//                a.player = PlayerType.Remote;
//            }
//
//            System.out.println(a.player+"'s turn: ");
//            reversi.printBoard(reversi.validMovesOverview(reversi.playerTypeToCellContent(a.player)));
//
//            if (inputCommand) {
//                Scanner input = new Scanner(System.in);
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
//            board = reversi.getBoard();
//            printBoard(board);
//
//            turn = !turn;
//        }
//
//    }
//
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
//}

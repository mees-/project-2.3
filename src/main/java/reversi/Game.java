package reversi;
import framework.*;

public class Game implements GameInterface {

    private static final int BOARD_SIZE = 8;
    private static final char BLACK_DISC = '#';
    private static final char WHITE_DISC = 'o';
    private static final char SUGGEST_DISC = 'x';
    private static final char EMPTY_SPACE = '-';

    private char playerColour;
    private char opponentColour;

    private CellContent playerCell;

    private boolean AI = false;

    private Board board;
    private char[][] suggestions;
    private char[][] charBoard;

    private GameState lastTurn;

    private GameState gameState;


    public Game() {
        init();

        // If player one; black; first
        if (gameState == GameState.TurnOne) {
            board.setCell(3, 3, CellContent.Remote);
            board.setCell(4, 4, CellContent.Remote);
            board.setCell(3, 4, CellContent.Local);
            board.setCell(4, 3, CellContent.Local);
            playerColour = BLACK_DISC;
            opponentColour = WHITE_DISC;

        // If player two; white; second
        } else {
            board.setCell(3, 3, CellContent.Local);
            board.setCell(4, 4, CellContent.Local);
            board.setCell(3, 4, CellContent.Remote);
            board.setCell(4, 3, CellContent.Remote);
            playerColour = WHITE_DISC;
            opponentColour = BLACK_DISC;
        }
    }

    private void init() {
        board = new Board(BOARD_SIZE);
        charBoard = new char[BOARD_SIZE][BOARD_SIZE];
        suggestions = new char[BOARD_SIZE][BOARD_SIZE];
        initSuggestions();
    }

    /**
     * @param playerCell
     * @return Overview of valid moves
     */
    public char[][] validMovesOverview(CellContent playerCell) {
        toCharBoard();
        initSuggestions();

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCell(i, j) == CellContent.Empty) {
                    boolean nn = validMove(playerCell, i, j, 0, -1);
                    boolean ne = validMove(playerCell, i, j, 1, -1);
                    boolean ee = validMove(playerCell, i, j, 1, 0);
                    boolean se = validMove(playerCell, i, j, 1, 1);
                    boolean ss = validMove(playerCell, i, j, 0, 1);
                    boolean sw = validMove(playerCell, i, j, -1, 1);
                    boolean ww = validMove(playerCell, i, j, -1, 0);
                    boolean nw = validMove(playerCell, i, j, -1, -1);

                    if (nn || ne || ee || se || ss || sw || ww || nw) {
                        suggestions[i][j] = SUGGEST_DISC;
                    }
                }
            }
        }
        return suggestions;
    }

    /**
     * Check if the current position contains the opposite player's colour and if the line indicated by adding checkX to
     * currentX and checkY to currentY eventually ends in the player's own colour
     * @param playerCell
     * @param currentX
     * @param currentY
     * @param checkX
     * @param checkY
     * @return boolean if move is valid.
     */
    public boolean validMove(CellContent playerCell, int currentX, int currentY, int checkX, int checkY) {
        CellContent opposite = getOpposite(playerCell);

        if ((currentX + checkX < 0) || (currentX + checkX >= board.getSize() )) {
            return false;
        }
        if ((currentY + checkY < 0) || (currentY + checkY >= board.getSize() )) {
            return false;
        }
        if (board.getCell(currentX + checkX, currentY + checkY) != opposite) {
            return false;
        }
        if ((currentX + 2 * checkX < 0) || (currentX + 2 * checkX >= board.getSize() )) {
            return false;
        }
        if ((currentY + 2 * checkY < 0) || (currentY + 2 * checkY >= board.getSize() )) {
            return false;
        }
        return check_line_match(playerCell, checkX, checkY, currentX + 2 * checkX, currentY + 2 * checkY);
    }

    /**
     * Check if there is the player's colour on the line starting at (checkX, checkY) or anywhere further by adding
     * nextX and nextY to (checkX, checkY)
     * @param playerCell
     * @param checkX
     * @param checkY
     * @param nextX
     * @param nextY
     * @return boolean if move creates line match
     */
    public boolean check_line_match(CellContent playerCell, int checkX, int checkY, int nextX, int nextY) {
        if (board.getCell(nextX, nextY) == playerCell) {
            return true;
        }
        if ((nextX + checkX < 0) || (nextX + checkX >= board.getSize() )) {
            return false;
        }
        if ((nextY + checkY < 0) || (nextY + checkY >= board.getSize() )) {
            return false;
        }
        return check_line_match(playerCell, checkX, checkY, checkX + nextX, checkY + nextY);
    }

    @Override
    public BoardInterface getBoard() {
        return board;
    }

    @Override
    public GameState doMove(Move move) throws InvalidMoveException, InvalidTurnException {
//        int localScore = 0;
//        int remoteScore = 0;
//        int emptyCells = board.getSize() * board.getSize();
        if(!validCurrentTurn(move.getPlayer())) {
            throw new InvalidTurnException(move.getPlayer() + " took two turns in a row, only one is allowed.");
        }

        CellContent player = moveToCellContent(move);

        if (validMovesOverview(player)[move.getX()][move.getY()] == SUGGEST_DISC) {
            board.setCell(move.getX(), move.getY(), player);
            flipDiscs(move, player);
        } else {
            throw new InvalidMoveException(move.getPlayer() + " can not place a disc here.");
        }

        if (canMakeTurn(getOpposite(player))) {
            setLastTurn(move.getPlayer());
        }
        return getResult(move);
    }


//        if(printToCommandLine) {
//            board.printBoard();
//        }
//        if (validMovesOverview(player)[move.getX()][move.getY()] == SUGGEST_DISC) {
//            board.setCell(move.getX(), move.getY(), player);
//            flipDiscs(move, player);
//            for (int i = 0; i < board.getSize(); i++) {
//                for (int j = 0; j < board.getSize(); j++) {
//                    if (board.getCell(i, j) == CellContent.Local) {
//                        localScore += 1;
//                    } else if (board.getCell(i, j) == CellContent.Remote) {
//                        remoteScore += 1;
//                    } else if (board.getCell(i, j) == CellContent.Empty) {
//                        emptyCells -= 1;
//                        if (emptyCells == 0) {
//                            if (localScore > remoteScore) {
//                                moveResult = MoveResult.Win;
//                            } else if (localScore < remoteScore) {
//                                moveResult = MoveResult.Loss;
//                            } else {
//                                moveResult = MoveResult.Draw;
//                            }
//                        } else {
//                            if (move.player == PlayerType.Local) {
//                                moveResult = MoveResult.LocalMove;
//                            } else if (move.player == PlayerType.Remote) {
//                                moveResult = MoveResult.RemoteMove;
//                            }
//                        }
//                    }
//                }
//            }
//
//        } else {
//            throw new InvalidMoveException("Invalid move");
//        }


    private GameState getResult(Move move) {
        CellContent player = moveToCellContent(move);
        int[] pieces = board.countPieces();
        if(pieces[0] > pieces[1] && !canMakeTurn(player)
                && !canMakeTurn(getOpposite(player))) {
            return GameState.OneWin;
        }
        else if(pieces[0] < pieces[1] && !canMakeTurn(player)
                && !canMakeTurn(getOpposite(player))) {
            return GameState.TwoWin;
        }
        else if(pieces[0] == pieces[1] && !canMakeTurn(player)
                && !canMakeTurn(getOpposite(player))) {
            return GameState.Draw;
        }
        else if(move.getPlayer() == GameState.TurnTwo) {
            return GameState.TurnOne;
        }
        else if(move.getPlayer() == GameState.TurnOne) {
            return GameState.TurnTwo;
        }
        return null;
    }

    private boolean canMakeTurn(CellContent player) {
        int count = 0;
        for (int i = 0; i < board.getSize(); i ++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (validMovesOverview(player)[i][j] == SUGGEST_DISC) {
                    count++;
                }
            }
        }
        return (count > 0);
    }

    @Override
    public void setup() {
        board.reset();
    }

    public CellContent moveToCellContent(Move move) {
        if (move.getPlayer() == GameState.TurnOne) {
            playerCell = CellContent.Local;
        } else if (move.getPlayer() == GameState.TurnTwo) {
            playerCell = CellContent.Remote;
        }
        return playerCell;
    }

    public CellContent getOpposite(CellContent playerCell) {
        CellContent opponent;
        if (playerCell == CellContent.Local) {
            opponent = CellContent.Remote;
        } else {
            opponent = CellContent.Local;
        }
        return opponent;
    }

    public void flipDiscs(Move move, CellContent player) {

        int x = move.getX();
        int y = move.getY();
        flipLine(player, x, y, 0, -1); //nn
        flipLine(player, x, y, 1, -1); //ne
        flipLine(player, x, y, 1, 0); //ee
        flipLine(player, x, y, 1, 1); //se
        flipLine(player, x, y, 0, 1); //ss
        flipLine(player, x, y, -1, 1); //sw
        flipLine(player, x, y, -1, 0); //ww
        flipLine(player, x, y, -1, -1); //nw
    }

    public boolean flipLine(CellContent player, int currentX, int currentY, int checkX, int checkY) {

        if ((currentX + checkX < 0) || (currentX + checkX >= board.getSize() )) {
            return false;
        }
        if ((currentY + checkY < 0) || (currentY + checkY >= board.getSize() )) {
            return false;
        }
        if (board.getCell(currentX + checkX, currentY + checkY) == CellContent.Empty) {
            return false;
        }
        if (board.getCell(currentX + checkX, currentY + checkY) == player) {
            return true;
        } else {
            if (flipLine(player, checkX, checkY, currentX + checkX, currentY + checkY)) {
                board.setCell(currentX + checkX, currentY + checkY, player);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public GameType getType() {
        return GameType.Reversi;
    }

    public void toCharBoard() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                switch (board.getCell(i, j)) {
                    case Empty:
                        charBoard[i][j] = EMPTY_SPACE;
                        break;
                    case Local:
                        charBoard[i][j] = playerColour;
                        break;
                    case Remote:
                        charBoard[i][j] = opponentColour;
                        break;
                }
            }
        }
    }

    public void initSuggestions() {
        suggestions = getCharBoard();
    }

    public char[][] getCharBoard() {
        return charBoard;
    }

    public void printBoard() {
        board.printBoard();
    }

    private boolean validCurrentTurn(GameState player) {
        if(lastTurn == null) {
            lastTurn = player;
            return true;
        }
        else {
            return lastTurn != player;
        }
    }

    private void setLastTurn(GameState player) {
        lastTurn = player;
    }
}

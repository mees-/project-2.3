package framework;

public interface GameInterface {

    public BoardInterface getBoard();

    public GameState doMove(Move move) throws InvalidMoveException;

    public void start();

    public GameType getType();
}

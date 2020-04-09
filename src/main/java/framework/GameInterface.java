package framework;

public interface GameInterface {

    BoardInterface getBoard();

    GameState doMove(Move move) throws InvalidMoveException, InvalidTurnException;

    void setup(GameState gameState);

    GameType getType();
}

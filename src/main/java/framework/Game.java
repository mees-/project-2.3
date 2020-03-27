package framework;

public class Game implements GameInterface {
    public Game(GameType gameType) {

    }

    public BoardInterface getBoard() {
        return null;
    }

    public MoveResult doMove(Move move) throws InvalidMoveException {
        return null;
    }

    public void start() {

    }

    public GameType getType() {
        return null;
    }
}

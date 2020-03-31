package framework;

public interface Player {
    Move getNextMove(BoardInterface board);

    void onEnd(GameResult state);
}

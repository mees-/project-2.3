package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.Move;

public interface Player {
    Move getNextMove(BoardInterface board);

    void onEnd(GameResult state);
}

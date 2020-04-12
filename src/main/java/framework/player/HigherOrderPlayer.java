package framework.player;

import framework.*;

import java.util.Set;

public class HigherOrderPlayer extends Player {
    private final Player original;

    protected HigherOrderPlayer(Player original) {
        super(original.getUsername(),original.getGameType());
        this.original = original;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        return original.getNextMove(board, possibleMoves, lastMove);
    }

    @Override
    public void onEnd(GameResult state) {
        original.onEnd(state);
    }

    @Override
    public String getUsername() {
        return original.getUsername();
    }

    @Override
    public GameState getTurn() {
        return original.getTurn();
    }

    @Override
    public void setTurn(GameState turn) {
        original.setTurn(turn);
    }

    @Override
    public GameType getGameType() {
        return super.getGameType();
    }

    public Player getSource() {
        if (original instanceof HigherOrderPlayer) {
            return ((HigherOrderPlayer) original).getSource();
        }
        return original;
    }

    public <T extends HigherOrderPlayer> HigherOrderPlayer getOrder(Class<T> type) throws IllegalStateException {
        if (type.isInstance(this)) {
            return this;
        } else if (original instanceof HigherOrderPlayer) {
            return ((HigherOrderPlayer) original).getOrder(type);
        } else {
            throw new IllegalStateException("Cant find " + type.toString() + " in the chain");
        }
    }
}

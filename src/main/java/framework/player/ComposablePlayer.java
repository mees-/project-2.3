package framework.player;

import framework.*;

import java.util.Set;

public class ComposablePlayer extends Player {
    private final Player original;

    protected ComposablePlayer(Player original) {
        super(original.getUsername(),original.getGameType());
        this.original = original;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        return original.getNextMove(board, possibleMoves, lastMove);
    }

    @Override
    public void forfeit() {
        original.forfeit();
    }

    @Override
    public void onEnd(GameResult state) {
        original.onEnd(state);
    }

    @Override
    public void setStart(GameState startingPlayer) {
        original.setStart(startingPlayer);
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
        if (original instanceof ComposablePlayer) {
            return ((ComposablePlayer) original).getSource();
        }
        return original;
    }

    public <T extends ComposablePlayer> ComposablePlayer getComposer(Class<T> type) throws ComposerNotFoundException {
        if (type.isInstance(this)) {
            return this;
        } else if (original instanceof ComposablePlayer) {
            return ((ComposablePlayer) original).getComposer(type);
        } else {
            throw new ComposerNotFoundException("Cant find " + type.toString() + " in the chain");
        }
    }

    public boolean isComposedOf(Class<? extends ComposablePlayer> type) {
        try {
            getComposer(type);
            return true;
        } catch (ComposerNotFoundException e) {
            return false;
        }
    }

    public static class ComposerNotFoundException extends Exception {
        public ComposerNotFoundException() {
        }

        public ComposerNotFoundException(String message) {
            super(message);
        }

        public ComposerNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ComposerNotFoundException(Throwable cause) {
            super(cause);
        }

        public ComposerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}

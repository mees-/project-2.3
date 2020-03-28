package framework;

public abstract class Framework {
    public abstract void move(Move move);

    public abstract State getState();

    public abstract void startGame(GameType gameType);

    public abstract void notifyMove(Move move);

    public abstract void notifyGameOffer(GameType gameType);

    public abstract void notifyTurn(PlayerType playerType);
}

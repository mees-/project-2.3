package framework;

public abstract class Player {
    public abstract Move getNextMove(BoardInterface board);

    public abstract void onLoss();

    public abstract void onWin();

    public abstract void onDraw();
}

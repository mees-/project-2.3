package framework;

import java.util.Set;

public abstract class BoardInterface {

    public abstract CellContent getCell(int x, int y);

    public abstract void setCell(int x, int y, CellContent content) throws InvalidMoveException;

    public abstract void reset();

    public abstract int getSize();


    public abstract Set<Move> getValidMoves(GameState state);

    public abstract BoardInterface clone();
}

package framework;

import java.util.Collection;

public interface BoardInterface {

    CellContent getCell(int x, int y);

    void setCell(int x, int y, CellContent content) throws InvalidMoveException;

    void reset();

    int getSize();


    Collection<Move> getValidMoves(GameState state);
}

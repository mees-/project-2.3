package framework;

import java.util.Set;

public interface BoardInterface {

    CellContent getCell(int x, int y);

    void setCell(int x, int y, CellContent content) throws InvalidMoveException;

    void reset();

    int getSize();


    Set<Move> getValidMoves(GameState state);
}

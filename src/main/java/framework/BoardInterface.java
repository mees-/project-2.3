package framework;

import java.util.Set;

public abstract class BoardInterface {

    public abstract CellContent getCell(int x, int y);

    public abstract void setCell(int x, int y, CellContent content) throws InvalidMoveException;

    public abstract void reset();

    public abstract int getSize();

    public abstract Set<Move> getValidMoves(GameState state);

    public abstract BoardInterface clone();

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("   ");

        for (int x = 0; x < getSize() - 1; x++) {
            str.append(x + " ");

        }
        str.append(getSize() - 1 + "\n");
        for (int y = 0; y < getSize(); y++) {
            str.append(y + " |");
            for (int x = 0; x < getSize(); x++) {
                str.append(getCell(x,y).toString() + "|");
            }
            str.append('\n');
//            for (int size = 0; size < getSize() * 2 + 3; size++) {
//                str.append('-');
//            }
//            str.append('\n');
        }

        return str.toString();
    }
}

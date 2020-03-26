package framework;

public interface BoardInterface {

    public CellContent getCell(int x, int y);

    public void setCell(int x, int y, CellContent content);

    public void reset();

    public int getSize();


}

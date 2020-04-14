package framework;

public enum CellContent {
    Local,
    Remote,
    Empty;

    public String toString() {
        switch (this) {
            case Local:
                return "A";
            case Remote:
                return "B";
            case Empty:
                return " ";
        }
        return null;
    }
}

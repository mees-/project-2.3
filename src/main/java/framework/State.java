package framework;

public abstract class State {
    private String remoteUsername;
    private String localUsername;
    private PlayerType turn;
    private BoardInterface board;

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public void setRemoteUsername(String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public PlayerType getTurn() {
        return turn;
    }

    public void setTurn(PlayerType turn) {
        this.turn = turn;
    }

    public BoardInterface getBoard() {
        return board;
    }

    public void setBoard(BoardInterface board) {
        this.board = board;
    }
}

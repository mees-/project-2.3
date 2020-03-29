package framework;

public class State {
    private String remoteUsername;
    private String localUsername;
    private GameState gameState;
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public BoardInterface getBoard() {
        return board;
    }

    public void setBoard(BoardInterface board) {
        this.board = board;
    }
}

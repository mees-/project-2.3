package framework;

public enum MoveResult {
    Win("win"),
    Loss("loss"),
    Draw("draw"),
    RemoteTurn,
    LocalTurn;

    private String token;

    MoveResult(String token) {
        this.token = token;
    }
    MoveResult() {

    }

    public static MoveResult fromString(String token) {
        for (MoveResult value : MoveResult.values()) {
            if (token.equalsIgnoreCase(value.token)) {
                return value;
            }
        }
        return null;
    }
}

package framework;

import connection.Connection;

public class GameFramework extends Framework {
    private GameInterface game;
    private State state;
    private Connection connection;

    public GameFramework() {
        state = new GameState();
    }

    public void Move(Move move) {
        try {
            BoardInterface board = state.getBoard();

            switch (game.doMove(move)) {
                case LocalMove:
                    board.setCell(move.x, move.y, CellContent.Local);
                    notifyTurn(PlayerType.Remote);
                    break;
                case RemoteMove:
                    board.setCell(move.x, move.y, CellContent.Remote);
                    notifyTurn(PlayerType.Local);
                    break;
                case Draw:
                case Loss:
                case Win:
                    board.reset();
                    break;
                default:
                    System.out.println("Something went wrong");
            }
        } catch (InvalidMoveException e) {
            System.out.println(e);
        }
    }

    public State getState() {
        return state;
    }

    public void startGame(GameType gameType) {
        game = new Game(gameType);
        connection.login(state.getLocalUsername());
        connection.subscribe(gameType);
    }

    public void notifyMove(Move move) {
        try {
            game.doMove(move);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    public void notifyGameOffer(GameType gameType) {
        startGame(gameType);
    }

    public void notifyTurn(PlayerType playerType) {
        state.setTurn(playerType);
    }
}

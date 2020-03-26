package connection;

import framework.GameType;
import framework.Move;

public abstract class Connection {
    public abstract void sendMove(Move move);

    public abstract void subscribe(GameType gameType);

    public abstract void login(String username);

}

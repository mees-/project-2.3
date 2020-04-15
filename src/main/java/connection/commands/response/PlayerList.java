package connection.commands.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerList extends StandardResponse {
    private ArrayList<String> players;
    public PlayerList(boolean success, ArrayList<String> players) {
        super(success);
        this.players = players;
    }

    public PlayerList(String errorMessage) {
        super(errorMessage);
    }

    public List<String> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("PlayerList:");
        for (String player : getPlayers()) {
            str.append('\n');
            str.append(player);
        }
        return str.toString();
    }
}

package connection.commands;

import connection.Parser;
import connection.commands.response.PlayerList;
import connection.commands.response.StandardResponse;
import connection.eventHandlers.EventHandler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetPlayerListCommand extends Command<PlayerList> {
    @Override
    public String getCommandString() {
        return "get playerlist";
    }

    @Override
    public boolean isValidResponse(String[] response) {
        return StandardResponse.isStandardResponse(response);
    }

    @Override
    public PlayerList parseResponse(String[] response) {
        // Unused
        return null;
    }

    @Override
    public int getLines() {
        return 2;
    }

    @Override
    public PlayerList parseResponse(String[][] response) {
        String rawList = Parser.sliceStringFromParts(response[1], 2, response[1].length);
        try {
            ArrayList<String> players = Parser.parseList(rawList);
            return new PlayerList(true, players);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

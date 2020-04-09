package connection.commands;

import connection.Parser;
import connection.commands.response.PlayerList;
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
        return EventHandler.validateWords(new String[]{"SVR", "PLAYERLIST"}, response);
    }

    @Override
    public PlayerList parseResponse(String[] response) {
        String rawList = Parser.sliceStringFromParts(response, 2, response.length);
        try {
            ArrayList<String> players = Parser.parseList(rawList);
            return new PlayerList(true, players);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

package connection.commands;

import connection.commands.response.StandardResponse;

public class MoveCommand extends Command<StandardResponse> {
    private final int cell;


    public MoveCommand(int cellTopLeftToBottomRight) {
        this.cell = cellTopLeftToBottomRight;
    }

    public MoveCommand(int boardSize, int x, int y) {
        this(y * boardSize + x);
    }

    @Override
    public String getCommandString() {
        return "move " + cell;
    }

    @Override
    public boolean isValidResponse(String[] response) {
        return StandardResponse.isStandardResponse(response);
    }

    @Override
    public StandardResponse parseResponse(String[] response) {
        return StandardResponse.isOk(response);
    }
}

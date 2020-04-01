package connection.commands;

public class MoveCommand implements ICommand {
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
        return ICommand.isStandardResponse(response);
    }

    @Override
    public CommandResponse parseResponse(String[] response) {
        return ICommand.isOk(response);
    }
}

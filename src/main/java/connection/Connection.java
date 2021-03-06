package connection;

import java.net.*;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import connection.commands.*;

import connection.commands.response.StandardResponse;
import connection.eventHandlers.*;
import framework.GameType;
import framework.Move;
import framework.player.BlockingPlayer;

public class Connection {
    private static final String serverIP = System.getenv("GAME_HOST");
    private static final int serverPort = Integer.parseInt(System.getenv("GAME_PORT"));

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    private final LinkedBlockingQueue<Command> commandsWaitingForResponse = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<EventPayload> eventsToHandle = new LinkedBlockingQueue<>();

    private final Thread readingThread = new Thread(this::connectionReader);
    private BlockingPlayer remotePlayer;

    public Connection() throws IOException {
        try {
            socket = new Socket(serverIP, serverPort);
        } catch (IOException e) {
            System.err.println("Can't connect to server, is the server running on " + serverIP + ":" + serverPort);
            throw new RuntimeException(e);
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        checkStartupMessage();
        eventHandlers.add(new GameEndHandler(this));
        eventHandlers.add(new MatchOfferHandler(this));
        eventHandlers.add(new MoveHandler(this));
        eventHandlers.add(new TurnHandler(this));
        eventHandlers.add(new ChallengeCancelledHandler(this));
        eventHandlers.add(new ChallengeHandler(this));
        readingThread.start();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    private void checkStartupMessage() throws IOException {
        String firstLine = in.readLine();
        assert firstLine.equals("Strategic Game Server Fixed [Version 1.1.0]");
        String secondLine = in.readLine();
        assert secondLine.equals("(C) Copyright 2015 Hanzehogeschool Groningen");
    }

    private void handleEventMessage(String[] message) {
        for (EventHandler handler : eventHandlers) {
            if (handler.isValidMessage(message)) {
                EventPayload payload = null;
                try {
                    payload = handler.handle(message);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (payload != null) {
                    try {
                        eventsToHandle.put(payload);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return;
            }
        }
        throw new RuntimeException(String.join(" ", message) + "\ndidn't match the first command in the queue and didn't match any eventHandler");
    }

    public <R extends StandardResponse, T extends Command<R>> GenericFuture<R> executeCommand(T command) {
        try {
            commandsWaitingForResponse.put(command);
            out.println(command.getCommandString());
            return command.getFuture();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public GenericFuture<? extends StandardResponse> sendMove(Move move, int boardSize) {
        return executeCommand(new MoveCommand(boardSize, move.getX() ,move.getY()));
    }

    public void subscribe(GameType gameType) {
        executeCommand(new SubscribeCommand(gameType));
    }

    private void connectionReader() {
        while (true) {
            try {
                String message = in.readLine();
                if (message == null) {
                    if (commandsWaitingForResponse.peek() instanceof LogoutCommand) {
                        commandsWaitingForResponse.poll().parseAndHandleResponse(null);
                        break;
                    } else {
                        throw new RuntimeException("Something's wrong");
                    }
                }
                String[] words = message.split("\\s+");
                Command command = commandsWaitingForResponse.peek();
                if (command != null && command.isValidResponse(words)) {
                    String[][] lines = new String[command.getLines()][];
                    lines[0] = words;
                    commandsWaitingForResponse.poll();
                    if (command.getLines() > 1) {
                        lines[0] = words;
                        for (int i = 1; i < command.getLines(); i++) {
                            lines[i] = in.readLine().split("\\s+");
                        }
                    }
                    StandardResponse response = command.parseAndHandleResponse(lines);
                    if (!response.isSuccess()) {
                        throw new RuntimeException("failed executing command: " + command.getCommandString() + "\nerror: " + response.getErrorMessage());
                    }
                } else {
                    handleEventMessage(words);
                }
            } catch (SocketException e) {
                // Socket was *most likely* closed
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BlockingPlayer getRemotePlayer() {
        return remotePlayer;
    }

    public void setRemotePlayer(BlockingPlayer remotePlayer) {
        this.remotePlayer = remotePlayer;
    }

    public EventPayload getEvent() throws InterruptedException {
        return eventsToHandle.take();
    }
}

package connection;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import connection.commands.*;

import connection.eventHandlers.*;
import framework.Framework;
import framework.GameType;
import framework.Move;
import framework.player.BlockingPlayer;

public class Connection {
    private static final String serverIP = System.getenv("GAME_HOST");
    private static final int serverPort = Integer.parseInt(System.getenv("GAME_PORT"));

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Framework framework;

    private final ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    private final LinkedBlockingQueue<ICommand> commandsWaitingForResponse = new LinkedBlockingQueue<>();

    private final Thread readingThread = new Thread(this::connectionReader);
    private MoveHandler moveHandler;

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
    }

    public Framework getFramework() {
        return framework;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
        moveHandler = new MoveHandler(this);
        eventHandlers.add(new GameEndHandler(this));
        eventHandlers.add(new MatchOfferHandler(this));
        eventHandlers.add(moveHandler);
        eventHandlers.add(new TurnHandler(this));
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
                handler.handle(message);
                return;
            }
        }
        throw new RuntimeException(String.join(" ", message) + "\ndidn't match the first command in the queue and didn't match any eventHandler");
    }

    private <T extends ICommand> void executeCommand(T command) {
        try {
            commandsWaitingForResponse.put(command);
            out.println(command.getCommandString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMove(Move move) {
        executeCommand(new MoveCommand(framework.getBoardSize(), move.getX() ,move.getY()));
    }

    public void subscribe(GameType gameType) {
        executeCommand(new SubscribeCommand(gameType));
    }

    public void login(String username) {
        executeCommand(new LoginCommand(username));
    }

    private void connectionReader() {
        while (true) {
            try {
                String message = in.readLine();
                String[] words = message.split("\\s+");
                ICommand command = commandsWaitingForResponse.peek();
                if (command != null && command.isValidResponse(words)) {
                    commandsWaitingForResponse.poll();
                    ICommand.CommandResponse response = command.parseResponse(words);
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

    public void setPlayer(BlockingPlayer remotePlayer) {
        this.moveHandler.setPlayer(remotePlayer);
    }
}

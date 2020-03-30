package connection;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import connection.commands.*;

import connection.eventHandlers.*;
import framework.Framework;
import framework.GameType;
import framework.Move;

public class Connection {
    private static String serverIP = System.getenv("env") == "production" ? "145.33.225.170" : "localhost";
    private static int serverPort = 7789;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Framework framework;

    private ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    private LinkedBlockingQueue<ICommand> commandsWaitingForResponse = new LinkedBlockingQueue<>();

    private boolean keepReading = true;
    private Thread readingThread = new Thread(() -> {
        while (keepReading) {
            try {
                String message = in.readLine();
                String[] words = message.split("\\s+");
                ICommand command = commandsWaitingForResponse.peek();
                if (command != null && command.isValidResponse(words)) {
                    ICommand.CommandResponse response = commandsWaitingForResponse.poll().parseResponse(words);
                    if (response.isSuccess()) {
                        System.out.println("Successfully executed command: \"" + command.getCommandString() + "\"");
                    } else {
                        System.out.println("Error executing command: \"" + command.getCommandString() + "\"" + "\nError: \"" + response.getErrorMessage() + "\"");
                    }
                } else {
                    System.err.println("server: " + message);
                    handleEventMessage(words);
                }
            } catch (IOException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    });

    public Connection(Framework framework) throws IOException {
        socket = new Socket(serverIP, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.framework = framework;

        checkStartupMessage();
        eventHandlers.add(new GameEndHandler(framework));
        eventHandlers.add(new MatchOfferHandler(framework));
        eventHandlers.add(new MoveHandler(framework));
        eventHandlers.add(new TurnHandler(framework));

        readingThread.start();
    }

    public void close() throws IOException {
        keepReading = false;
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
        executeCommand(new MoveCommand(framework.getState().getBoard().getSize(), move.getX() ,move.getY()));
    }

    public void subscribe(GameType gameType) {
        executeCommand(new SubscribeCommand(gameType));
    }

    public void login(String username) {
        executeCommand(new LoginCommand(username));
    }
}

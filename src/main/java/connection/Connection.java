package connection;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import connection.commands.*;

import connection.eventHandlers.EventHandler;
import connection.eventHandlers.MatchOfferHandler;
import connection.eventHandlers.MoveHandler;
import connection.eventHandlers.TurnHandler;
import framework.Framework;
import framework.GameType;
import framework.Move;

public abstract class Connection {
    private static String serverIP = System.getenv("env") == "production" ? "" : "localhost";
    private static int serverPort = 7789;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    private LinkedBlockingQueue<ICommand> commandsWaitingForResponse = new LinkedBlockingQueue<>();

    private boolean keepReading = true;
    private Thread readingThread = new Thread(() -> {
        while (keepReading) {
            try {
                String message = in.readLine();
                System.out.println("server: " + message);

                String[] words = message.split("\\s+");
                ICommand command = commandsWaitingForResponse.peek();
                if (command != null && command.isValidResponse(words)) {
                    commandsWaitingForResponse.poll().parseResponse(words);
                } else {
                    handleEventMessage(words);
                }
            } catch (IOException e) {
                System.err.println(e.getStackTrace());
            }
        }
    });

    public Connection(Framework framework) throws IOException {
        socket = new Socket(serverIP, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

    private void handleEventMessage(String[] message) {
        for (EventHandler handler : eventHandlers) {
            if (handler.isValidMessage(message)) {
                handler.handle(message);
                break;
            }
        }
    }

    private <T extends ICommand> void executeCommand(T command) {
        try {
            commandsWaitingForResponse.put(command);
            out.println(command.getCommandString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void sendMove(Move move);

    public void subscribe(GameType gameType) {
        executeCommand(new SubscribeCommand(gameType));
    }

    public void login(String username) {
        executeCommand(new LoginCommand(username));
    }
}

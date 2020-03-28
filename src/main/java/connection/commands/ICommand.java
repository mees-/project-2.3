package connection.commands;

import java.util.Arrays;

public interface ICommand {
    String getCommandString();

    boolean isValidResponse(String[] response);

    <R extends CommandResponse> R parseResponse(String[] response);

    class CommandResponse {
        private boolean success;
        private String errorMessage;

        public CommandResponse(boolean success) {
            this.success = success;
        }

        public CommandResponse(String errorMessage) {
            this(false);
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    static boolean isStandardResponse(String[] response) {
        return response[0].equalsIgnoreCase("ok") || response[0].equalsIgnoreCase("err");
    }

    static CommandResponse isOk(String[] response) {
        if (response[0].equalsIgnoreCase("ok")) {
            return new CommandResponse(true);
        } else {

            String error = String.join(", ", Arrays.copyOfRange(response, 1, response.length));
            return new CommandResponse(error);
        }
    }
}

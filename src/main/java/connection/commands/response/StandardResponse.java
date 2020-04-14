package connection.commands.response;

import java.util.Arrays;

public class StandardResponse {
    private final boolean success;
    private String errorMessage;

    public StandardResponse(boolean success) {
        this.success = success;
    }

    public StandardResponse(String errorMessage) {
        this(false);
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static boolean isStandardResponse(String[] response) {
        if (response == null) {
            return false;
        }
        return response[0].equalsIgnoreCase("ok") || response[0].equalsIgnoreCase("err");
    }

    public static StandardResponse isOk(String[] response) {
        if (response[0].equalsIgnoreCase("ok")) {
            return new StandardResponse(true);
        } else {

            String error = String.join(" ", Arrays.copyOfRange(response, 1, response.length));
            return new StandardResponse(error);
        }
    }
}

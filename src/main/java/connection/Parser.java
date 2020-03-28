package connection;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Parser() {}

    public static HashMap<String, String> parseMap(String raw) throws ParseException {
        HashMap<String, String> result = new HashMap<>();

        char[] chars = raw.toCharArray();
        if (chars[0] != '{') {
            throw new ParseException("Map must begin with '{'.\n string: " + raw, 0);
        }
        String token = "";
        ParseState state = ParseState.Key;
        String currentKey = "";
        for (int i = 1; i < chars.length; i++) {
            if (state != ParseState.Value && chars[i] == ' ') {
                continue;
            }
            switch (state) {
                case Key: {
                    if (chars[i] == ':') {
                        currentKey = token;
                        token = "";
                        state = ParseState.BeforeValue;
                    } else {
                        token = token + chars[i];
                    }
                    break;
                }
                case BeforeValue: {
                    if (chars[i] != '"') {
                        throw new ParseException("Invalid token.\n string: " + raw, i);
                    }
                    state = ParseState.Value;
                    break;
                }
                case Value: {
                    if (chars[i] == '"') {
                        result.put(currentKey, token);
                        currentKey = "";
                        token = "";
                        state = ParseState.AfterValue;
                    } else {
                        token = token + chars[i];
                    }
                    break;
                }
                case AfterValue: {
                    if (chars[i] == '}') {
                        return result;
                    }
                    if (chars[i] != ',') {
                        throw new ParseException("Invalid Token.\n string: " + raw, i);
                    }
                    state = ParseState.Key;
                    break;
                }
            }
        }

        throw new ParseException("Reached end of string without complete parse.\n string: " + raw, chars.length);
    }

    private enum ParseState {
        Key,
        BeforeValue,
        Value,
        AfterValue,
    }

    public static String sliceStringFromParts(String[] strs, int start, int end) {
        return String.join(" ", Arrays.copyOfRange(strs, start, end));
    }
}

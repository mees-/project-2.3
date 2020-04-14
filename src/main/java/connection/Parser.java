package connection;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private Parser() {}

    public static HashMap<String, String> parseMap(String raw) throws ParseException {
        HashMap<String, String> result = new HashMap<>();

        char[] chars = raw.trim().toCharArray();
        if (chars[0] != '{') {
            throw new ParseException("Map must begin with '{'.\n string: " + raw, 0);
        }
        StringBuilder token = new StringBuilder();
        MapParseState state = MapParseState.Key;
        String currentKey = "";
        for (int i = 1; i < chars.length; i++) {
            if (state != MapParseState.Value && chars[i] == ' ') {
                continue;
            }
            switch (state) {
                case Key: {
                    if (chars[i] == ':') {
                        currentKey = token.toString();
                        token = new StringBuilder();
                        state = MapParseState.BeforeValue;
                    } else {
                        token.append(chars[i]);
                    }
                    break;
                }
                case BeforeValue: {
                    if (chars[i] != '"') {
                        throw new ParseException("Invalid token.\n string: " + raw, i);
                    }
                    state = MapParseState.Value;
                    break;
                }
                case Value: {
                    if (chars[i] == '"') {
                        result.put(currentKey, token.toString());
                        currentKey = "";
                        token = new StringBuilder();
                        state = MapParseState.AfterValue;
                    } else {
                        token.append(chars[i]);
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
                    state = MapParseState.Key;
                    break;
                }
            }
        }

        throw new ParseException("Reached end of string without complete parse.\n string: " + raw, chars.length);
    }

    private enum MapParseState {
        Key,
        BeforeValue,
        Value,
        AfterValue,
    }

    public static ArrayList<String> parseList(String raw) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        char[] chars = raw.trim().toCharArray();
        StringBuilder token = new StringBuilder();
        ListParseState state = ListParseState.BeforeValue;
        if (chars[0] != '[') {
            throw new ParseException("List must begin with '['\nString: " + raw, 0);
        }
         for (int i = 1; i < chars.length; i++) {
             char currentChar = chars[i];
             if (currentChar == ' ') {
                 continue;
             }
             switch (state) {
                 case BeforeValue: {
                     if (currentChar == '"') {
                         state = ListParseState.Value;
                     } else {
                         throw new ParseException("Unexpected token\nString: " + raw, i);
                     }
                     break;
                 }
                 case Value: {
                     if (currentChar == '"') {
                         result.add(token.toString());
                         token = new StringBuilder();
                         state = ListParseState.AfterValue;
                     } else {
                         token.append(currentChar);
                     }
                     break;
                 }
                 case AfterValue: {
                     if (currentChar == ',') {
                         state = ListParseState.BeforeValue;
                     } else if (currentChar == ']') {
                         return result;
                     }
                     break;
                 }
             }
         }
         throw new ParseException("Reached end of string without complete parse\nString: " + raw, raw.length());
    }

    private enum ListParseState {
        BeforeValue,
        Value,
        AfterValue,
    }

    public static String sliceStringFromParts(String[] parts, int start, int end) {
        return String.join(" ", Arrays.copyOfRange(parts, start, end));
    }
}

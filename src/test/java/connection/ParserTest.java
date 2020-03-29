package connection;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;

public class ParserTest {

    @Test
    public void parseMapSingleEntry() {
        String rawMap =  "{Test: \"hello, world\"}";
        HashMap<String, String> result;
        try {
            result = Parser.parseMap(rawMap);
            assertEquals(1, result.size());
            assertEquals("hello, world", result.get("Test"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void parseMapMultipleEntries() {
        String rawMap =  "{Test: \"hello, world\", name: \"parser\", proffesion: \"parsing\"}";
        HashMap<String, String> result;
        try {
            result = Parser.parseMap(rawMap);
            assertEquals(3, result.size());
            assertEquals("hello, world", result.get("Test"));
            assertEquals("parser", result.get("name"));
            assertEquals("parsing", result.get("proffesion"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void parseMapWhitespace() {
        String rawMap =  "      {        Test:          \"hello,    w  or ld    \"      }         ";
        HashMap<String, String> result;
        try {
            result = Parser.parseMap(rawMap);
            assertEquals(1, result.size());
            assertEquals("hello,    w  or ld    ", result.get("Test"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}
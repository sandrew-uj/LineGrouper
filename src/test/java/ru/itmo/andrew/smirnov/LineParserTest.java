package ru.itmo.andrew.smirnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LineParserTest {
    @Test
    void testValid() {
        var lineParser = new LineParser();
        Assertions.assertEquals(lineParser.parseLine("\"123\";\"\""), Arrays.asList(123f, null));
        Assertions.assertEquals(lineParser.parseLine("\"1\";\"2\";\"3\";\"\""), Arrays.asList(1f, 2f, 3f, null));
        Assertions.assertEquals(lineParser.parseLine(""), Collections.emptyList());
    }

    @Test
    void testInvalid() {
        var lineParser = new LineParser();
        Assertions.assertEquals(lineParser.parseLine("\";\"12123\";\"121\""), Collections.emptyList());
        Assertions.assertEquals(lineParser.parseLine("\"\";\"12123\",\"121\""), Collections.emptyList());
        Assertions.assertEquals(lineParser.parseLine("\"\";\"\";\"121\"23232"), Collections.emptyList());
        Assertions.assertEquals(lineParser.parseLine( "8383\"200000741652251"), Collections.emptyList());
        Assertions.assertEquals(lineParser.parseLine(  "79855053897\"83100000580443402\";\"200000133000191"), Collections.emptyList());
    }

    @Test
    void testIsSensible() {
        var lineParser = new LineParser();
        Assertions.assertTrue(lineParser.isSensibleString(List.of(1f)));
        Assertions.assertFalse(lineParser.isSensibleString(List.of(1f)));
        Assertions.assertFalse(lineParser.isSensibleString(List.of(1f)));
        Assertions.assertTrue(lineParser.isSensibleString(List.of(1f, 2f)));
        Assertions.assertTrue(lineParser.isSensibleString(Arrays.asList(1f, null, 2f)));
    }
}

package ru.itmo.andrew.smirnov;

import org.junit.jupiter.api.Test;

public class LineParserTest {
    @Test
    void testMake() {
        var dsu = new DSU();
        dsu.makeSet(1);
        dsu.makeSet(2);
        assert dsu.find(1) == 1;
        assert dsu.find(2) == 2;
    }
}

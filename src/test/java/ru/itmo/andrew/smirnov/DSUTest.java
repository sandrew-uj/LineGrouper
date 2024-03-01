package ru.itmo.andrew.smirnov;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class DSUTest {

    @Test
    void testMake() {
        var dsu = new DSU();
        dsu.makeSet(1);
        dsu.makeSet(2);
        assert dsu.find(1) == 1;
        assert dsu.find(2) == 2;
    }

    @Test
    void testUnion() {
        var dsu = new DSU();
        dsu.makeSet(1);
        dsu.makeSet(2);
        dsu.union(1, 2);
        assert dsu.find(1) == 2 || dsu.find(2) == 1;
    }

    @Test
    void testFind() {
        var dsu = new DSU();
        dsu.makeSet(1);
        dsu.makeSet(2);
        dsu.makeSet(3);
        dsu.makeSet(4);
        dsu.makeSet(5);
        dsu.union(4, 5);
        dsu.union(4, 3);
        assert dsu.find(3) == dsu.find(5);
        dsu.union(1, 2);
        assert dsu.find(2) != dsu.find(3);
        assert dsu.find(1) == dsu.find(2);
    }

    @Test
    void testBig() {
        var dsu = new DSU();
        final int SIZE = 100000;

        for (int i = 0; i < SIZE; i++) {
            dsu.makeSet(i);
        }

        var rand = new Random();
        var relations = new HashMap<Integer, Integer>();
        for (int i = 0; i < SIZE / 5; i++) {
            int first = rand.nextInt(SIZE);
            int second = rand.nextInt(SIZE);
            dsu.union(first, second);
            relations.put(first, second);
        }

        for (int i = 0; i < SIZE; i++) {
            int firstFind = dsu.find(i);
            assert Objects.isNull(relations.get(i)) || firstFind == dsu.find(relations.get(i));
        }
    }
}

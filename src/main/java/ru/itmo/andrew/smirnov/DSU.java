package ru.itmo.andrew.smirnov;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that encapsulates heuristics of DSU
 * here's written more about DSU: <a href="https://habr.com/ru/articles/104772/">link on habr</a>
 */
public class DSU {
    /**
     * Array of parents
     */
    private final List<Integer> p = new ArrayList<>();
    /**
     * Array for rank heuristics
     */
    private final List<Integer> r = new ArrayList<>();

    /**
     * Method for making set of one element
     * @param x element
     */
    public void makeSet(int x) {
        while (x >= p.size()) {
            p.add(0);
            r.add(0);
        }
        p.set(x, x);
    }

    /**
     * Method for finding root parent of element
     * @param x element
     * @return root parent
     */
    public int find(int x) {
        if (p.get(x) == x) {
            return x;
        }
        p.set(x, find(p.get(x)));
        return p.get(x);
    }

    /**
     * Mehod for union two sets in which x and y persists
     * @param x element of first set
     * @param y element of second set
     */
    public void union(int x, int y) {
        x = find(x);
        y = find(y);

        if (r.get(x) < r.get(y)) {
            p.set(x, y);
        } else {
            p.set(y, x);
            if (Objects.equals(r.get(x), r.get(y))) {
                r.set(x, r.get(x) + 1);
            }
        }
    }
}
